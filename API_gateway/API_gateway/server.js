const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const jwt = require('jsonwebtoken');
const http = require('http');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = 'tu_clave_secreta_super_segura';

// --- DEFINICIÓN DE MICROSERVICIOS ---
const microservicios = [
    { nombre: 'Usuarios',        ruta: '/api/usuarios',       puerto: 8081 },
    { nombre: 'Reservas',        ruta: '/api/reservas',       puerto: 8082 },
    { nombre: 'Reportes',        ruta: '/api/reportes',       puerto: 8083 },
    { nombre: 'Profesionales',   ruta: '/api/profesionales',  puerto: 8084 },
    { nombre: 'Pagos',           ruta: '/api/pagos',          puerto: 8085 },
    { nombre: 'Agenda',          ruta: '/api/agendas',        puerto: 8086 },
    { nombre: 'Notificaciones',  ruta: '/api/notificaciones', puerto: 8087 },
    { nombre: 'Ficha Clinica',   ruta: '/api/ficha-clinica',  puerto: 8088 },
    { nombre: 'Especialidades',  ruta: '/api/especialidades', puerto: 8089 },
    { nombre: 'Pacientes',       ruta: '/api/pacientes',      puerto: 8090 },
];

// --- 1. HEALTH CHECK DE MICROSERVICIO ---
const verificarConexion = (puerto) => {
    return new Promise((resolve) => {
        const req = http.get(`http://localhost:${puerto}/actuator/health`, (res) => {
            resolve(res.statusCode === 200);
        });
        req.setTimeout(2000, () => {
            req.destroy();
            resolve(false);
        });
        req.on('error', () => resolve(false));
    });
};

// --- 2. MOSTRAR ESTADO DE MICROSERVICIOS AL ARRANCAR ---
const mostrarEstado = async () => {
    console.log('\n╔══════════════════════════════════════════════════════════╗');
    console.log('║       SISTEMA DE GESTION CLINICA - GATEWAY               ║');
    console.log('╠══════════════════════════════════════════════════════════╣');
    console.log(`║  Gateway corriendo en: http://localhost:${PORT}              ║`);
    console.log('╠══════════════════════════════════════════════════════════╣');
    console.log('║  ESTADO DE MICROSERVICIOS:                               ║');
    console.log('╠══════════════════════════════════════════════════════════╣');

    let online = 0;
    let offline = 0;

    for (const ms of microservicios) {
        const conectado = await verificarConexion(ms.puerto);
        const estado = conectado ? '🟢 ONLINE ' : '🔴 OFFLINE';
        if (conectado) online++; else offline++;
        console.log(`║  ${estado}  ${ms.nombre.padEnd(16)} → localhost:${ms.puerto}  ║`);
    }

    console.log('╠══════════════════════════════════════════════════════════╣');
    console.log(`║  Online: ${String(online).padEnd(2)}  |  Offline: ${String(offline).padEnd(2)}                            ║`);
    console.log('╚══════════════════════════════════════════════════════════╝\n');
};

// --- 3. MIDDLEWARES DE SEGURIDAD ---
app.use(helmet());
app.use(cors());

const limiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
    message: { error: 'Demasiadas peticiones. Intente mas tarde.' }
});
app.use(limiter);

// --- 4. MIDDLEWARE DE AUTENTICACION ---
const verificarToken = (req, res, next) => {
    if (req.path.startsWith('/api/usuarios/auth')) return next();

    const token = req.headers['authorization'];
    if (!token) return res.status(403).json({ error: 'Token requerido para acceder' });

    try {
        const tokenLimpio = token.split(' ')[1];
        const decodificado = jwt.verify(tokenLimpio, JWT_SECRET);
        req.usuario = decodificado;
        next();
    } catch (error) {
        return res.status(401).json({ error: 'Token invalido o expirado' });
    }
};

app.use('/api', verificarToken);

// --- 5. REGISTRO DINAMICO DE RUTAS Y PROXYS ---
const proxyOptions = (puerto) => ({
    target: `http://localhost:${puerto}`,
    changeOrigin: true,
    pathRewrite: {},
    on: {
        error: (err, req, res) => {
            res.status(503).json({
                error: 'Microservicio no disponible',
                detalle: `No se pudo conectar al servicio en puerto ${puerto}`
            });
        }
    }
});


// Enrutamiento a cada microservicio (Asumiendo que corren en localhost con distintos puertos)
app.use('/api/usuarios', createProxyMiddleware(proxyOptions('http://localhost:8081')));
app.use('/api/pacientes', createProxyMiddleware(proxyOptions('http://localhost:8086')));
app.use('/api/profesionales', createProxyMiddleware(proxyOptions('http://localhost:8084')));
app.use('/api/especialidades', createProxyMiddleware(proxyOptions('http://localhost:8089')));
app.use('/api/agendas', createProxyMiddleware(proxyOptions('http://localhost:8090')));
app.use('/api/reservas', createProxyMiddleware(proxyOptions('http://localhost:8082')));
app.use('/api/pagos', createProxyMiddleware(proxyOptions('http://localhost:8085')));
app.use('/api/notificaciones', createProxyMiddleware(proxyOptions('http://localhost:8087')));
app.use('/api/sucursales', createProxyMiddleware(proxyOptions('http://localhost:8088')));
app.use('/api/reportes', createProxyMiddleware(proxyOptions('http://localhost:8083')));

// --- 6. RUTA DE HEALTH CHECK DEL GATEWAY ---
app.get('/health', (req, res) => {
    res.json({ status: 'UP', gateway: 'API Gateway Clinica', puerto: PORT });
});

// --- 7. ARRANQUE DEL SERVIDOR ---
app.listen(PORT, async () => {
    await mostrarEstado();
});