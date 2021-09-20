import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import java.util.Base64

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("kotlinx-atomicfu")

    `maven-publish`
    signing
}

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))
    testRuntimeOnly(kotlin("test-junit5"))
}

tasks {
    tasks.getByName("apiCheck") {
        onlyIf { Library.isRelease }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = Jvm.target
            freeCompilerArgs = listOf(
                CompilerArguments.coroutines,
                CompilerArguments.time,
                CompilerArguments.optIn
            )
        }
    }

    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    publishing {
        publications {
            create<MavenPublication>(Library.name) {
                from(components["kotlin"])
                groupId = "dev.kord"
                artifactId = "kord-${project.name}"
                version = "zerotwo-SNAPSHOT"

                artifact(sourcesJar.get())

                pom {
                    name.set(Library.name)
                    description.set(Library.description)
                    url.set(Library.description)

                    organization {
                        name.set("Kord")
                        url.set("https://github.com/kordlib")
                    }

                    developers {
                        developer {
                            name.set("The Kord Team")
                        }
                    }

                    issueManagement {
                        system.set("GitHub")
                        url.set("https://github.com/kordlib/kord/issues")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:ssh://github.com/kordlib/kord.git")
                        developerConnection.set("scm:git:ssh://git@github.com:kordlib/kord.git")
                        url.set(Library.projectUrl)
                    }
                }

                    repositories {
                        maven {
                            url = URI("https://nexus.zerotwo.bot/repository/m2-snapshots-public/")

                            credentials {
                                username = System.getenv("NEXUS_USER")
                                password = System.getenv("NEXUS_PASSWORD")
                            }
                        }
                    }
            }
        }
    }
}
