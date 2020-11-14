plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
buildscript {
    val kotlin_version by extra("1.4.0")
    dependencies {
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}
