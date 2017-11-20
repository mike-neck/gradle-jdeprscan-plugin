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

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

public class Jdeprscan {

    private final Path path;

    private Jdeprscan(final Path path) {
        this.path = path;
    }

    void validate() throws JdeprscanNotFoundException {
        if (!Files.exists(path)) {
            throw new JdeprscanNotFoundException(path);
        }
        if (!Files.isExecutable(path)) {
            throw new JdeprscanNotFoundException(path);
        }
    }

    static Jdeprscan fromJavaHome(final File javaHome) throws JdeprscanNotFoundException {
        if (javaHome == null) {
            throw new JdeprscanNotFoundException();
        }
        final Path home = javaHome.toPath();
        final Path jdeprscan = home.resolve("bin").resolve("jdeprscan");
        return new Jdeprscan(jdeprscan);
    }

    JdeprscanResult runCommand(final JdeprscanParameter option) throws JdeprscanRuntimeException {
        try {
            final ProcessResult result = new ProcessExecutor()
                    .command(option.createCommand(path))
                    .readOutput(true)
                    .execute();
            return new JdeprscanResult(result);
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new JdeprscanRuntimeException(e);
        }
    }
}
