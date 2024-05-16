import org.apache.tools.ant.taskdefs.condition.Os

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

// tasks.named<JavaExec>("run") {
//     standardInput = System.`in`
// }

tasks.register<Exec>("jextract") {
    val ext = if (Os.isFamily(Os.FAMILY_WINDOWS)) ".bat" else ""

    commandLine = listOf(
        "../../jextract/jextract$ext",
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
    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            val dllPath = "rust\\target\\release"
            val currentDir = System.getProperty("user.dir").split("\\").dropLast(2).joinToString("\\")
            val fullPath = "$currentDir\\$dllPath"
            val path = System.getenv("PATH")
            environment("PATH", "$path;$fullPath")
        }
        else -> environment("LD_LIBRARY_PATH", "../../rust/target/release")
    }
}