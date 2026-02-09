#!/bin/bash

# Array of images to pull
images=("ckw-api-gateway" "ckw-user-service" "ckw-auth-service" "ckw-page-service" "ckw-workspace-service") 

# Registry URL
registry="192.168.29.125/prod" 

# Loop through images and pull each one
for image in "${images[@]}"; do
	docker pull "$registry/$image:latest"
	echo "Pulled $registry/$image:latest"
done
