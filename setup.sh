#!/bin/bash

# Start Colima with custom resources
echo "Starting Colima..."
colima start --cpu 4 --memory 8 --disk 40

# Remove existing juice-shop container if it exists
if docker ps -a --format '{{.Names}}' | grep -q '^juice-shop$'; then
  echo "Removing existing juice-shop container..."
  docker rm -f juice-shop
fi

# Run OWASP Juice Shop in Docker
echo "Starting OWASP Juice Shop container..."
docker run -d -p 3000:3000 --name juice-shop bkimminich/juice-shop:v13.2.0

echo "Setup complete. Juice Shop should be running on http://localhost:3000"