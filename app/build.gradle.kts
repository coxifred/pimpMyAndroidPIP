plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.coxifred.pimmyandroidpip"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.coxifred.pimmyandroidpip"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.nanohttpd)
    implementation(libs.androidx.cardview)
    implementation(libs.gson)
    implementation(libs.androidx.constraintlayout)
}