plugins {
    alias(libs.plugins.android.library)
}

apply(from = "$rootDir/common-android.gradle")

android {
    namespace = "lucas.cordeiro.community.shared.ui"
}

dependencies {
    api(libs.icons.lucide)
}
