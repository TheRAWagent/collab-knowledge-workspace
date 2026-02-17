import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar


plugins {
    alias(libs.plugins.org.springframework.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
    group = "com.dj.ckw"
    version = "0.0.1-SNAPSHOT"
    pluginManager.withPlugin("org.springframework.boot") {
        tasks.withType<BootBuildImage>().configureEach {
            val registry = providers.gradleProperty("registryUrl")

            imageName.set(
                registry.map { "$it/${project.name}:${project.version}" }
                    .orElse("${project.name}:${project.version}")
            )

            buildCache {
                volume {
                    name.set("pack-build-cache")
                }
            }

            launchCache {
                volume {
                    name.set("pack-launch-cache")
                }
            }

            if (registry.isPresent) {
                docker {
                    publishRegistry {
                        username.set(providers.gradleProperty("registryUsername"))
                        password.set(providers.gradleProperty("registryPassword"))
                    }
                }
            }

            builder.set("paketobuildpacks/builder-jammy-java-tiny")
            runImage.set("paketobuildpacks/run-jammy-tiny")

            environment.set(
                mapOf(
                    "BP_JVM_VERSION" to libs.versions.java.get(),
                    "BP_JVM_JLINK_ENABLED" to "true",
                    "BP_SPRING_CLOUD_BINDINGS_DISABLED" to "true",
                    "BP_JVM_CDS_ENABLED" to "true",
                    "SPRING_PROFILES_ACTIVE" to "build"

                )
            )
            publish.set(registry.isPresent)
        }

        tasks.withType<BootJar> {
            layered {
                enabled.set(true)
            }
        }
    }
}
