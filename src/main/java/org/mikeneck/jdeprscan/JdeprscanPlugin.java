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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.mikeneck.jdeprscan.impl.DefaultJdeprscanExtension;
import org.mikeneck.jdeprscan.impl.JavaProject;

public class JdeprscanPlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull final Project project) {
        final JdeprscanExtension jdeprscanExtension = project.getExtensions().create(JdeprscanExtension.class, JdeprscanExtension.EXTENSION_NAME,
                DefaultJdeprscanExtension.class, project);

        project.getTasks().create(JdeprscanTask.TASK_NAME, JdeprscanTask.class, task -> {
            task.setJdeprscanExtension(jdeprscanExtension);
            task.setThisProject(new JavaProject(project));
            task.setGroup("Help");
            task.setDescription("Runs jdeprscan command detecting a uses of deprecated API.");
        });
    }
}
