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

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import java.io.File;
import java.nio.file.Path;

public interface JdeprscanExtension {

    Project getProject();

    String EXTENSION_NAME = "jdeprscan";

    boolean LIST_OPTION = false;

    Property<Boolean> getListOption();

    default boolean isListOption() {
        return getListOption().getOrElse(LIST_OPTION);
    }

    void setListOption(final boolean listOption);

    default JdeprscanParameterBuilder getOption() {
        if (isListOption()) {
            return JdeprscanParameterBuilder.listOption();
        } else {
            return JdeprscanParameterBuilder.defaultOption();
        }
    }

    String DEFAULT_JAVA_HOME = System.getProperty("java.home");

    Property<File> getJavaHome();

    default Jdeprscan getJdeprscan() throws JdeprscanNotFoundException {
        final File javaHome = getJavaHome().getOrNull();
        final Jdeprscan jdeprscan = Jdeprscan.fromJavaHome(javaHome);
        jdeprscan.validate();
        return jdeprscan;
    }

    void setJavaHome(final String java9Home);

    void setJavaHome(final File javaHome);

    String DEFAULT_REPORT_FILE = "build/jdeprscan/report.txt";

    Property<File> getReportFile();

    default Path getReportFileAsPath() {
        return getReportFile().map(File::toPath).getOrElse(getProject().file(DEFAULT_REPORT_FILE).toPath());
    }

    void setReportFile(final File reportFile);

    void setReportFile(final String reportFile);
}
