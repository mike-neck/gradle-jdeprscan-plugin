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

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.contains
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.nio.file.Files
import java.nio.file.Path

var Path.contents: String 
    get() = Files.readAllLines(this).joinToString("\n")
    set(contents) = when (Files.exists(this.parent)) {
        true -> this.parent
        false -> Files.createDirectories(this.parent)
    }.let {
        Files.write(this, contents.toByteArray(Charsets.UTF_8)).unit
    }

var Path.buildGradle: String
    get() = Files.readAllLines(this.resolve("build.gradle")).joinToString("\n")
    set(text) = Files.write(this.resolve("build.gradle"), text.toByteArray(Charsets.UTF_8)).unit

fun Path.gradle(vararg commands: String): BuildResult = GradleRunner.create()
        .withProjectDir(this.toFile())
        .withPluginClasspath()
        .withArguments(*commands)
        .build()

@Suppress("unused")
val Any.unit: Unit get() = Unit

fun contains(pattern: String): Matcher<String> = contains(Regex(pattern))
