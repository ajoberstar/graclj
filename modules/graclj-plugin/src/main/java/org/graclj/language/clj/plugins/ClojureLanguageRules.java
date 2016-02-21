package org.graclj.language.clj.plugins;

import org.graclj.internal.GracljInternal;
import org.graclj.language.clj.ClojureSourceSet;
import org.graclj.language.clj.tasks.ClojureCompile;
import org.graclj.platform.clj.ClojureGeneralComponentSpec;
import org.gradle.api.Task;
import org.gradle.jvm.JarBinarySpec;
import org.gradle.jvm.JvmBinarySpec;
import org.gradle.jvm.JvmLibrarySpec;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.jvm.test.JvmTestSuiteSpec;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.Defaults;
import org.gradle.model.Each;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinaryTasks;
import org.gradle.platform.base.ComponentType;
import org.gradle.platform.base.TypeBuilder;

public class ClojureLanguageRules extends RuleSource {
    @ComponentType
    public void registerLanguage(TypeBuilder<ClojureSourceSet> builder) {
        // using managed type
    }

    // TODO generalize to JvmComponentSpec when handles getSources
    @Defaults
    public void addLibrarySourceSets(@Each JvmLibrarySpec component) {
        component.getSources().create("clojure", ClojureSourceSet.class);
    }

    // TODO generalize to JvmComponentSpec when handles getSources
    @Defaults
    public void addTestSuiteSourceSets(@Each JvmTestSuiteSpec component) {
        component.getSources().create("clojure", ClojureSourceSet.class);
    }

    @BinaryTasks
    public void createSourceProcessTasks(ModelMap<Task> tasks, JvmBinarySpec binary) {
        binary.getInputs().withType(ClojureSourceSet.class, sourceSet -> {
            String taskName = binary.getTasks().taskName("process", sourceSet.getName());
            tasks.create(taskName, ProcessResources.class, task -> {
                task.setDescription(String.format("Compiles %s", sourceSet));
                task.dependsOn(sourceSet);
                task.from(sourceSet.getSource());
                task.setDestinationDir(binary.getResourcesDir());
                ((WithJvmAssembly) binary).getAssembly().builtBy(task);
            });
        });
    }

    @BinaryTasks
    public void createAotCompileTasks(ModelMap<Task> tasks, JarBinarySpec binary, GracljInternal internal) {
        if (binary.getLibrary() instanceof ClojureGeneralComponentSpec) {
            boolean aot = ((ClojureGeneralComponentSpec) binary.getLibrary()).isAot();
            if (aot) {
                binary.getInputs().withType(ClojureSourceSet.class, sourceSet -> {
                    String taskName = binary.getTasks().taskName("compile", sourceSet.getName());
                    tasks.create(taskName, ClojureCompile.class, task -> {
                        task.setDescription(String.format("Compiles %s", sourceSet));
                        task.dependsOn(sourceSet);
                        task.setSource(sourceSet.getSource());
                        task.setCompiler(internal.resolve("org.graclj:graclj-tools:" + internal.getGracljVersion()));
                        task.setClasspath(internal.resolve(binary.getLibrary().getDependencies()));
                        task.setDestinationDir(binary.getClassesDir());
                        ((WithJvmAssembly) binary).getAssembly().builtBy(task);
                    });
                });
            }
        }
    }
}
