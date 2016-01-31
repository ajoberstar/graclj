package org.graclj.test.jvm;

import org.gradle.jvm.JvmComponentSpec;
import org.gradle.jvm.test.JvmTestSuiteSpec;
import org.gradle.model.Managed;
import org.gradle.platform.base.DependencySpecContainer;
import org.gradle.testing.base.TestSuiteSpec;

@Managed
public interface ClojureTestSuiteSpec extends TestSuiteSpec, JvmComponentSpec {
    @Override
    JvmComponentSpec getTestedComponent();

    DependencySpecContainer getDependencies();

    String getClojureVersion();

    void setClojureVersion(String version);
}
