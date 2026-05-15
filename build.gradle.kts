import java.io.File
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    kotlin("multiplatform") version "2.4.0-RC"
}

repositories {
    mavenCentral()
}

group = "org.bljw"
version = "0.1.0-SNAPSHOT"

val raylibUpstreamDir = layout.projectDirectory.dir("third_party/raylib")
val mingwGccExecutable = File("/usr/bin/x86_64-w64-mingw32-gcc")
val mingwGppExecutable = File("/usr/bin/x86_64-w64-mingw32-g++")

val hostIsMacOs = System.getProperty("os.name").lowercase().contains("mac")

val cmakeParallelJobs = Runtime.getRuntime().availableProcessors().toString()

val linuxRaylibOut = layout.buildDirectory.dir("third-party-raylib/linuxX64").get().asFile
val macosRaylibOut = layout.buildDirectory.dir("third-party-raylib/macosArm64").get().asFile
val mingwRaylibOut = layout.buildDirectory.dir("third-party-raylib/mingwX64").get().asFile

val linuxRaylibA = layout.buildDirectory.file("third-party-raylib/linuxX64/raylib/libraylib.a")
val macosRaylibA = layout.buildDirectory.file("third-party-raylib/macosArm64/raylib/libraylib.a")
val mingwRaylibA = layout.buildDirectory.file("third-party-raylib/mingwX64/raylib/libraylib.a")

val linuxHostLibDirs =
    sequenceOf(File("/usr/lib"), File("/usr/lib64"), File("/usr/lib/x86_64-linux-gnu"))
        .filter { it.isDirectory && it.canRead() }
        .distinct()

tasks.register<Exec>("configureRaylibLinuxX64") {
    inputs.dir(raylibUpstreamDir.dir("src"))
    inputs.file(raylibUpstreamDir.file("CMakeLists.txt"))
    outputs.file(File(linuxRaylibOut, "CMakeCache.txt"))
    commandLine(
        "cmake",
        "-S",
        raylibUpstreamDir.asFile.absolutePath,
        "-B",
        linuxRaylibOut.absolutePath,
        "-DCMAKE_BUILD_TYPE=Release",
        "-DBUILD_SHARED_LIBS=OFF",
        "-DBUILD_EXAMPLES=OFF",
    )
}

tasks.register<Exec>("compileRaylibLinuxX64") {
    dependsOn("configureRaylibLinuxX64")
    inputs.dir(raylibUpstreamDir.dir("src"))
    inputs.file(File(linuxRaylibOut, "CMakeCache.txt"))
    outputs.file(linuxRaylibA.get().asFile)
    commandLine(
        "cmake",
        "--build",
        linuxRaylibOut.absolutePath,
        "--parallel",
        cmakeParallelJobs,
    )
}

tasks.register("buildRaylibLinuxX64") {
    dependsOn("compileRaylibLinuxX64")
}

tasks.register<Exec>("configureRaylibMacosArm64") {
    onlyIf { hostIsMacOs }

    inputs.dir(raylibUpstreamDir.dir("src"))
    inputs.file(raylibUpstreamDir.file("CMakeLists.txt"))
    outputs.file(File(macosRaylibOut, "CMakeCache.txt"))
    commandLine(
        "cmake",
        "-S",
        raylibUpstreamDir.asFile.absolutePath,
        "-B",
        macosRaylibOut.absolutePath,
        "-DCMAKE_BUILD_TYPE=Release",
        "-DBUILD_SHARED_LIBS=OFF",
        "-DBUILD_EXAMPLES=OFF",
    )
}

tasks.register<Exec>("compileRaylibMacosArm64") {
    onlyIf { hostIsMacOs }

    dependsOn("configureRaylibMacosArm64")
    outputs.file(macosRaylibA.get().asFile)
    commandLine(
        "cmake",
        "--build",
        macosRaylibOut.absolutePath,
        "--parallel",
        cmakeParallelJobs,
    )
}

tasks.register("buildRaylibMacosArm64") {
    dependsOn("compileRaylibMacosArm64")
}

