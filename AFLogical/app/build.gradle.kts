plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.aflogical"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aflogical"
        minSdk = 24
        targetSdk = 35
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

    packaging {
        resources {
            // Solución definitiva para archivos duplicados
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/LICENSE.md",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE.md",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module",
                "META-INF/{AL2.0,LGPL2.1}",
                "**/attach_hotspot_windows.dll",
                "META-INF/versions/9/previous-compilation-data.bin"
            )
            merges += setOf("META-INF/services/javax.mail.*")
            pickFirsts += setOf(
                "META-INF/io.netty.versions.properties",
                "META-INF/INDEX.LIST"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.android.mail)
    implementation(libs.android.activation)

    implementation (libs.appcompat.v161)
    implementation (libs.material.v190)
    implementation (libs.constraintlayout.v214)

    // Para manejo de permisos más fácil (opcional)
    implementation (libs.dexter)

    androidTestImplementation (libs.junit.v115)
    androidTestImplementation (libs.espresso.core.v351)

    implementation (libs.core)
    implementation (libs.documentfile)
}