plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dentify.dentifycare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dentify.dentifycare"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Core libraries to support Android development
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Material Design components for improving user interface elements
    implementation(libs.material)

    // Library for creating layouts using ConstraintLayout
    implementation(libs.androidx.constraintlayout)

    // Lifecycle components for managing reactive data and lifecycle awareness
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation components for managing navigation between fragments with Jetpack
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)

    // Circle ImageView for displaying circular images
    implementation(libs.circleimageview)

    // Rotate Image
    implementation(libs.androidx.exifinterface)

    // Glide for image loading and caching
    implementation(libs.glide)

    // Retrofit for making network requests
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Firebase for Google services
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Firebase Auth
    implementation(libs.firebase.auth)

    // Firebase Cloud Firestore
    implementation(libs.firebase.firestore)

    // Unit testing library for testing application logic
    testImplementation(libs.junit)

    // Android Instrumented Testing libraries for UI testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}