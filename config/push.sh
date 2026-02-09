#!/bin/bash

# Array of image names
images=("ckw-api-gateway" "ckw-user-service" "ckw-auth-service" "ckw-page-service" "ckw-workspace-service", "ckw-collaboration-service")

# Registry URL
registry="192.168.29.125/prod"

# Loop through each image
for image in "${images[@]}"; do
  # Tag the image with latest tag
  docker tag "$image:latest" "$registry/$image:latest"

  # Push the tagged image
  docker push "$registry/$image:latest"

  echo "Pushed $registry/$image:latest"
done
