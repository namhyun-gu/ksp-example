plugins {
    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
    kotlin("jvm")
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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

//kotlin {
//    sourceSets.main {
//        kotlin.srcDir("build/generated/ksp/main/kotlin")
//    }
//    sourceSets.test {
//        kotlin.srcDir("build/generated/ksp/test/kotlin")
//    }
//}