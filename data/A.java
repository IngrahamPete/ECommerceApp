plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}