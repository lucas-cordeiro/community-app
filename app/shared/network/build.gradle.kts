plugins {
    alias(libs.plugins.android.library)
}

apply(from = "$rootDir/common-android.gradle")

android {
    namespace = "lucas.cordeiro.community.shared.network"
}

dependencies {
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.contentnegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)
}
