import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.kuro.money"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kuro.money"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    val apikey: String by localProperties
    val apiKeyValue = localProperties.getProperty("API_KEY_EXCHANGE_RATES") ?: ""

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"$apiKeyValue\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_KEY", "\"$apiKeyValue\"")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    ksp {
        arg("room.generateKotlin", "true")
    }
}

dependencies {
    implementation(project(":customimagevector"))
    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material
    implementation(libs.androidx.compose.material)

    // Material Design 3
  //  implementation(libs.androidx.compose.material3)

    // Foundational component
    implementation(libs.androidx.compose.foundation)

    // main APIs for the underlying toolkit systems, such as input and measurement/layout
    implementation(libs.androidx.compose.ui)

    // Android Studio Preview support
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.kotlinx.metadata.jvm)

    implementation(libs.androidx.compose.material.icon.core)
    implementation(libs.androidx.compose.material.icon.extended)
    implementation(libs.androidx.compose.window.size)

    // Optional - Integration with activities
    implementation(libs.androidx.compose.activity)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.compose.viewmodel)
    // Navigation
    implementation(libs.androidx.compose.navigation)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.converter.gson)
    implementation(libs.okhttp3.interceptor)
    implementation(libs.okhttp3.okhttps)


    implementation("androidx.compose.animation:animation")

    implementation(libs.material)

    // Location
    implementation(libs.play.services.location)

    // Permission
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Coil load image from url
    implementation(libs.coil.compose)

    implementation("org.mozilla:rhino:1.7.13")

}