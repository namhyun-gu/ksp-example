plugins {
    id("kotlin-ksp") version "1.4.0-rc-dev-experimental-20200814"
    kotlin("jvm")
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":processor"))
    ksp(project(":processor"))
}

ksp {
    arg("option1", "value1")
    arg("option2", "value2")
}