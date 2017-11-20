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
import org.gradle.api.provider.Property;
import org.mikeneck.jdeprscan.JdeprscanExtension;

import java.io.File;

public class DefaultJdeprscanExtension implements JdeprscanExtension {

    private final Project project;

    private final Property<Boolean> listOption;

    private final Property<File> javaHome;

    private final Property<File> reportFile;

    public DefaultJdeprscanExtension(final Project project) {
        this.project = project;
        this.listOption = project.getObjects().property(Boolean.class);
        this.listOption.set(JdeprscanExtension.LIST_OPTION);
        this.javaHome = project.getObjects().property(File.class);
        this.javaHome.set(new File(JdeprscanExtension.DEFAULT_JAVA_HOME));
        this.reportFile = project.getObjects().property(File.class);
        this.reportFile.set(project.file(JdeprscanExtension.DEFAULT_REPORT_FILE));
    }

    @Override
    public Property<Boolean> getListOption() {
        return listOption;
    }

    @Override
    public void setListOption(final boolean listOption) {
        this.listOption.set(listOption);
    }

    @Override
    public Property<File> getJavaHome() {
        return javaHome;
    }

    @Override
    public void setJavaHome(final String javaHome) {
        this.javaHome.set(project.file(javaHome));
    }

    @Override
    public void setJavaHome(final File javaHome) {
        this.javaHome.set(javaHome);
    }

    @Override
    public Property<File> getReportFile() {
        return reportFile;
    }

    @Override
    public void setReportFile(final File reportFile) {
        this.reportFile.set(reportFile);
    }

    @Override
    public void setReportFile(final String reportFile) {
        this.reportFile.set(project.file(reportFile));
    }
}
