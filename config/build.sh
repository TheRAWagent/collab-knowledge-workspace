#!/bin/bash

images=("api-gateway" "user-service" "auth-service" "page-service" "workspace-service" "collaboration-service")

for image in "${images[@]}"; do

cd "../$image"

docker build -t "ckw-$image:latest" .

done
