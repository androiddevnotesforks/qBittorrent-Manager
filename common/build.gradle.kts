@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("dev.yashgarg.qbit.kotlin-android")
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.yashgarg.qbit.common"
    compileSdk = 33

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint { baseline = file("lint-baseline.xml") }
}

dependencies {
    api(projects.clientWrapper.models)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
}
