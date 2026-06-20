const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const jwt = require('jsonwebtoken');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = 'tu_clave_secreta_super_segura'; // Esto debe ir en un archivo .env

// --- 1. MIDDLEWARES DE SEGURIDAD GLOBALES ---
app.use(helmet()); // Protege las cabeceras HTTP
app.use(cors()); // Permite peticiones desde el frontend (ej. React o Angular)

// Limitador de peticiones (Rate Limiting) para evitar ataques DDoS o fuerza bruta
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutos
    max: 100, // Límite de 100 peticiones por IP cada 15 minutos
    message: "Demasiadas peticiones desde esta IP, por favor intente de nuevo más tarde."
});
app.use(limiter);

// --- 2. MIDDLEWARE DE AUTENTICACIÓN ---
// Verifica que el usuario tenga un token JWT válido antes de acceder a servicios privados
const verificarToken = (req, res, next) => {
    // Excluimos rutas que no necesitan token (como el login o registro)
    if (req.path.startsWith('/api/usuarios/auth')) return next();

    const token = req.headers['authorization'];
    if (!token) return res.status(403).json({ error: 'Token requerido para acceder' });

    try {
        const tokenLimpio = token.split(" ")[1]; // Quita la palabra "Bearer"
        const decodificado = jwt.verify(tokenLimpio, JWT_SECRET);
        req.usuario = decodificado; // Agrega los datos del usuario a la petición
        next();
    } catch (error) {
        return res.status(401).json({ error: 'Token inválido o expirado' });
    }
};

app.use('/api', verificarToken);

// --- 3. RUTAS Y PROXYS DE LOS MICROSERVICIOS ---
// Configuración base para los proxies
const proxyOptions = (target) => ({
    target,
    changeOrigin: true,
    pathRewrite: { '^/api': '' }, // Quita '/api' al enviar la petición al microservicio
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

// --- 4. INICIO DEL SERVIDOR ---
app.listen(PORT, () => {
    console.log(`🚀 API Gateway ejecutándose en el puerto ${PORT}`);
    console.log(`Redirigiendo tráfico hacia los 10 microservicios del sistema clínico...`);
});