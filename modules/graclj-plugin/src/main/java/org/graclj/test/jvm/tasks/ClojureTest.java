package org.graclj.test.jvm.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.TaskAction;

public class ClojureTest extends DefaultTask  {
    private FileCollection classpath;

    @TaskAction
    public void runTests() {
        getProject().javaexec(spec -> {
            spec.classpath(getClasspath());
            spec.setMain("clojure.main");
            spec.args("--main", "org.graclj.tools.test.clojure-test", "");
        });
    }

    @InputFiles
    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }
}
