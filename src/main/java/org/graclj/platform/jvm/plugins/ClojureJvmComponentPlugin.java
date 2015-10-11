package org.graclj.platform.jvm.plugins;

import org.graclj.internal.DependencyExtension;
import org.graclj.internal.DontUnderstand;
import org.graclj.internal.plugins.GracljInternalPlugin;
import org.graclj.platform.jvm.ClojureJvmLibrarySpec;
import org.graclj.platform.jvm.internal.toolchain.DefaultClojureJvmToolChainRegistry;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.model.Model;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.ComponentType;
import org.gradle.platform.base.ComponentTypeBuilder;

public class ClojureJvmComponentPlugin implements Plugin<Project> {

    public void apply(Project project) {
        project.getPluginManager().apply(GracljInternalPlugin.class);
    }

    @DontUnderstand("How does this get applied? It's not part of the plugin apply method.")
    static class Rules extends RuleSource {
        @Model
        ClojureJvmToolChainRegistry clojureToolChain(DependencyExtension dependencies) {
            return new DefaultClojureJvmToolChainRegistry(dependencies);
        }

//        @ComponentType
//        void register(ComponentTypeBuilder<ClojureJvmLibrarySpec> builder) {
//            builder.defaultImplementation(DefaultClojureJvmLibrarySpec.class);
//        }
    }
}
