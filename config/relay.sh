#!/bin/bash

# This script is to relay hardened utility images from dhi.io to harbor

images=("grafana:12" "opentelemetry-collector:0" "postgres:18-alpine3.22" "prometheus:3.9" "redis:8" "tempo:2")

for image in "${images[@]}"; do
    docker pull "dhi.io/$image"
    docker tag "dhi.io/$image" "192.168.29.125/prod/$image"
    docker push "192.168.29.125/prod/$image"
    docker rmi "dhi.io/$image"
    docker rmi "192.168.29.125/prod/$image"
done
