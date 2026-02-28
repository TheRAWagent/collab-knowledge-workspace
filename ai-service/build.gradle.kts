plugins {
	java
	alias(libs.plugins.org.springframework.boot)
	alias(libs.plugins.spring.dependency.management)
}

group = "com.dj.ckw"
version = "0.0.1-SNAPSHOT"
description = "AI Service for Collab Knowledge Workspace"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.ai:spring-ai-advisors-vector-store")
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
  implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
  implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
  implementation("io.micrometer:micrometer-registry-prometheus")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
  implementation("ch.qos.logback:logback-classic")
  implementation(libs.logstash.logback.encoder)
	implementation(libs.springdoc.openapi.webmvc.scalar)
	implementation(platform(libs.spring.ai.bom))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
