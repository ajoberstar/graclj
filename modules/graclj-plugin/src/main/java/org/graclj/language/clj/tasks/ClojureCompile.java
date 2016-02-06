package org.graclj.language.clj.tasks;

import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class ClojureCompile extends SourceTask {
    private FileCollection compiler;
    private FileCollection classpath;
    private File destinationDir;

    @TaskAction
    public void compile() {
        getProject().copy(spec -> {
            spec.from(getSource());
            spec.into(getTemporaryDir());
        });
        getDestinationDir().mkdirs();
        getProject().javaexec(spec -> {
            spec.classpath(getCompiler());
            spec.classpath(getClasspath());
            spec.classpath(getTemporaryDir());
            spec.classpath(getDestinationDir());

            spec.setMain("org.graclj.tools.compiler.clojure");

            // Location of source
            spec.args(getTemporaryDir().getAbsolutePath());

            // Location to write class files to
            spec.args(getDestinationDir().getAbsolutePath());
        });
    }

    public FileCollection getCompiler() {
        return compiler;
    }

    public void setCompiler(FileCollection compiler) {
        this.compiler = compiler;
    }

    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

    public File getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }
}
