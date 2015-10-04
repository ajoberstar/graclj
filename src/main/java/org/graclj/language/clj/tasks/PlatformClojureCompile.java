package org.graclj.language.clj.tasks;

import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.*;

import java.io.File;

public class PlatformClojureCompile extends SourceTask {
    private File destinationDir;
    private FileCollection classpath;
    private String aotPattern;

    @OutputDirectory
    public File getDestinationDir() {
        return this.destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

    @InputFiles
    public FileCollection getClasspath() {
        return this.classpath;
    }

    public void setClasspath(FileCollection configuration) {
        this.classpath = configuration;
    }

    @Input
    public String getAotPattern() {
        return aotPattern;
    }

    public void setAotPattern(String aotPattern) {
        this.aotPattern = aotPattern;
    }

    @TaskAction
    public void compile() {
        // need to execute the compile instead of just copying

        getProject().copy(spec -> {
            spec.from(getSource());
            spec.into(getDestinationDir());
        });
    }
}
