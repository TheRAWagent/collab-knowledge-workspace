#!/bin/bash

images=("api-gateway" "user-service" "auth-service" "page-service" "workspace-service" "collaboration-service")
cd ..

for image in "${images[@]}"; do
    if [[ "$image" == "collaboration-service" ]]; then
        cd "$image" || exit
    docker build -t "$image:0.0.1-SNAPSHOT" .
    cd ..
else
       bash ./gradlew ":$image:bootBuildImage" -PregistryUrl=192.168.29.125/prod
fi
done
