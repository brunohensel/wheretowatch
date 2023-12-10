import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.anvil)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.sqldelight)
}

val apiKeyFile: File = rootProject.file("apikey.properties")
val apiKeyProperties = Properties()
apiKeyProperties.load(FileInputStream(apiKeyFile))

android {
    namespace = "dev.bruno.wheretowatch"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.bruno.wheretowatch"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(type = "String", name = "API_KEY", value = apiKeyProperties.getProperty("API_KEY"))
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sqldelight {
    databases {
        create("WhereToWatchDatabase")
    }
}

dependencies {

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.circuit.circuitx)
    implementation(libs.circuit.foundation)
    implementation(libs.circuit.codegen.annotations)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    ksp(libs.circuit.codegen.ksp)
    kapt(libs.dagger.apt.compiler)
    implementation(libs.dagger.runtime)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.serialization.json)
    implementation(libs.sqldelight.androidDriver)
    implementation(libs.sqldelight.primitiveAdapters)

    testImplementation(libs.test.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
