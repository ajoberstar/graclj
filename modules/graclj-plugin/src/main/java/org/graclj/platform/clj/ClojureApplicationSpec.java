package org.graclj.platform.clj;

import org.gradle.jvm.JvmLibrarySpec;
import org.gradle.model.Managed;

// TODO Yes, I know I shouldn't call this Application and extend Library
@Managed
public interface ClojureApplicationSpec extends JvmLibrarySpec, ClojureGeneralComponentSpec {
    String getMain();
    void setMain(String main);

    boolean isUberjar();
    void setUberjar(boolean uberjar);
}
