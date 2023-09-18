#!/bin/bash
# Compile the Spring Boot application
mvn clean package -Dmaven.test.skip=true

# Build a Docker image from the compiled application
docker build --build-arg=target/*.jar -t register-demo .

# List all Docker images
#docker image ls

# Run the Docker image in detached mode, exposing port 8080 on the host machine
docker run -d -p 8080:8080 register-demo