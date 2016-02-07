package org.graclj.test.clj.plugins;

import org.graclj.internal.GracljInternal;
import org.gradle.api.Task;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.internal.JvmAssembly;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.jvm.test.JUnitTestSuiteBinarySpec;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinaryTasks;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClojureTestSuiteRules extends RuleSource {
    @BinaryTasks
    public void copyGracljTools(ModelMap<Task> tasks, JUnitTestSuiteBinarySpec binary, GracljInternal internal) {
        Test testTask = binary.getTasks().getRun();

        // Need to provide explicit list of directories to scan for test namespaces in. Not sure it should be in this rule.
        JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
        Stream<File> classDirs = assembly.getClassDirectories().stream();
        Stream<File> resourceDirs = assembly.getResourceDirectories().stream();
        String testDirs = Stream.concat(classDirs, resourceDirs)
            .map(File::getAbsolutePath)
            .collect(Collectors.joining(File.pathSeparator));
        testTask.systemProperty("clojure.test.dirs", testDirs);

        tasks.create(binary.getName() + "GracljTools", Copy.class, task -> {
            testTask.dependsOn(task);
            File tools = internal.resolve("org.graclj:graclj-tools:0.1.0-rc.1@jar").getSingleFile();
            task.from(task.getProject().zipTree(tools));
            task.into(binary.getClassesDir());
        });
        testTask.setClasspath(testTask.getClasspath().plus(internal.resolve("org.graclj:graclj-tools:0.1.0-rc.1")));
    }
}
