FROM dhi.io/eclipse-temurin:25-jdk-debian13-dev AS builder
WORKDIR /home/gradle/project

ENV GRADLE_USER_HOME=/cache

COPY gradlew .
COPY gradle gradle
COPY build.gradle* build.gradle.kts* settings.gradle* settings.gradle.kts* gradle.properties* ./


# Resolve and cache dependencies (fast layer). If this runs partially, fall back without failing the build.
RUN --mount=type=cache,target=/cache ./gradlew --no-daemon dependencies || true

# Copy the rest of the source and build the application
COPY . .
RUN --mount=type=cache,target=/cache ./gradlew --no-daemon build -x test

# Consolidate the built artifact to a known path
RUN mkdir -p /output && cp build/libs/**.jar /output/app.jar

FROM dhi.io/eclipse-temurin:25-alpine3.23
WORKDIR /app

# Copy the minimal jar from the builder stage
COPY --from=builder /output/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]