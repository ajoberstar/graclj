package org.graclj.internal;

import org.gradle.jvm.Classpath;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskDependency;

public class BasicClasspath implements Classpath {
    private final FileCollection files;

    public BasicClasspath(FileCollection files) {
        this.files = files;
    }

    @Override
    public FileCollection getFiles() {
        return files;
    }

    @Override
    public TaskDependency getBuildDependencies() {
        return files.getBuildDependencies();
    }
}
