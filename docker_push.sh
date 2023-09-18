#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t register-demo:monolithic .
docker images
docker tag register-demo:monolithic $DOCKER_USERNAME/register-demo:monolithic
docker push $DOCKER_USERNAME/register-demo:monolithic