import com.gradle.publish.PluginBundleExtension
import com.gradle.publish.PluginConfig
import groovy.lang.Closure

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
    testImplementation("com.natpryce:hamkrest:1.4.2.2")
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
}

pluginBundle {
    website = "https://github.com/mike-neck/gradle-jdeprscan-plugin"
    vcsUrl = "https://github.com/mike-neck/gradle-jdeprscan-plugin"
    description = "Gradle plugin that scans a use of deprecated APIs in classes built by a project using `jdeprscan` command, which is introduced since jdk9."
    tags = listOf("inspection", "code quality")
    version = project.version

    plugins {
        this.create("jdeprscanPlugin") {
            id = "org.mikeneck.gradle-jdeprscan-plugin"
            displayName = "Gradle jdeprscan plugin"
        }
    }
}

fun <D> D.closure(f: D.() -> Unit): Closure<Unit> = object: Closure<Unit>(this) {
    fun doCall() = this@closure.f()
}

fun PluginBundleExtension.plugins(configuration: NamedDomainObjectContainer<PluginConfig>.() -> Unit) = this.plugins((this.plugins as NamedDomainObjectContainer<PluginConfig>).closure(configuration))
