/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.jdeprscan

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.gradle.testkit.runner.TaskOutcome
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Files
import java.nio.file.Path

object JdeprscanPluginSpec: Spek({

    given("apply org.mikeneck.gradle-jdeprscan-plugin") {
        val gradleProject = gradleProject()

        gradleProject.buildGradle = """
            plugins {
                id 'org.mikeneck.gradle-jdeprscan-plugin'
                id 'java'
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                compile 'junit:junit:4.12'
            }
        """.trimIndent()

        gradleProject.createJavaFile()

        on("calling jdeprscan task after compileJava") {
            val result = gradleProject.gradle("compileJava", "jdeprscan")
            it("report file will be created") {
                val reportFile = gradleProject.file("build/jdeprscan/report.txt")

                assertThat(Files.exists(reportFile), equalTo(true))
                assertThat(reportFile.contents, contains("class sample/Sample") and contains("java/util/Date::<init>"))
                assertThat(result.task(":jdeprscan")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

    given("apply org.mikenec.gradle-jdeprscan-plugin with Java9 home") {
        val gradleProject = gradleProject()

        gradleProject.buildGradle = """
            plugins {
                id 'org.mikeneck.gradle-jdeprscan-plugin'
                id 'java'
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                compile 'junit:junit:4.12'
            }
            jdeprscan {
                javaHome = '${JavaHome.java9}'
            }
        """.trimIndent()

        gradleProject.createJavaFile()

        on("calling jdeprscan task after compileJava") {
            val result = gradleProject.gradle("compileJava", "jdeprscan")
            it("will run jdeprscan task") {
                val reportFile = gradleProject.file("build/jdeprscan/report.txt")

                assertThat(Files.exists(reportFile), equalTo(true))
                assertThat(reportFile.contents, contains("class sample/Sample") and contains("java/util/Date::<init>"))
                assertThat(result.task(":jdeprscan")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

    given("apply org.mikeneck.gradle-jdeprscan-plugin with Java9 home") {
        val gradleProject = gradleProject()

        gradleProject.buildGradle = """
            plugins {
                id 'org.mikeneck.gradle-jdeprscan-plugin'
                id 'java'
            }
            repositories {
                mavenCentral()
            }
            dependencies {
                compile 'junit:junit:4.12'
            }
            jdeprscan {
                javaHome = '${JavaHome.java8}'
            }
        """.trimIndent()

        gradleProject.createJavaFile()

        on("calling jdeprscan task after compileJava") {
            val result = gradleProject.gradleWillFail("compileJava", "jdeprscan")
    
            it("will not run jdeprscan task because of no executable") {
                val reportFile = gradleProject.file("build/jdeprscan/report.txt")
    
                assertThat(Files.exists(reportFile), equalTo(false))
                assertThat(result.task(":jdeprscan")?.outcome, equalTo(TaskOutcome.FAILED))
            }
        }
    }
})

private fun Path.createJavaFile() {
    this.file("src/main/java/sample/Sample.java").contents = """
            package sample;
            import java.util.Date;
            import java.lang.reflect.Method;
            class Sample {
                void deprecatedConstructor() {
                    final Date date = new Date(117, 11, 4);
                    System.out.println(date);
                }
                void deprecatedReflection() throws Exception {
                    final Date date = Date.class.newInstance();
                    System.out.println(date);
                }
                void deprecatedPackageRef() throws Exception {
                    final ClassLoader loader = getClass().getClassLoader();
                    final Method method = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
                    final Package pkg = (Package)method.invoke(loader, "sample");
                    System.out.println(pkg);
                }
            }
        """.trimIndent()
}
