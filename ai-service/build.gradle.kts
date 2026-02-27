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
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.ai:spring-ai-advisors-vector-store")
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  implementation(platform(libs.spring.ai.bom))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
