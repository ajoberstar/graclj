package org.graclj.testing.clj;

import org.graclj.platform.clj.ClojureGeneralComponentSpec;
import org.gradle.jvm.test.JvmTestSuiteSpec;
import org.gradle.model.Managed;

@Managed
public interface ClojureTestSuiteSpec extends JvmTestSuiteSpec, ClojureGeneralComponentSpec {
}
