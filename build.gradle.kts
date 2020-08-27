plugins {
    kotlin("jvm") version "1.4.0-rc" apply false
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.0-rc"))
    }

    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}
