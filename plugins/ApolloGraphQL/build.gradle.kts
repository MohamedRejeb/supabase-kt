plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "io.github.jan-tennert.supabase"
version = Versions.SUPABASEKT
description = "Extends supabase-kt with a Apollo GraphQL Client"

repositories {
    mavenCentral()
}

kotlin {
    /** Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    jvm {
        jvmToolchain(11)
        compilations.all {
            kotlinOptions.freeCompilerArgs = listOf(
                "-Xjvm-default=all",  // use default methods in interfaces,
                "-Xlambdas=indy"      // use invokedynamic lambdas instead of synthetic classes
            )
        }
    }
    android {
        publishLibraryVariants("release", "debug")
    }
    js(IR) {
        browser {
            testTask {
                enabled = false
                /**useKarma {
                    useFirefox()
                }*/
            }
        }
    }
    //ios()
    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
        val commonMain by getting {
            dependencies {
                api(project(":"))
                api(project(":gotrue-kt"))
                api("com.apollographql.apollo3:apollo-runtime:${Versions.APOLLO_GRAPHQL}")
                // https://mvnrepository.com/artifact/io.ktor/ktor-server-core
            }
        }
        val commonTest by getting
        val jvmMain by getting
        val androidMain by getting
        val jsMain by getting
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}