plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("org.jetbrains.dokka") version "1.9.20"
    java
    `java-library`
    `maven-publish`
    signing
}

group = "com.IceCreamQAQ.Aoba"
version = "0.0.3"

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
        plugin("signing")
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
                version = rootProject.version.toString()

                pom {
                    name.set("Aoba")
                    description.set("Aoba 是一个基于 Kotlin/JVM 与 Kotlin 协程 编写的纯异步的各大云服务商的 API 调用库。")
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
                        developerConnection.set("https://github.com/IceCream-QAQ/Aoba")
                        url.set("https://github.com/IceCream-QAQ/Aoba")
                    }
                }
                from(components["java"])
            }

            repositories {
                mavenLocal()
                maven {
//                    val snapshotsRepoUrl = "https://maven.icecreamqaq.com/repository/maven-snapshots/"
                    val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    url = uri(releasesRepoUrl)

                    credentials {
                        System.getenv("MAVEN_USER")?.let { username = it }
                        System.getenv("MAVEN_TOKEN")?.let { password = it }
                    }
                }
            }
        }
    }
    signing {
        val key = project.findProperty("signingKey") as String? ?: System.getenv("SIGNING_KEY")
        val password = project.findProperty("signingPassword") as String? ?: System.getenv("SIGNING_PASSWORD")

        if (key != null && password != null) {
            sign(publishing.publications[name])

            useInMemoryPgpKeys(key, password)
        }
    }

}

