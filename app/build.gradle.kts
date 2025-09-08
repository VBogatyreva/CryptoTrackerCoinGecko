plugins {
    id("com.android.application") version "8.7.3"
    id("org.jetbrains.kotlin.android") version "1.9.22"
    id("kotlin-kapt")
}

android {
    namespace = "ru.netology.cryptotrackercoingecko"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.netology.cryptotrackercoingecko"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Базовые зависимости
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.androidx.activity)

    // Тестирование
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:4.11.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Kotlin & Coroutines
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Сетевые запросы Retrofit & Gson (для работы с API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // AndroidX Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.1")

    // Fragment
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")

    // UI  RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // База данных Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // WorkManager (для фоновых задач)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    kapt ("com.github.bumptech.glide:compiler:4.16.0")
}