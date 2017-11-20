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

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.mikeneck.jdeprscan.impl.JavaProject;

import java.io.File;
import java.io.IOException;

public class JdeprscanTask extends DefaultTask {

    static final String TASK_NAME = "jdeprscan";

    private JdeprscanExtension jdeprscanExtension;

    void setJdeprscanExtension(final JdeprscanExtension jdeprscanExtension) {
        this.jdeprscanExtension = jdeprscanExtension;
    }

    @SuppressWarnings("unused")
    @TaskAction
    public void runJdeprscan() {
        final LanguageProject thisProject = new JavaProject(getProject());

        final Jdeprscan jdeprscan = jdeprscanExtension.getJdeprscan();
        jdeprscan.validate();
        final JdeprscanParameter parameter = jdeprscanExtension.getOption()
                .withClasspath(thisProject.getClasspath())
                .withDestination(thisProject.getDestinationDirs());

        final JdeprscanResult result = jdeprscan.runCommand(parameter);

        try {
            result.writeTo(jdeprscanExtension.getReportFile().map(File::toPath).getOrElse(getProject().file(JdeprscanExtension.DEFAULT_REPORT_FILE).toPath()));
        } catch (IOException e) {
            throw new JdeprscanRuntimeException(e);
        }
    }
}
