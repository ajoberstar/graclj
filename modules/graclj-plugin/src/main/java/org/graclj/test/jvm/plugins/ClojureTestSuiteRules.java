package org.graclj.test.jvm.plugins;

import org.graclj.internal.GracljInternal;
import org.gradle.api.Task;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.test.JUnitTestSuiteBinarySpec;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinaryTasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ClojureTestSuiteRules extends RuleSource {
    @BinaryTasks
    public void copyGracljTools(ModelMap<Task> tasks, JUnitTestSuiteBinarySpec binary, GracljInternal internal) {
        Test testTask = binary.getTasks().getRun();
        tasks.create(binary.getName() + "GracljTools", Copy.class, task -> {
            testTask.dependsOn(task);
            File tools = internal.resolve("org.graclj:graclj-tools:0.1.0-SNAPSHOT@jar").getSingleFile();
            task.from(task.getProject().zipTree(tools));
            task.into(binary.getClassesDir());
        });
        testTask.setClasspath(testTask.getClasspath().plus(internal.resolve("org.graclj:graclj-tools:0.1.0-SNAPSHOT")));
    }
}
