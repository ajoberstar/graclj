package org.graclj.tools.clojure;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class Compiler {
    public static void main(String[] args) {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("org.graclj.tools.clojure"));
        IFn compile = Clojure.var("org.graclj.tools.clojure", "sample");
        compile.invoke(Clojure.read("\"Is it working?\""));
    }
}
