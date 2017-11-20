plugins {
    id("java-library")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version("0.9.9")
    id("org.mikeneck.junit.starter.spek") version("1.1.5-1.1.60")
}

group = "org.mikeneck.jdeprscan"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    api(gradleApi())
    implementation("org.zeroturnaround:zt-exec:1.10")
    implementation("org.jetbrains:annotations:13.0")
    testImplementation(gradleTestKit())
    testImplementation(kotlin(module = "stdlib-jre8", version = "1.1.60"))
    testImplementation(kotlin(module = "reflect", version = "1.1.60"))
}

tasks {
    "junitPlatformTest" {
        dependsOn("pluginUnderTestMetadata")
    }
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }

    val compileJava = "compileJava"(JavaCompile::class)

    "showClasses" {
        dependsOn(compileJava)
        doLast {
            println(compileJava.classpath.asPath)
            println(files(compileJava).files)
        }
    }

    "showSourceSets" {
        doLast {
            println("source sets")
            project.container(SourceSetContainer::class.java).names.forEach { println(it) }

            println("destination")
            println(compileJava.destinationDir)
        }
    }
}
