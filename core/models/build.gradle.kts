plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)

    id("kotlinx-serialization")
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}
dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.serialization.json)
}