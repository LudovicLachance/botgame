plugins {
    id("java")
}

dependencies {
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    modularity.inferModulePath.set(true)
}
