plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "lucas.cordeiro.community.shared.ui.test"
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
    api(libs.junit)
    api(libs.coroutines.test)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui.test.junit4)
}
