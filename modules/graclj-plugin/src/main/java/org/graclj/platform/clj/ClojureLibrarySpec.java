package org.graclj.platform.clj;

import org.gradle.jvm.JvmLibrarySpec;
import org.gradle.model.Managed;

@Managed
public interface ClojureLibrarySpec extends JvmLibrarySpec, ClojureGeneralComponentSpec {
}
