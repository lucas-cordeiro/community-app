plugins {
    alias(libs.plugins.android.library)
}

apply(from = "$rootDir/common-android.gradle")

android {
    namespace = "lucas.cordeiro.community.shared.storage"
}

dependencies {
    implementation(libs.datastore.preferences)
}
