FROM node:18-alpine AS builder
WORKDIR /app

# Copy package requirements first to take advantage of layer caching
COPY package.json ./
RUN npm install

# Copy application files
COPY . .

# Serve the build files using Nginx
RUN npm run build
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html

# Set React internal execution configurations
EXPOSE 8081

CMD ["nginx", "-g", "daemon off;"]