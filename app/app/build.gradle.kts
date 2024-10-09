plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.dagger.hilt.android")
    kotlin("kapt") version "1.9.22"
    id("com.google.gms.google-services")
}

android {
    namespace = "hu.bme.aut.android.cityofguilds"
    compileSdk = 34

    defaultConfig {
        applicationId = "hu.bme.aut.android.cityofguilds"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {


    implementation(libs.androidx.appcompat)

    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    implementation(libs.play.services.location)

    //Firebase
    val firebaseBom = platform("com.google.firebase:firebase-bom:32.8.1")
    implementation(firebaseBom)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")



    //Compose Bill of Materials
    val composeBom = platform("androidx.compose:compose-bom:2024.02.02")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite")



    //Kotlin Extensions DateTime - LocalDate
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    //Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Material Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // To use java.time lib
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    val hiltVersion = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Retrofit core library
    implementation(libs.retrofit.core)

    // Retrofit Gson converter for JSON deserialization
    implementation(libs.retrofit.gson)

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

// Hilt for testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
}