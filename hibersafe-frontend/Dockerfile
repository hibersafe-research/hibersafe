FROM node:18-alpine
WORKDIR /app

# Copy package requirements first to take advantage of layer caching
COPY package.json ./
RUN npm install

# Copy application files
COPY . .

# Set React internal execution configurations
EXPOSE 8081

CMD ["npm", "start"]