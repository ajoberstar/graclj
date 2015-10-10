package org.graclj.language.clj.internal;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.jvm.Classpath;
import org.graclj.language.clj.ClojurePlatform;
import org.graclj.language.clj.toolchain.ClojureToolChain;

public class DownloadingClojureToolChain implements ClojureToolChain {
  private final ConfigurationContainer configurations;
  private final DependencyHandler dependencies;

  public DownloadingClojureToolChain(ConfigurationContainer configurations, DependencyHandler dependencies) {
    this.configurations = configurations;
    this.dependencies = dependencies;
  }

  @Override
  public String getDisplayName() {
    return "Clojure Tool Chain";
  }

  @Override
  public String getName() {
    return "ClojureToolChain";
  }

  @Override
  public Classpath getClojure(ClojurePlatform platform) {
    Dependency clojure = dependencies.create(String.format("org.clojure:clojure:%s", platform.getClojureVersion()));
    Dependency toolsNamespace = dependencies.create(String.format("org.clojure:tools.namespace:0.3.0-alpha1"));
    Dependency gracljTools = dependencies.create(String.format("org.graclj:graclj-tools:0.1.0-SNAPSHOT"));
    Configuration configuration = configurations.detachedConfiguration(clojure, toolsNamespace, gracljTools);
    // TODO: Should this resolve eagerly?
    return new BasicClasspath(configuration);
  }
}
