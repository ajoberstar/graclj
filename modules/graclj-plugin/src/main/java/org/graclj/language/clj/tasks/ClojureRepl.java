package org.graclj.language.clj.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskAction;

public class ClojureRepl extends DefaultTask {
    private FileCollection classpath;

    @TaskAction
    public void start() {
        getProject().javaexec(spec -> {
            spec.classpath(classpath);
            spec.setMain("clojure.main");
            spec.systemProperty("clojure.server.repl", "{:port 5555 :accept clojure.core.server/repl}");
        });
    }

    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }
}
