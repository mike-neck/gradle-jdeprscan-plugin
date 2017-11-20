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
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class JdeprscanTask extends DefaultTask {

    static final String TASK_NAME = "jdeprscan";

    private final Property<LanguageProject> thisProject = getProject().getObjects().property(LanguageProject.class);

    private final Property<JdeprscanExtension> jdeprscanExtension = getProject().getObjects().property(JdeprscanExtension.class);

    @SuppressWarnings("WeakerAccess")
    public void setThisProject(final LanguageProject thisProject) {
        this.thisProject.set(thisProject);
    }

    void setJdeprscanExtension(final JdeprscanExtension jdeprscanExtension) {
        this.jdeprscanExtension.set(jdeprscanExtension);
    }

    @SuppressWarnings("unused")
    @TaskAction
    public void runJdeprscan() {
        final JdeprscanExtension extension = jdeprscanExtension.get();
        final LanguageProject project = thisProject.get();
        
        final Jdeprscan jdeprscan = extension.getJdeprscan();
        final JdeprscanParameter parameter = extension.getOption()
                .withClasspath(project.getClasspath())
                .withDestination(project.getDestinationDirs());

        final JdeprscanResult result = jdeprscan.runCommand(parameter);

        try {
            result.writeTo(extension.getReportFileAsPath());
        } catch (IOException e) {
            throw new JdeprscanRuntimeException(e);
        }
    }
}
