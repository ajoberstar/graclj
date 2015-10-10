package org.graclj.language.clj.internal;

import org.graclj.language.clj.ClojurePlatform;

public class DefaultClojurePlatform implements ClojurePlatform {
  private final String version;

  public DefaultClojurePlatform(String version) {
    this.version = version;
  }

  public String getDisplayName() {
    return String.format("Clojure Platform (Clojure %s)", version);
  }

  public String getName() {
    return String.format("ClojurePlatform%s", version);
  }

  public String getClojureVersion() {
    return version;
  }
}
