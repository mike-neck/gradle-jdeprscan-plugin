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

import org.zeroturnaround.exec.ProcessResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JdeprscanResult {

    private final ProcessResult result;

    public JdeprscanResult(final ProcessResult result) {
        this.result = result;
    }

    void writeTo(final Path report) throws IOException {
        final Path directory = report.getParent();
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        Files.write(report, result.getOutput().getLinesAsUTF8());
    }
}
