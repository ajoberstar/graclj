package org.graclj.language.jvm.plugins;

import org.graclj.language.jvm.ClojureSourceSet;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.jvm.JvmResources;
import org.gradle.jvm.internal.JvmAssembly;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.internal.registry.LanguageTransform;
import org.gradle.language.base.internal.registry.LanguageTransformContainer;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.BinarySpec;
import org.gradle.platform.base.LanguageType;
import org.gradle.platform.base.LanguageTypeBuilder;

import java.util.Collections;
import java.util.Map;

import static org.gradle.util.CollectionUtils.first;

public class ClojureLanguageRules extends RuleSource {
    @LanguageType
    public void registerLanguage(LanguageTypeBuilder<ClojureSourceSet> builder) {
        builder.setLanguageName("clojure");
    }

    // TODO stop using internals
    @Mutate
    public void registerTransform(LanguageTransformContainer transforms) {
        transforms.add(new Clojure());
    }

    // TODO stop using internals
    // copied from jvmresourcesplugin
    private static class Clojure implements LanguageTransform<ClojureSourceSet, JvmResources> {
        public Class<ClojureSourceSet> getSourceSetType() {
            return ClojureSourceSet.class;
        }

        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        public Class<org.gradle.jvm.JvmResources> getOutputType() {
            return org.gradle.jvm.JvmResources.class;
        }

        public SourceTransformTaskConfig getTransformTask() {
            return new SourceTransformTaskConfig() {
                public String getTaskPrefix() {
                    return "process";
                }

                public Class<? extends DefaultTask> getTaskType() {
                    return ProcessResources.class;
                }

                public void configureTask(Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry) {
                    ProcessResources resourcesTask = (ProcessResources) task;
                    ClojureSourceSet resourceSet = (ClojureSourceSet) sourceSet;
                    resourcesTask.from(resourceSet.getSource());

                    // The first directory is the one created by JvmComponentPlugin.configureJvmBinaries()
                    // to be used as the default output directory for processed resources
                    JvmAssembly assembly = ((WithJvmAssembly) binary).getAssembly();
                    resourcesTask.setDestinationDir(first(assembly.getResourceDirectories()));

                    assembly.builtBy(resourcesTask);
                }
            };
        }
        public boolean applyToBinary(BinarySpec binary) {
            return binary instanceof WithJvmAssembly;
        }
    }
}