tasks.register<Exec>("configureRaylibMingwX64") {
    onlyIf { mingwGccExecutable.exists() }

    val cxx =
        mingwGppExecutable.absolutePath.takeIf { mingwGppExecutable.exists() }
            ?: mingwGccExecutable.absolutePath

    inputs.dir(raylibUpstreamDir.dir("src"))
    inputs.file(raylibUpstreamDir.file("CMakeLists.txt"))
    outputs.file(File(mingwRaylibOut, "CMakeCache.txt"))
    commandLine(
        "cmake",
        "-S",
        raylibUpstreamDir.asFile.absolutePath,
        "-B",
        mingwRaylibOut.absolutePath,
        "-DCMAKE_BUILD_TYPE=Release",
        "-DBUILD_SHARED_LIBS=OFF",
        "-DBUILD_EXAMPLES=OFF",
        "-DCMAKE_C_COMPILER=${mingwGccExecutable.absolutePath}",
        "-DCMAKE_CXX_COMPILER=$cxx",
    )
}

tasks.register<Exec>("compileRaylibMingwX64") {
    onlyIf { mingwGccExecutable.exists() }

    dependsOn("configureRaylibMingwX64")
    outputs.file(mingwRaylibA.get().asFile)
    commandLine(
        "cmake",
        "--build",
        mingwRaylibOut.absolutePath,
        "--parallel",
        cmakeParallelJobs,
    )
}

tasks.register("buildRaylibMingwX64") {
    dependsOn("compileRaylibMingwX64")
}

tasks.withType<KotlinNativeLink>().configureEach {
    when {
        name.contains("LinuxX64") ->
            dependsOn(tasks.named("compileRaylibLinuxX64"))

        name.contains("MacosArm64") && hostIsMacOs ->
            dependsOn(tasks.named("compileRaylibMacosArm64"))

        name.contains("MingwX64") && mingwGccExecutable.exists() ->
            dependsOn(tasks.named("compileRaylibMingwX64"))
    }

    if (name.contains("MacosArm64") && !hostIsMacOs) {
        enabled = false
    }

    if (name.contains("MingwX64") && !mingwGccExecutable.exists()) {
        enabled = false
    }
}

kotlin {
    linuxX64 {
        binaries.executable {
            entryPoint = "main"
            linkerOpts +=
                buildList {
                    add(linuxRaylibA.get().asFile.absolutePath)
                    linuxHostLibDirs.forEach { add("-L${it.absolutePath}") }
                    addAll(
                        listOf(
                            "-lGL",
                            "-lm",
                            "-lpthread",
                            "-ldl",
                            "-lrt",
                            "-lX11",
                        ),
                    )
                }
        }
    }
    macosArm64 {
        binaries.executable {
            entryPoint = "main"
            linkerOpts +=
                listOf(
                    macosRaylibA.get().asFile.absolutePath,
                    "-framework",
                    "Cocoa",
                    "-framework",
                    "OpenGL",
                    "-framework",
                    "IOKit",
                    "-framework",
                    "CoreVideo",
                )
        }
    }
    mingwX64 {
        binaries.executable {
            entryPoint = "main"
            linkerOpts +=
                listOf(
                    mingwRaylibA.get().asFile.absolutePath,
                    "-static-libgcc",
                    "-lwinmm",
                    "-lgdi32",
                    "-lopengl32",
                )
        }
    }

    sourceSets {
        val commonMain by getting
        val rlCore by creating
        rlCore.dependsOn(commonMain)

        linuxX64Main.configure {
            dependsOn(rlCore)
        }
        macosArm64Main.configure {
            dependsOn(rlCore)
        }
        mingwX64Main.configure {
            dependsOn(rlCore)
        }
    }
}

kotlin.targets.withType<KotlinNativeTarget>().configureEach {
    val nc = compilations.getByName("main") as KotlinNativeCompilation
    nc.cinterops.create("raylib") {
        defFile(layout.projectDirectory.file("native-defs/raylib.def"))
        packageName("rl")
        compilerOpts("-I${raylibUpstreamDir.dir("src").asFile.absolutePath}")
    }
}
