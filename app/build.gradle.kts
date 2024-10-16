buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath(libs.gradle)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false

    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    kotlin("kapt") version "1.9.22" apply false
}