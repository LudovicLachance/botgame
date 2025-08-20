buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
    }
}

subprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins.withType<JavaPlugin> {
        dependencies {
            // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
            "implementation"(platform("org.apache.logging.log4j:log4j-bom:2.25.1"))
            "implementation"("org.apache.logging.log4j:log4j-api")
            "runtimeOnly"("org.apache.logging.log4j:log4j-core")
            "runtimeOnly"("org.apache.logging.log4j:log4j-layout-template-json")
            "runtimeOnly"("org.apache.logging.log4j:log4j-slf4j2-impl")

            // Use JUnit test framework.
            "testImplementation"(platform("org.junit:junit-bom:5.13.4"))
            "testImplementation"("org.junit.jupiter:junit-jupiter")
            "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
            "testImplementation"("org.junit.jupiter:junit-jupiter-params")

            // https://mvnrepository.com/artifact/org.assertj/assertj-core
            "testImplementation"("org.assertj:assertj-core:3.27.3")
        }
    }
}