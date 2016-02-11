package org.graclj.language.clj.plugins;

import org.graclj.internal.GracljInternal;
import org.graclj.language.clj.ClojureAotJarBinarySpec;
import org.graclj.language.clj.ClojureSourceSet;
import org.graclj.language.clj.tasks.ClojureCompile;
import org.gradle.api.Task;
import org.gradle.jvm.JvmComponentSpec;
import org.gradle.jvm.JvmLibrarySpec;
import org.gradle.jvm.internal.JvmAssembly;
import org.gradle.jvm.internal.JvmBinarySpecInternal;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.jvm.platform.internal.DefaultJavaPlatform;
import org.gradle.language.base.internal.LanguageSourceSetInternal;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.Defaults;
import org.gradle.model.Each;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.*;

import static org.gradle.util.CollectionUtils.first;

public class ClojureLanguageRules extends RuleSource {
    @BinaryType
    public void registerAotJarBinary(TypeBuilder<ClojureAotJarBinarySpec> builder) {
    }

    @ComponentBinaries
    public void createAotJarBinaries(ModelMap<ClojureAotJarBinarySpec> binaries, JvmLibrarySpec library) {
        binaries.create("aotJar", binary -> {
            binary.setExportedPackages(library.getApi().getExports());
            // TODO stop using internals
            binary.setTargetPlatform(DefaultJavaPlatform.current());
        });
    }

    @BinaryTasks
    public void createAotCompileTasks(ModelMap<Task> tasks, ClojureAotJarBinarySpec binary, GracljInternal internal) {
        binary.getInputs().withType(ClojureSourceSet.class, sourceSet -> {
            String taskName = "compile" + capitalize(binary.getProjectScopedName()) + capitalize(((LanguageSourceSetInternal) sourceSet).getProjectScopedName());
            tasks.create(taskName, ClojureCompile.class, task -> {
                task.setDescription(String.format("Compiles %s", sourceSet));
                task.dependsOn(sourceSet);
                task.setSource(sourceSet.getSource());
                task.setCompiler(internal.resolve("org.graclj:graclj-tools:0.1.0-rc.1"));
                task.setClasspath(internal.resolve(binary.getLibrary().getDependencies()));

                // The first directory is the one created by JvmComponentPlugin.configureJvmBinaries()
                // to be used as the default output directory for compiled classes
                JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
                task.setDestinationDir(first(assembly.getClassDirectories()));

                assembly.builtBy(task);
            });
        });
    }

    @BinaryTasks
    public void createSourceProcessTasks(ModelMap<Task> tasks, JvmBinarySpecInternal binary, GracljInternal internal) {
        binary.getInputs().withType(ClojureSourceSet.class, sourceSet -> {
            String taskName = "process" + capitalize(binary.getProjectScopedName()) + capitalize(((LanguageSourceSetInternal) sourceSet).getProjectScopedName());
            tasks.create(taskName, ProcessResources.class, task -> {
                task.setDescription(String.format("Compiles %s", sourceSet));
                task.dependsOn(sourceSet);
                task.from(sourceSet.getSource());

                // The first directory is the one created by JvmComponentPlugin.configureJvmBinaries()
                // to be used as the default output directory for compiled classes
                JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
                task.setDestinationDir(first(assembly.getResourceDirectories()));

                assembly.builtBy(task);
            });
        });
    }

    @Defaults
    public void addSourceSets(@Each JvmComponentSpec component) {
        component.getSources().create("clojure", ClojureSourceSet.class);
    }

    @Defaults
    public void defaultGracljTools(@Each ClojureAotJarBinarySpec binary) {
    }

    @LanguageType
    public void registerLanguage(TypeBuilder<ClojureSourceSet> builder) {
    }

    private static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
