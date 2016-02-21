package org.graclj.platform.clj.plugins;

import org.graclj.internal.GracljInternal;
import org.graclj.platform.clj.ClojureApplicationSpec;
import org.graclj.platform.clj.ClojureLibrarySpec;
import org.gradle.api.Task;
import org.gradle.api.file.FileTree;
import org.gradle.jvm.JarBinarySpec;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.jvm.tasks.Jar;
import org.gradle.model.Defaults;
import org.gradle.model.Each;
import org.gradle.model.Finalize;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinaryTasks;
import org.gradle.platform.base.ComponentType;
import org.gradle.platform.base.TypeBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClojureComponentRules extends RuleSource {
    @ComponentType
    public void registerLibrary(TypeBuilder<ClojureLibrarySpec> builder) {
        // using managed type
    }

    @ComponentType
    public void registarApplication(TypeBuilder<ClojureApplicationSpec> builder) {
        // using managed type
    }

    @Defaults
    public void applicationDefaults(@Each ClojureApplicationSpec component) {
        component.setUberjar(true);
        component.setAot(true);
    }

    @BinaryTasks
    public void uberjarPrep(ModelMap<Task> tasks, JarBinarySpec binary, GracljInternal internal) {
        if (binary.getLibrary() instanceof ClojureApplicationSpec) {
            boolean uberjar = ((ClojureApplicationSpec) binary.getLibrary()).isUberjar();
            if (uberjar) {
                String taskName = binary.getTasks().taskName("extractDependencies");
                File destinationDir = binary.getResourcesDir();
                tasks.create(taskName, task -> {
                    task.setDescription("Extract binary's dependencies for use in an uberjar.");

                    task.doLast(t -> {
                        internal.resolve(binary.getLibrary().getDependencies()).forEach(file -> {
                            if (file.getName().endsWith(".jar")) {
                                FileTree tree = task.getProject().zipTree(file);
                                task.getProject().copy(spec -> {
                                    spec.from(tree);
                                    spec.into(destinationDir);
                                    spec.exclude("project.clj", "META-INF/MANIFEST.MF", "META-INF/NOTICE*", "META-INF/LICENSE*", "META-INF/DEPENDENCIES");
                                });
                            }
                        });
                    });

                    ((WithJvmAssembly) binary).getAssembly().builtBy(task);
                });
            }
        }
    }

    // TODO this is gross...
    @Finalize
    public void setMainClass(@Each JarBinarySpec binary) {
        if (binary.getLibrary() instanceof ClojureApplicationSpec) {
            Map<String, String> attrs = new HashMap<>();

            String main = ((ClojureApplicationSpec) binary.getLibrary()).getMain();
            attrs.put("Main-Class", main);

            binary.getTasks().whenObjectAdded(task -> {
               if (task instanceof Jar) {
                   ((Jar) task).getManifest().attributes(attrs);
               }
            });
        }
    }
}
