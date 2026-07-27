import java.util.Properties

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

val properties = Properties()
if (rootProject.file("local.properties").exists()) {
    properties.load(rootProject.file("local.properties").inputStream())
}
android {
    namespace = "com.vcco.weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vcco.weather"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.vcco.weather.di.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BASE_URL", "\"${project.properties["base_url"]}\"")
        buildConfigField("String", "API_KEY", "\"${properties.get("api_key")}\"")
        buildConfigField("String", "PREFERENCE_FILE", "\"${properties.get("prefence_file")}\"")
        buildConfigField("String", "APP_DB_NAME", "\"${properties.get("app_db_name")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Android Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // UI
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // retrofit
    implementation(libs.retrofit)

    // Gson
    implementation(libs.gson)
    implementation(libs.gson.converter)

    // OkHTTP
    implementation(libs.okHttp)
    implementation(libs.okHttp.logging.interceptor)

    // RXJava
    implementation(libs.rxjava)
    implementation(libs.rxjava.adapter)
    implementation(libs.kotlinx.coroutines.rxjava)

    // RXAndroid
    implementation(libs.rxandroid)

    // Streams
    implementation(libs.androidx.lifecycle.reactive.streams)

    // Multidex
    implementation(libs.androidx.multidex)

    // Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.dagger.hilt.compilation)

    // Location
    implementation(libs.play.services.location)

    // Data Source
    implementation(libs.androidx.datasource.preferences)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.support.fragments)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // HiltTesting
    androidTestImplementation(libs.dagger.hilt.test)
    kaptAndroidTest(libs.dagger.hilt.compilation)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.android)
    testImplementation(libs.androidx.test)
    androidTestImplementation(libs.androidx.arch.core)

    // Selenium testing
    testImplementation(libs.apium.java.client)
    testImplementation(libs.selenium)
}

kapt {
    correctErrorTypes = true
}

ktlint {
    version = "1.4.0"
    enableExperimentalRules.set(true)
}
