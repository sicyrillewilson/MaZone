// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.15.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    }

}
plugins{
    id("com.android.application") version "8.6.0" apply false
    id ("com.android.library") version "8.6.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("org.jetbrains.kotlin.kapt") version "1.8.20" apply false
}
