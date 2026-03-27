import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}
android {

    signingConfigs {
        getByName("debug") {
            storeFile = file("signKey")
            storePassword = "Mercedes2018"
            keyAlias = "keyMbt"
            keyPassword = "Mercedes2018"
        }
        create("enterprise") {
            storePassword = "Mercedes2018"
            keyAlias = "keyMbt"
            keyPassword = "Mercedes2018"
            storeFile = file("signKey")
        }
    }
    compileSdk = 34

    namespace = "com.daimlertruck.dtag.internal.android.mbt"


    defaultConfig {
        minSdk = 24
        targetSdk = 33
        versionCode = 45
        versionName = "1.4.0"
        dataBinding.enable = true
        multiDexEnabled = false

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("enterprise")
    }

    flavorDimensions += "env"

    productFlavors {
        create("dev") {
            dimension = "env"
            applicationId = "com.daimlertruck.dtag.internal.android.mbt.test2"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "MBT APP DEV")
            manifestPlaceholders["usesCleartextTraffic"] = "true"

            // Android emulator localhost mapping for the backend running on host machine.
            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://10.0.2.2:8000/api/v1/\""
            )
            buildConfigField("boolean", "OIDC_BYPASS_ENABLED", "true")
            buildConfigField(
                "String",
                "OIDC_BYPASS_TOKEN",
                "\"mbtbiziz-local-dev-bypass-token\""
            )
            buildConfigField("String", "OIDC_SCOPE", "\"\"")
        }

        create("staging") {
            dimension = "env"
            applicationId = "com.daimlertruck.dtag.internal.android.mbt.test2"
            resValue("string", "app_name", "MBT APP STAGING")
            manifestPlaceholders["usesCleartextTraffic"] = "false"
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://biziapp-test.app.daimlertruck.com/bizizBackend/public/index.php/api/v1/\""
            )
            buildConfigField("boolean", "OIDC_BYPASS_ENABLED", "false")
            buildConfigField("String", "OIDC_BYPASS_TOKEN", "\"\"")
            buildConfigField(
                "String",
                "OIDC_SCOPE",
                "\"api://910155c2-0cc9-4d21-a48b-4c49c99f8128/Read\""
            )
        }

        create("prod") {
            dimension = "env"
            applicationId = "com.daimlertruck.dtag.internal.android.mbt.test"
            resValue("string", "app_name", "MBT APP")
            manifestPlaceholders["usesCleartextTraffic"] = "false"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://bizizapp.com/bizizBackend/public/index.php/api/v1/\""
            )
            buildConfigField("boolean", "OIDC_BYPASS_ENABLED", "false")
            buildConfigField("String", "OIDC_BYPASS_TOKEN", "\"\"")
            buildConfigField(
                "String",
                "OIDC_SCOPE",
                "\"api://48252d22-0987-4d84-b1d9-00468ec9d424/Read\""
            )
        }
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("enterprise")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //Support libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")


    //Network
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")
    implementation("com.squareup.okhttp3:okhttp:3.9.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:3.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.3.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("com.google.code.gson:gson:2.7")
    implementation("com.fatboyindustrial.gson-jodatime-serialisers:gson-jodatime-serialisers:1.2.0")

    //Dagger
    implementation("com.google.dagger:dagger-android:2.14.1")
    implementation("com.google.dagger:dagger-android-support:2.14.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    annotationProcessor("com.google.dagger:dagger-android-processor:2.14.1")
    annotationProcessor("com.google.dagger:dagger-compiler:2.14.1")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.6.2")

    //Gradle
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //Google
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics")   // firebase-core yerine
    implementation("com.google.firebase:firebase-messaging")

    annotationProcessor("androidx.databinding:databinding-compiler:8.2.0")

    //Slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("com.github.chrisbanes:PhotoView:2.0.0")

    implementation("com.microsoft.identity.client:msal:5.6.0") { exclude(group = "com.microsoft.device.display") }
    implementation("com.android.volley:volley:1.2.1")
}

apply(plugin = "com.google.gms.google-services")

