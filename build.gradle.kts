// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.anvil) apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.sqldelight) apply false
}

detekt {
    source.setFrom(
        files(
            fileTree(
                "dir" to projectDir,
                "includes" to listOf("**/*.kt"),
                "excludes" to listOf("**/build/**", "**/resources/**")
            )
        )
    )
    config.setFrom("detekt/config.yml")
    baseline = file("detekt/baseline.xml")
    autoCorrect = true
    basePath = projectDir.absolutePath
}

subprojects {
    pluginManager.withPlugin("com.squareup.anvil") {
        dependencies { add("compileOnly", libs.anvil.annotations) }
    }
}
