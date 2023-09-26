@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.firebase.appdistribution) apply false
}

// Workaround for "Expecting an expression" build error
println("")

val reportMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.xml")) // or "reports/detekt/merge.sarif"
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        toolVersion = "1.23.0"
        config.from(rootProject.files("config/detekt/detekt.yml"))
        baseline = file("${rootProject.projectDir}/config/detekt/baseline.xml")
        autoCorrect = true
        // The directories where detekt looks for source files.
        // Defaults to `files("src/main/java", "src/test/java", "src/main/kotlin", "src/test/kotlin")`.
        source.setFrom("src/main/java", "src/main/kotlin")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        finalizedBy(reportMerge)
    }

    reportMerge {
        input.from(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().map { it.xmlReportFile }) // or .sarifReportFile
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }

    tasks.register("copyGitPreCommitHook", Copy::class.java) {
        description = "Copies the git pre-commit.sh.sh hook from $detektPreCommitScript to the .git folder."
        group = "Git Hooks"
        from(detektPreCommitScript) {
            include("**/*.sh")
            rename("(.*).sh", "$1")
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        into("$rootDir/.git/hooks")
        onlyIf {
            isLinuxOrMacOs()
        }
    }

    tasks.register("installGitHooks", Exec::class.java) {
        description = "Installs the pre-commit.sh.sh git hooks from $detektPreCommitScript."
        group = "Git Hooks"
        workingDir(rootDir)
        commandLine("chmod")
        args("-R", "+x", ".git/hooks")
        dependsOn("copyGitPreCommitHook")
        onlyIf {
            isLinuxOrMacOs()
        }
        doLast {
            println("Git hook installed successfully.")
        }
    }

    tasks.getByPath("$path:assembleDebug").dependsOn("installGitHooks")

    dependencies {
        detektPlugins(libs.detekt.formatting)
        detektPlugins(libs.detekt.twitter.compose)
    }
}

tasks.register("runUITests") {
    val hasTestFiles = File(rootProject.projectDir.toString() + "/app/src/androidTest")
        .walkTopDown()
        .any { it.name.endsWith(".kt") }

    if (hasTestFiles) {
        println("UI tests found")
        dependsOn("pixel2api30DebugAndroidTest")
    } else {
        println("No UI tests found")
    }
}

fun isLinuxOrMacOs(): Boolean {
    val osName = System.getProperty("os.name").lowercase(java.util.Locale.ROOT)
    return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
}

val detektPreCommitScript: String
    get() = "$rootDir/scripts/pre-commit.sh"
