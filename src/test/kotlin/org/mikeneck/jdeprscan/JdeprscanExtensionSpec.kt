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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.internal.provider.AbstractProvider
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mikeneck.jdeprscan.impl.DefaultJdeprscanExtension
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.reflect.Proxy
import java.nio.file.Paths

object JdeprscanExtensionSpec: Spek({

    given("with default values") {
        val extension = DefaultJdeprscanExtension(JdeprscanExtensionSpecSupport.project())

        on("getJdeprscan") {
            val jdeprscan = extension.jdeprscan

            it("is bin directory under java.home system property") {
                assertThat(jdeprscan.path, equalTo(Paths.get(System.getProperty("java.home"), "bin", "jdeprscan")))
            }
        }

        on("getReportFileAsPath") {
            val reportFile = extension.reportFileAsPath

            it("is build/jdeprscan/report.txt") {
                assertThat(reportFile, equalTo(Paths.get("build/jdeprscan/report.txt")))
            }
        }

        on("getOption") {
            val builder = extension.option

            it("will receive other option") {
                assertThat(builder.receiveOtherOptions, equalTo(true))
            }
        }
    }
})

object JdeprscanExtensionSpecSupport {

    fun project(): Project = Proxy.newProxyInstance(JdeprscanExtensionSpec::class.java.classLoader, arrayOf(Project::class.java)) { _, method, args ->
        if (method == null) {
            throw IllegalStateException("method $method is null")
        }
        if (method.name == "getObjects") {
            return@newProxyInstance objectFactory
        }
        if (method.name == "file") {
            return@newProxyInstance File("${args[0]}")
        } else {
            throw UnsupportedOperationException("${method.name} is not supported by this proxy.")
        }
    } as Project

    val objectFactory: ObjectFactory = object : ObjectFactory {
        override fun <T : Any> property(valueType: Class<T>?): Property<T> =
                if (valueType == null) throw IllegalArgumentException("type should be non-null") else PropertyImpl(null, valueType)

        override fun <T : Named?> named(type: Class<T>?, name: String?): T = throw UnsupportedOperationException("not supported")

        override fun <T : Any?> newInstance(type: Class<out T>?, vararg parameters: Any?): T = throw UnsupportedOperationException("not supported")

        override fun <T : Any?> listProperty(elementType: Class<T>?): ListProperty<T> = throw UnsupportedOperationException("not supported")
    }

    class PropertyImpl<T>(private var value: Provider<out T>?, private val klass: Class<T>): AbstractProvider<T>(), Property<T> {

        override fun getType(): Class<T>? = klass

        override fun getOrNull(): T? = value?.orNull

        override fun set(provider: Provider<out T>?) {
            this.value = provider
        }

        override fun set(value: T?) {
            this.value = ProviderImpl(value, klass)
        }
    }

    class ProviderImpl<T>(private var value: T?, private val klass: Class<T>): AbstractProvider<T>() {

        override fun getType(): Class<T>? = klass

        override fun getOrNull(): T? = value
    }
}
