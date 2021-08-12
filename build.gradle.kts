plugins {
    kotlin("multiplatform") version "1.5.30-RC"
    id("com.android.library")
}

group = "co.touchlab"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
}

kotlin {
    android()
    iosX64("ios") {
        binaries {
            framework {
                baseName = "library"
            }
        }
        compilations.all {
            kotlinOptions.freeCompilerArgs += listOf("-memory-model", "experimental")
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-new-mm-dev1")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
    }
}
