import com.google.protobuf.gradle.id

plugins {
    java
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.com.google.protobuf)
}

group = "com.dj.ckw"
version = "0.0.1-SNAPSHOT"
description = "user-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
    }
}


extra["springGrpcVersion"] = "1.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("io.grpc:grpc-services")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")
    implementation("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-redis-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.grpc:spring-grpc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.springdoc.openapi.webmvc.scalar)
    implementation(platform(libs.aws.sdk.bom))
    implementation("software.amazon.awssdk:sesv2")
    implementation(libs.bouncycastle.provider)
    implementation("io.grpc:grpc-netty-shaded")
    modules {
        module("io.grpc:grpc-netty") {
            replacedBy(
                "io.grpc:grpc-netty-shaded",
                "Use Netty shaded instead of regular Netty"
            )
        }
    }
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("ch.qos.logback:logback-classic")
    implementation(libs.logstash.logback.encoder)
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(platform(libs.spring.grpc.bom))
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protoc.get()}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.codegen.get()}"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("@generated=omit")
                }
            }
        }
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}
