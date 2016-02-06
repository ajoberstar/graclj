package org.graclj.language.clj.plugins;

import org.graclj.internal.GracljInternal;
import org.graclj.language.clj.ClojureAotJarBinarySpec;
import org.graclj.language.clj.ClojureSourceSet;
import org.graclj.language.clj.tasks.ClojureCompile;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.jvm.JvmLibrarySpec;
import org.gradle.jvm.JvmResources;
import org.gradle.jvm.internal.JvmAssembly;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.jvm.platform.internal.DefaultJavaPlatform;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.LanguageSourceSetInternal;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.internal.registry.LanguageTransform;
import org.gradle.language.base.internal.registry.LanguageTransformContainer;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.*;
import org.gradle.platform.base.*;

import java.util.Collections;
import java.util.Map;

import static org.gradle.util.CollectionUtils.first;

public class ClojureLanguageRules extends RuleSource {
    @BinaryType
    public void registerAotJarBinary(BinaryTypeBuilder<ClojureAotJarBinarySpec> builder) {
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
                task.setCompiler(internal.resolve("org.graclj:graclj-tools:0.1.0-SNAPSHOT"));
                task.setClasspath(internal.resolve(binary.getLibrary().getDependencies()));

                // The first directory is the one created by JvmComponentPlugin.configureJvmBinaries()
                // to be used as the default output directory for compiled classes
                JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
                task.setDestinationDir(first(assembly.getClassDirectories()));

                assembly.builtBy(task);
            });
        });
    }

    @Defaults
    public void defaultGracljTools(@Each ClojureAotJarBinarySpec binary) {
    }

    @LanguageType
    public void registerLanguage(LanguageTypeBuilder<ClojureSourceSet> builder) {
        builder.setLanguageName("clojure");
    }

    // TODO stop using internals
    @Mutate
    public void registerTransform(LanguageTransformContainer transforms, GracljInternal internal) {
        transforms.add(new ClojureSource());
    }

    // TODO stop using internals
    // copied from jvmresourcesplugin
    private static class ClojureSource implements LanguageTransform<ClojureSourceSet, JvmResources> {
        public Class<ClojureSourceSet> getSourceSetType() {
            return ClojureSourceSet.class;
        }

        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        public Class<JvmResources> getOutputType() {
            return JvmResources.class;
        }

        public SourceTransformTaskConfig getTransformTask() {
            return new SourceTransformTaskConfig() {
                public String getTaskPrefix() {
                    return "process";
                }

                public Class<? extends DefaultTask> getTaskType() {
                    return ProcessResources.class;
                }

                public void configureTask(Task rawTask, BinarySpec binary, LanguageSourceSet rawSourceSet, ServiceRegistry serviceRegistry) {
                    ProcessResources task = (ProcessResources) rawTask;
                    ClojureSourceSet sourceSet = (ClojureSourceSet) rawSourceSet;
                    task.from(sourceSet.getSource());

                    // The first directory is the one created by JvmComponentPlugin.configureJvmBinaries()
                    // to be used as the default output directory for processed resources
                    JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
                    task.setDestinationDir(first(assembly.getResourceDirectories()));

                    assembly.builtBy(task);
                }
            };
        }
        public boolean applyToBinary(BinarySpec binary) {
            return binary instanceof WithJvmAssembly;
        }
    }

    private static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
