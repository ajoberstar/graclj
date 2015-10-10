package org.graclj.language.jvm.plugins;

import org.graclj.internal.DontUnderstand;
import org.graclj.internal.InternalUse;
import org.graclj.language.jvm.ClojureJvmSourceSet;
import org.graclj.language.jvm.internal.DefaultClojureJvmSourceSet;
import org.graclj.language.jvm.tasks.ClojureJvmCompile;
import org.graclj.platform.jvm.internal.DefaultClojureJvmPlatform;
import org.graclj.platform.jvm.plugins.ClojureJvmPlatformPlugin;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.api.*;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.jvm.JvmBinarySpec;
import org.gradle.jvm.JvmByteCode;
import org.gradle.jvm.JvmResources;
import org.gradle.jvm.platform.internal.DefaultJavaPlatform;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.internal.registry.LanguageTransform;
import org.gradle.language.base.internal.registry.LanguageTransformContainer;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.language.jvm.plugins.JvmResourcesPlugin;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.Finalize;
import org.gradle.model.ModelSet;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinarySpec;
import org.gradle.platform.base.LanguageType;
import org.gradle.platform.base.LanguageTypeBuilder;

import java.util.Collections;
import java.util.Map;

public class ClojureLanguagePlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getPluginManager().apply(ComponentModelBasePlugin.class);
        project.getPluginManager().apply(JvmResourcesPlugin.class);
        project.getPluginManager().apply(ClojureJvmPlatformPlugin.class);
    }

    @DontUnderstand("How does this get applied? It's not part of the plugin apply method.")
    static class Rules extends RuleSource {
        @LanguageType
        void registerLanguage(LanguageTypeBuilder<ClojureJvmSourceSet> builder) {
            builder.setLanguageName("clojure");
            builder.defaultImplementation(DefaultClojureJvmSourceSet.class);
        }

        @InternalUse("Language transform is internal")
        @Mutate
        void registerLanguageTransform(LanguageTransformContainer languages) {
            languages.add(new ClojureSource());
            languages.add(new ClojureAot());
        }

        @Finalize
        void injectToolChainToCompile(TaskContainer tasks, ClojureJvmToolChainRegistry toolChainRegistry) {
            tasks.withType(ClojureJvmCompile.class, task -> task.setToolChainRegistry(toolChainRegistry));
        }
    }

    @InternalUse("Language transform is internal")
    private static class ClojureSource implements LanguageTransform<ClojureJvmSourceSet, JvmResources> {
        @Override
        public Class<ClojureJvmSourceSet> getSourceSetType() {
            return ClojureJvmSourceSet.class;
        }

        @Override
        public Class<JvmResources> getOutputType() {
            return JvmResources.class;
        }

        @DontUnderstand("What can this be used for?")
        @Override
        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        @Override
        public SourceTransformTaskConfig getTransformTask() {
            return new SourceTransformTaskConfig() {
                @Override
                public String getTaskPrefix() {
                    return "copy";
                }

                @Override
                public Class<? extends DefaultTask> getTaskType() {
                    return ProcessResources.class;
                }

                @Override
                public void configureTask(Task task, BinarySpec binarySpec, LanguageSourceSet languageSourceSet, ServiceRegistry serviceRegistry) {
                    ProcessResources resources = (ProcessResources) task;
                    JvmBinarySpec binary = (JvmBinarySpec) binarySpec;
                    ClojureJvmSourceSet sourceSet = (ClojureJvmSourceSet) languageSourceSet;

                    resources.setDescription(String.format("Copies source for %s", sourceSet));
                    resources.from(sourceSet.getSource());
                    resources.setDestinationDir(binary.getResourcesDir());
                    binary.getTasks().getJar().dependsOn(resources);
                }
            };
        }

        @Override
        public boolean applyToBinary(BinarySpec binarySpec) {
            return binarySpec instanceof JvmBinarySpec;
        }
    }

    @InternalUse("Language transform is internal")
    private static class ClojureAot implements LanguageTransform<ClojureJvmSourceSet, JvmByteCode> {
        @Override
        public Class<ClojureJvmSourceSet> getSourceSetType() {
            return ClojureJvmSourceSet.class;
        }

        @Override
        public Class<JvmByteCode> getOutputType() {
            return JvmByteCode.class;
        }

        @DontUnderstand("What can this be used for?")
        @Override
        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        @Override
        public SourceTransformTaskConfig getTransformTask() {
            return new SourceTransformTaskConfig() {
                @Override
                public String getTaskPrefix() {
                    return "compile";
                }

                @Override
                public Class<? extends DefaultTask> getTaskType() {
                    return ClojureJvmCompile.class;
                }

                @Override
                public void configureTask(Task task, BinarySpec binarySpec, LanguageSourceSet languageSourceSet, ServiceRegistry serviceRegistry) {
                    ClojureJvmCompile compile = (ClojureJvmCompile) task;
                    JvmBinarySpec binary = (JvmBinarySpec) binarySpec;
                    ClojureJvmSourceSet sourceSet = (ClojureJvmSourceSet) languageSourceSet;

                    // TODO: How do I input the platform elsewhere?
                    compile.setPlatform(new DefaultClojureJvmPlatform(1, 7, 0, null, new DefaultJavaPlatform(JavaVersion.current())));

                    compile.setDescription(String.format("AOT compiles %s", sourceSet));
                    compile.setSource(sourceSet.getSource());
                    compile.setClasspath(sourceSet.getCompileClasspath().getFiles());
                    compile.setDestinationDir(binary.getClassesDir());
                    binary.getTasks().getJar().dependsOn(compile);
                }
            };
        }

        @Override
        public boolean applyToBinary(BinarySpec binarySpec) {
            return binarySpec instanceof JvmBinarySpec;
        }
    }
}
