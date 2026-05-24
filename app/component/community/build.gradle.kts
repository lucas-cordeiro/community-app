plugins {
    alias(libs.plugins.android.library)
}

apply(from = "$rootDir/common-component.gradle")

android {
    namespace = "lucas.cordeiro.community.component.community"
}
