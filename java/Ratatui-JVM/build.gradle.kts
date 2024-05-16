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
        "--include-dir", "../../rust/dist/target/release",
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
    val os = System.getProperty("os.name").toLowerCase()
    if (os.contains("mac")) {
        environment("DYLD_LIBRARY_PATH", "../../rust/target/release")
    } else if (os.contains("win")) {
        environment("PATH", "../../rust/target/release")
    } else {
        environment("LD_LIBRARY_PATH", "../../rust/target/release")
    }
}