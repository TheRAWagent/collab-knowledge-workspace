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
                    name.set("pack-build-cache-${project.name}")
                }
            }

            launchCache {
                volume {
                    name.set("pack-launch-cache-${project.name}")
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
                    "BP_SPRING_CLOUD_BINDINGS_DISABLED" to "true",
                    "SPRING_PROFILES_ACTIVE" to "build",
                    "BPL_JVM_THREAD_COUNT" to "20",
                    "BPL_JVM_HEAD_ROOM" to "15",
                    "JAVA_TOOL_OPTIONS" to """
                        -XX:ReservedCodeCacheSize=128M
                        -Xss512k
                    """
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
