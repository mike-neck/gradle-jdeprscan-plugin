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
package org.mikeneck.jdeprscan;

import org.gradle.api.file.FileCollection;
import org.mikeneck.jdeprscan.impl.ClasspathOption;
import org.mikeneck.jdeprscan.impl.DestinationDirs;
import org.mikeneck.jdeprscan.impl.ListOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdeprscanParameterBuilder {

    private final List<CommandOption> options;

    private final boolean receiveOtherOptions;

    private JdeprscanParameterBuilder() {
        this.options = new ArrayList<>();
        this.receiveOtherOptions = true;
    }

    private JdeprscanParameterBuilder(final ListOption listOption) {
        this.options = new ArrayList<>();
        this.options.add(listOption);
        this.receiveOtherOptions = false;
    }

    static JdeprscanParameterBuilder listOption() {
        return new JdeprscanParameterBuilder(new ListOption());
    }

    static JdeprscanParameterBuilder defaultOption() {
        return new JdeprscanParameterBuilder();
    }

    WithDestination withClasspath(final FileCollection classpath) {
        if (receiveOtherOptions) {
            return destinationDirs -> {
                options.add(new ClasspathOption(classpath));
                options.add(new DestinationDirs(destinationDirs));
                return new JdeprscanParameter(Collections.unmodifiableList(options));
            };
        } else {
            return destinationDirs -> new JdeprscanParameter(Collections.unmodifiableList(options));
        }
    }

    interface WithDestination {

        JdeprscanParameter withDestination(final FileCollection destinationDirs);
    }
}

