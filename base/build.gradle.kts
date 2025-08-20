import com.github.jengelman.gradle.plugins.shadow.transformers.PreserveFirstFoundResourceTransformer

plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.0.0"
}

dependencies {
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    modularity.inferModulePath.set(true)
}

application {
    // Define the main class for the application.
    mainClass = "org.example.app.App"
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    transform<PreserveFirstFoundResourceTransformer> {

    }
    mergeServiceFiles()
}
