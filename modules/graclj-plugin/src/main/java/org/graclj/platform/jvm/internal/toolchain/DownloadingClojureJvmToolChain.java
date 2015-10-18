package org.graclj.platform.jvm.internal.toolchain;

import org.graclj.internal.GracljInternal;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChain;
import org.gradle.jvm.Classpath;

public class DownloadingClojureJvmToolChain implements ClojureJvmToolChain {
    private final ClojureJvmPlatform platform;
    private final GracljInternal dependencies;

    public DownloadingClojureJvmToolChain(ClojureJvmPlatform platform, GracljInternal dependencies) {
        this.platform = platform;
        this.dependencies = dependencies;
    }

    @Override
    public String getDisplayName() {
        return String.format("%s ToolChain", platform.getDisplayName());
    }

    @Override
    public String getName() {
        return String.format("%stoolchain", platform.getName());
    }

    @Override
    public Classpath getCompiler() {
        String clojure = String.format("org.clojure:clojure:%s", platform.getVersion());
        String toolsNamespace = String.format("org.clojure:tools.namespace:0.3.0-alpha1");
        String gracljTools = String.format("org.graclj:graclj-tools:0.1.0-SNAPSHOT");
        return dependencies.resolve(clojure, toolsNamespace, gracljTools);
    }
}
