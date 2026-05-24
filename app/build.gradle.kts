plugins {
    alias(libs.plugins.android.application)
    id(libs.plugins.kover.get().pluginId)
}

apply(from = "$rootDir/common-android.gradle")

android {
    namespace = "lucas.cordeiro.community"
    compileSdk = 36

    defaultConfig {
        applicationId = "lucas.cordeiro.community"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(project(":app:shared:network"))
    implementation(project(":app:shared:storage"))
    implementation(project(":app:shared:ui"))

    implementation(project(":app:component:community"))

    implementation(project(":app:feature:community"))

    implementation(libs.utils.appstartup)

    testImplementation(project(":app:shared:ui:test"))
    androidTestImplementation(project(":app:shared:ui:test"))

    kover(project(":app:shared:core"))
    kover(project(":app:shared:network"))
    kover(project(":app:shared:storage"))
    kover(project(":app:shared:ui"))
    kover(project(":app:component:community"))
    kover(project(":app:feature:community"))
}

kover {
    reports {
        verify {
            rule {
                minBound(80)
            }
        }
    }
}
