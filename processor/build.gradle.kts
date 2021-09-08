plugins {
    kotlin("jvm")
}

group = "dev.namhyun.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.30-1.0.0")
    implementation("com.squareup:kotlinpoet:1.6.0")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}