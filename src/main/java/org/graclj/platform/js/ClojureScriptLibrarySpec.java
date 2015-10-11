package org.graclj.platform.js;

import org.graclj.platform.common.ClojureLibrarySpec;

public interface ClojureScriptLibrarySpec extends ClojureLibrarySpec {
    String getMainNamespace();

    void setMainNamespace(String namespace);
}
