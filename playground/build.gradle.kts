plugins {
    id("java")
    id("application")

    id("com.gradleup.shadow") version "9.0.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":game-library"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    modularity.inferModulePath.set(true)
}

application {
    mainClass.set("com.playground.GamingRoom")
}

tasks.jar {
    manifest {
        attributes["Multi-Release"] = true
        attributes["Main-Class"] = application.mainClass
    }
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    mergeServiceFiles()
}

tasks.test {
    useJUnitPlatform()
}