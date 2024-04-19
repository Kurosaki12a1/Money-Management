plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "co.yml.charts"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.04.00")
    implementation(composeBom)

    implementation(libs.androidx.compose.ui)

    // Android Studio Preview support
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Foundational component
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.core.ktx)

    // Material
    implementation(libs.androidx.compose.material)

    // Material Design 3
    implementation(libs.androidx.compose.material3)

    // Optional - Integration with activities
    implementation(libs.androidx.compose.activity)
}