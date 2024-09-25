plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("org.jetbrains.dokka") version "1.9.20"
    java
    `java-library`
    `maven-publish`
}

group = "com.IceCreamQAQ.Aoba"
version = "0.0.1"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://maven.icecreamqaq.com/repository/maven-public/")
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("maven-publish")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.serialization")
        plugin("org.jetbrains.dokka")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        jvmToolchain(8)
    }

    dependencies {
        if (name != "core")
            api(project(":core"))
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.replace("javadocJar", Jar::class).apply {
        dependsOn(tasks.dokkaJavadoc)
        from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
        archiveClassifier.set("javadoc")
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>(name) {
                groupId = "com.IceCreamQAQ.Aoba"
                artifactId = name
                version = "1.0.0-DEV1"

                pom {
                    name.set("Aoba")
                    description.set("Aoba")
                    url.set("https://github.com/IceCream-QAQ/Aoba")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("IceCream")
                            name.set("IceCream")
                            email.set("www@withdata.net")
                        }
                    }
                    scm {
                        connection.set("https://github.com/IceCream-QAQ/Aoba")
                    }
                }
                from(components["java"])
            }

            repositories {
                mavenLocal()
                maven {
                    val snapshotsRepoUrl = "https://maven.icecreamqaq.com/repository/maven-snapshots/"
                    val releasesRepoUrl = "https://maven.icecreamqaq.com/repository/maven-releases/"
                    url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                    credentials {
                        System.getenv("MAVEN_USER")?.let { username = it }
                        System.getenv("MAVEN_TOKEN")?.let { password = it }
                    }
                }
            }
        }
    }
}