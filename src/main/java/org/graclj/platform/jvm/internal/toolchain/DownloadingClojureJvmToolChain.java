package org.graclj.platform.jvm.internal.toolchain;

import org.graclj.internal.DependencyExtension;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChain;
import org.gradle.jvm.Classpath;

public class DownloadingClojureJvmToolChain implements ClojureJvmToolChain {
    private final ClojureJvmPlatform platform;
    private final DependencyExtension dependencies;

    public DownloadingClojureJvmToolChain(ClojureJvmPlatform platform, DependencyExtension dependencies) {
        this.platform = platform;
        this.dependencies = dependencies;
    }

    @Override
    public String getDisplayName() {
        return "Clojure JVM Tool Chain";
    }

    @Override
    public String getName() {
        return "ClojureJvmToolChain";
    }

    @Override
    public Classpath getCompiler() {
        String clojure = String.format("org.clojure:clojure:%s", platform.getClojureJvmVersion());
        String toolsNamespace = String.format("org.clojure:tools.namespace:0.3.0-alpha1");
        String gracljTools = String.format("org.graclj:graclj-tools:0.1.0-SNAPSHOT");
        return dependencies.resolve(clojure, toolsNamespace, gracljTools);
    }
}
