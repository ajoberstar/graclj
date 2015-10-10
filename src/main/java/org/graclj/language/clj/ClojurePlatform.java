package org.graclj.language.clj;

import org.gradle.platform.base.Platform;

public interface ClojurePlatform extends Platform {
  String getClojureVersion();
}
