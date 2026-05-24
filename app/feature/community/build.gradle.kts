plugins {
    alias(libs.plugins.android.library)
}

apply(from = "$rootDir/common-feature.gradle")

android {
    namespace = "lucas.cordeiro.community.feature.community"
}

dependencies {
    implementation(project(":app:component:community"))
    implementation(libs.flagkit)
    implementation(libs.icons.lucide)
}
