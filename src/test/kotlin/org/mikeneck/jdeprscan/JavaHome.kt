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

import java.io.InputStreamReader
import java.io.Reader
import java.util.*

object JavaHome {

    private val javaHomeProperties = "java.home.properties"

    private val classLoader: ClassLoader = JavaHome::class.java.classLoader

    private val roadReader: (Reader) -> Properties = { r ->
        Properties().apply { load(r) }
    }

    private val properties: Properties by lazy {
        classLoader.getResourceAsStream(javaHomeProperties)
                .let { InputStreamReader(it, Charsets.UTF_8) }
                .use(roadReader)
    }

    private val java9Key: String = "java9.home"

    val java9: String = properties.getProperty(java9Key) ?: throw IllegalStateException("$javaHomeProperties file or '$java9Key' property is not set.")

    private val java8Key: String = "java8.home"

    val java8: String = properties.getProperty(java8Key) ?: throw IllegalStateException("$javaHomeProperties file or '$java8Key' property is not set.")
}
