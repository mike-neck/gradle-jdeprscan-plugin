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
package org.mikeneck.jdeprscan.impl;

import org.gradle.api.file.FileCollection;
import org.mikeneck.jdeprscan.CommandOption;

import java.util.ArrayList;
import java.util.List;

public class ClasspathOption implements CommandOption {

    private final FileCollection classpath;

    public ClasspathOption(final FileCollection classpath) {
        this.classpath = classpath;
    }

    @Override
    public List<String> getOption() {
        final List<String> options = new ArrayList<>();
        options.add("--class-path");
        options.add(classpath.getAsPath());
        return options;
    }
}
