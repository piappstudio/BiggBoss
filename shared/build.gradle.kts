import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id ("dev.icerock.mobile.multiplatform-resources")
    kotlin("plugin.serialization") version "1.9.0"

}

kotlin {
    androidTarget()

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                // For material3
                implementation(compose.material3)
                // for icons
                implementation(compose.materialIconsExtended)
                implementation(libs.serialization.json)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                // For DI
                implementation(libs.koin.compose)

                // For networking
                implementation(libs.bundles.ktor)

                // For navigation
                implementation( libs.bundles.voyager)

                // For async image loading
                implementation(libs.kamel.image)

                // For logging
                implementation(libs.napier.log)
                implementation(libs.kermit.log)

                implementation (libs.chart)

                implementation(libs.kotlinx.datetime)
                implementation(libs.google.code.gson )


            }
        }
        val  androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
                implementation(libs.compose.preview)
                api("androidx.core:core-ktx:1.12.0")
                implementation(libs.ktor.android)

                implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
                implementation("com.google.firebase:firebase-analytics-ktx")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.ios)

            }
        }
        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.compose.preview)
                implementation(compose.desktop.common)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.desktop)
                implementation ("org.slf4j:slf4j-log4j12:1.7.29")

            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.piappstudio.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
dependencies {
    implementation(libs.material3)
    commonMainApi(libs.bundles.moko.resources)
    testImplementation(libs.junit)


}
multiplatformResources {
    multiplatformResourcesPackage = "com.biggboss.shared" // required
}