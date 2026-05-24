plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "lucas.cordeiro.community.shared.network.test"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    api(libs.ktor.core)
    api(libs.ktor.mock)
    implementation(libs.ktor.contentnegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.utils.serialization)
}
