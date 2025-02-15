import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.secrets)
    alias(libs.plugins.google.services)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.dnd.safety"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dnd.safety"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders += mapOf(
            "KAKAO_NATIVE_APP_KEY" to properties.getProperty("KAKAO_NATIVE_APP_KEY"),
            "GOOGLE_MAPS_API_KEY" to properties.getProperty("GOOGLE_MAPS_API_KEY"),
            "GOOGLE_CLIENT_ID" to properties.getProperty("GOOGLE_CLIENT_ID"),
        )

        buildConfigField(
            "String",
            "KAKAO_REST_API_KEY",
            "\"${properties.getProperty("KAKAO_REST_API_KEY")}\""
        )
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${properties.getProperty("KAKAO_NATIVE_APP_KEY")}\""
        )
        buildConfigField(
            "String",
            "GOOGLE_MAPS_API_KEY",
            "\"${properties.getProperty("GOOGLE_MAPS_API_KEY")}\""
        )
        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            "\"${properties.getProperty("GOOGLE_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "ADDRESS_SEARCH_KEY",
            "\"${properties.getProperty("ADDRESS_SEARCH_KEY")}\""
        )
        buildConfigField(
            "String",
            "POINT_SEARCH_KEY",
            "\"${properties.getProperty("POINT_SEARCH_KEY")}\""
        )
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
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    implementation(libs.firebase.crashlytics.buildtools)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Sandwich
    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)

    // FlowLayout
    implementation(libs.accompanist.flowlayout)

    // Landscapist
    implementation(libs.landscapist.coil)

    // KakaoTalk
    implementation(libs.v2.all)

    // Google Auth
    implementation(libs.play.services.auth)

    // Google Mapsx
    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)

    // Paging3
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.video)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.material.icon)

    //Google Services & Maps
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.places)

    //Accompanist (Permission)
    implementation(libs.accompanist.permissions)

    implementation(libs.landscapist.coil)

    implementation(libs.proj4j)
    implementation(libs.proj4j.epsg)

    implementation(libs.kotlinx.immutable)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
}