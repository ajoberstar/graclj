package org.graclj.platform.jvm.plugins;

import org.graclj.internal.DependencyExtension;
import org.graclj.internal.Util;
import org.graclj.internal.plugins.GracljInternalPlugin;
import org.graclj.platform.jvm.ClojureJvmBinarySpec;
import org.graclj.platform.jvm.ClojureJvmLibrarySpec;
import org.graclj.platform.jvm.internal.DefaultClojureJvmBinarySpec;
import org.graclj.platform.jvm.internal.DefaultClojureJvmLibrarySpec;
import org.graclj.platform.jvm.internal.toolchain.DefaultClojureJvmToolChainRegistry;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.model.Defaults;
import org.gradle.model.Model;
import org.gradle.model.ModelMap;
import org.gradle.model.RuleSource;
import org.gradle.model.internal.registry.ModelRegistry;
import org.gradle.model.internal.type.ModelType;
import org.gradle.platform.base.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class ClojureJvmComponentPlugin implements Plugin<Project> {
    private final ModelRegistry modelRegistry;

    @Inject
    public ClojureJvmComponentPlugin(ModelRegistry modelRegistry) {
        this.modelRegistry = modelRegistry;
    }


    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);
        project.getPluginManager().apply(ComponentModelBasePlugin.class);
        modelRegistry.getRoot().applyToAllLinksTransitive(ModelType.of(ClojureJvmLibrarySpec.class), BinaryRules.class);
    }

    @SuppressWarnings("unused")
    static class Rules extends RuleSource {
        @Model
        ClojureJvmToolChainRegistry clojureToolChain(DependencyExtension dependencies) {
            return new DefaultClojureJvmToolChainRegistry(dependencies);
        }

        @ComponentType
        void registerLibrary(ComponentTypeBuilder<ClojureJvmLibrarySpec> builder) {
            System.out.println("registerLibrary");
            builder.defaultImplementation(DefaultClojureJvmLibrarySpec.class);
        }

        @BinaryType
        void registerBinary(BinaryTypeBuilder<ClojureJvmBinarySpec> builder) {
            System.out.println("registerBinary");
            builder.defaultImplementation(DefaultClojureJvmBinarySpec.class);
        }

        @ComponentBinaries
        void createBinariesForLibrary(ModelMap<ClojureJvmBinarySpec> binaries, ClojureJvmLibrarySpec component) {
            System.out.println("createBinariesForLibrary");
            binaries.create(component.getName() + "Jar");
        }

        @BinaryTasks
        void createBinaryTasksForJar(ModelMap<Jar> tasks, ClojureJvmBinarySpec binary) {
            System.out.println("createBinaryTasksForJar");
            String taskName = "create" + Util.capitalize(binary.getName());
            tasks.create(taskName, Jar.class, jar -> {
                jar.setDescription("Creates the binary file for " + binary);
                jar.from(binary.getClassesDir());
                jar.setDestinationDir(binary.getJarFile().getParentFile());
                jar.setArchiveName(binary.getJarFile().getName());
            });
        }
    }
}
