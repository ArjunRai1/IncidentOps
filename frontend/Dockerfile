# ==========================
# Build Stage
# ==========================
FROM node:22-alpine AS builder

WORKDIR /app

# Copy dependency manifests first
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy application source
COPY . .

# Build production assets - passing VITE arguments as they are coopied during compile time unlike backend
# where docker compose injects it. Spring Boot is a server application that reads environment variables
# when the JVM starts. Vite compiles a static frontend.
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL
RUN npm run build

# ==========================
# Runtime Stage
# ==========================
FROM nginx:1.27-alpine

# Remove default website
RUN rm -rf /usr/share/nginx/html/*

# Copy production build
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]