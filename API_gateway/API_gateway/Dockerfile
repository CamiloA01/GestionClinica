# Ubicar en: API_gateway/API_gateway/Dockerfile

FROM node:20-alpine
WORKDIR /app

COPY package*.json ./
RUN npm ci --omit=dev

COPY --from=build /app/target/*.jar app.jar
COPY Wallet /app/wallet

EXPOSE 3000

CMD ["node", "server.js"]
