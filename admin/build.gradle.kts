plugins {
	java
	alias(libs.plugins.org.springframework.boot)
  alias(libs.plugins.spring.dependency.management)
}

group = "com.ckw"
version = "0.0.1-SNAPSHOT"
description = "admin"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
	}
}

dependencies {
	implementation(platform(libs.spring.cloud.bom))
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("de.codecentric:spring-boot-admin-starter-server")
	implementation("org.springframework.cloud:spring-cloud-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation(platform(libs.spring.boot.admin.bom))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
