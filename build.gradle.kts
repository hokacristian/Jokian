// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
    }
}
plugins {
    id("com.google.dagger.hilt.android") version "2.44" apply false
    alias(libs.plugins.androidApplication) apply false
}