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

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.collections.SimpleFileCollection;
import org.gradle.api.tasks.compile.JavaCompile;
import org.mikeneck.jdeprscan.LanguageProject;

import java.io.File;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class JavaProject implements LanguageProject {

    private final Project project;

    public JavaProject(final Project project) {
        this.project = project;
    }

    @Override
    public FileCollection getClasspath() {
        final Set<File> classpath = project.getTasks()
                .withType(JavaCompile.class)
                .stream()
                .flatMap(task -> task.getClasspath().getFiles().stream())
                .collect(toSet());

        project.getLogger().debug("jdeprscan with classpath: {}", classpath);

        return new SimpleFileCollection(classpath);
    }

    @Override
    public FileCollection getDestinationDirs() {
        final Set<File> destinationDirs = project.getTasks()
                .withType(JavaCompile.class)
                .stream()
                .map(JavaCompile::getDestinationDir)
                .collect(toSet());

        project.getLogger().debug("jdeprscan with destination dir: {}", destinationDirs);

        return new SimpleFileCollection(destinationDirs);
    }
}
