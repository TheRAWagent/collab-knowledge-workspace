FROM gradle:9.2.1-jdk25-ubi AS builder
WORKDIR /home/gradle/project

COPY build.gradle* build.gradle.kts* settings.gradle* settings.gradle.kts* gradle.properties* ./


# Resolve and cache dependencies (fast layer). If this runs partially, fall back without failing the build.
RUN gradle --no-daemon assemble -x test --no-parallel || true

# Copy the rest of the source and build the application
COPY . .
RUN gradle --no-daemon clean assemble -x test --no-parallel

# Consolidate the built artifact to a known path
RUN mkdir -p /output && cp build/libs/**.jar /output/app.jar

FROM dhi.io/eclipse-temurin:25-alpine3.23
WORKDIR /app

# Copy the minimal jar from the builder stage
COPY --from=builder /output/app.jar /app/app.jar

EXPOSE 8080
EXPOSE 9090

ENTRYPOINT ["java","-Djava.io.tmpdir=/app","-jar","/app/app.jar"]
