plugins {
    id("java")
    application
}

group = "org.ratatui"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass = "org.ratatui.Main"
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

// tasks.name<JavaExec>("run") {
//     standardInput = System.`in`
// }

tasks.register<Exec>("jextract") {
    commandLine = listOf(
        "../../jextract/jextract",
        "--include-dir", "../../rust/target/release",
        "--output", "src/main/java",
        "--target-package", "org.ratatui.bindings",
        "--library", "ratatui_jvm",
        "../../rust/bindings.h"
    )
}

tasks.withType<JavaCompile> {
    dependsOn("jextract")
}

tasks.withType<JavaExec> {
    args = listOf("--library", "ratatui_jvm")
    val os = System.getProperty("os.name").lowercase()

    val envVar = when {
        os.contains("mac") -> "DYLD_LIBRARY_PATH"
        os.contains("win") -> {
            val path = System.getenv("PATH")
            val newPath = "$path;../../rust/target/release"
            System.setProperty("PATH", newPath)
            ""
        }
        else -> "LD_LIBRARY_PATH"
    }

    environment(envVar, "../../rust/target/release")
}