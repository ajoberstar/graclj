package org.graclj.language.jvm.plugins;

import org.graclj.language.jvm.ClojureJvmSourceSet;
import org.graclj.language.jvm.internal.DefaultClojureJvmSourceSet;
import org.graclj.language.jvm.tasks.ClojureJvmCompile;
import org.graclj.platform.jvm.internal.DefaultClojureJvmPlatform;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.artifacts.ArtifactDependencyResolver;
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository;
import org.gradle.internal.Transformers;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.jvm.JarBinarySpec;
import org.gradle.jvm.JvmByteCode;
import org.gradle.jvm.JvmResources;
import org.gradle.jvm.internal.DependencyResolvingClasspath;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.internal.registry.LanguageTransform;
import org.gradle.language.base.internal.registry.LanguageTransformContainer;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.gradle.model.Defaults;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.model.internal.manage.schema.ModelSchemaStore;
import org.gradle.platform.base.BinarySpec;
import org.gradle.platform.base.LanguageType;
import org.gradle.platform.base.LanguageTypeBuilder;
import org.gradle.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ClojureJvmLanguageRules extends RuleSource {
    @LanguageType
    public void registerLanguage(LanguageTypeBuilder<ClojureJvmSourceSet> builder) {
        builder.setLanguageName("clojure");
        builder.defaultImplementation(DefaultClojureJvmSourceSet.class);
    }

    @Defaults
    public void compileDefaults(ModelMap<ClojureJvmCompile> tasks, ClojureJvmToolChainRegistry toolChainRegistry) {
        tasks.afterEach(task -> {
            task.setToolChainRegistry(toolChainRegistry);
        });
    }

    @Mutate
    // TODO Stop using internals.
    public void registerLanguageTransform(LanguageTransformContainer transforms, ModelSchemaStore schemaStore) {
        transforms.add(new ClojureSource());
        transforms.add(new ClojureAot(schemaStore));
    }

    private static class ClojureSource implements LanguageTransform<ClojureJvmSourceSet, JvmResources> {
        @Override
        public Class<ClojureJvmSourceSet> getSourceSetType() {
            return ClojureJvmSourceSet.class;
        }

        @Override
        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        @Override
        public Class<JvmResources> getOutputType() {
            return JvmResources.class;
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
                public void configureTask(Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry) {
                    ProcessResources resources = (ProcessResources) task;
                    ClojureJvmSourceSet clojureSource = (ClojureJvmSourceSet) sourceSet;
                    JarBinarySpec jar = (JarBinarySpec) binary;

                    resources.setDescription(String.format("Copies source for %s", sourceSet));
                    resources.from(clojureSource.getSource());
                    resources.setDestinationDir(jar.getResourcesDir());

                    // TODO: Does this need to be explicit?
                    resources.dependsOn(clojureSource);

                    // TODO: Does this need to be explicit?
                    binary.getTasks().withType(Jar.class, jarTask -> {
                        jarTask.dependsOn(resources);
                    });
                }
            };
        }

        @Override
        public boolean applyToBinary(BinarySpec binary) {
            return binary instanceof JarBinarySpec;
        }
    }

    private static class ClojureAot implements LanguageTransform<ClojureJvmSourceSet, JvmByteCode> {
        private final ModelSchemaStore schemaStore;

        public ClojureAot(ModelSchemaStore schemaStore) {
            this.schemaStore = schemaStore;
        }

        @Override
        public Class<ClojureJvmSourceSet> getSourceSetType() {
            return ClojureJvmSourceSet.class;
        }

        @Override
        public Map<String, Class<?>> getBinaryTools() {
            return Collections.emptyMap();
        }

        @Override
        public Class<JvmByteCode> getOutputType() {
            return JvmByteCode.class;
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
                public void configureTask(Task task, BinarySpec binary, LanguageSourceSet sourceSet, ServiceRegistry serviceRegistry) {
                    ClojureJvmCompile compile = (ClojureJvmCompile) task;
                    ClojureJvmSourceSet clojureSource = (ClojureJvmSourceSet) sourceSet;
                    JarBinarySpec jar = (JarBinarySpec) binary;

                    compile.setDescription(String.format("AOT compiles %s", sourceSet));
                    // TODO: How do I input the platform elsewhere?
                    compile.setPlatform(new DefaultClojureJvmPlatform(1, 7, 0, null));
                    compile.setSource(sourceSet.getSource());
                    compile.setDestinationDir(jar.getClassesDir());

                    // TODO: This is horrible...
                    ArtifactDependencyResolver dependencyResolver = serviceRegistry.get(ArtifactDependencyResolver.class);
                    RepositoryHandler repositories = serviceRegistry.get(RepositoryHandler.class);
                    List<ResolutionAwareRepository> resolutionAwareRepositories = CollectionUtils.collect(repositories, Transformers.cast(ResolutionAwareRepository.class));
                    DependencyResolvingClasspath classpath = new DependencyResolvingClasspath(jar, clojureSource, dependencyResolver, schemaStore, resolutionAwareRepositories);
                    compile.setClasspath(classpath);

                    // TODO: Does this need to be explicit?
                    compile.dependsOn(clojureSource);

                    // TODO: Does this need to be explicit?
                    binary.getTasks().withType(Jar.class, jarTask -> {
                        jarTask.dependsOn(compile);
                    });
                }
            };
        }

        @Override
        public boolean applyToBinary(BinarySpec binary) {
            return binary instanceof JarBinarySpec;
        }
    }
}
