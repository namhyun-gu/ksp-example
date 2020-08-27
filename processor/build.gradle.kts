plugins {
    kotlin("jvm")
}

group = "dev.namhyun.example"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-symbol-processing-api:1.4.0-rc-dev-experimental-20200814")
    implementation("com.squareup:kotlinpoet:1.6.0")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}