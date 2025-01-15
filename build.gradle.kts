// build.gradle.kts (Project: My_Application)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // یا نسخه‌ی جدیدتر
        classpath("com.google.gms:google-services:4.3.15") // افزودن پلاگین Google Services
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}