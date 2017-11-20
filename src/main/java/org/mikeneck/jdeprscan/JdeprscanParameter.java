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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdeprscanParameter {

    private final List<CommandOption> options;

    public JdeprscanParameter(final List<CommandOption> options) {
        this.options = options;
    }

    List<String> createCommand(final Path executable) {
        final List<String> command = new ArrayList<>();
        command.add(executable.toAbsolutePath().toString());
        options.stream()
                .map(CommandOption::getOption)
                .flatMap(List::stream)
                .forEach(command::add);
        return Collections.unmodifiableList(command);
    }
}
