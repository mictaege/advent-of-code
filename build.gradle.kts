plugins {
    kotlin("jvm") version "1.9.21"
}

group = "io.github.mictaege"
version = "2023"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}