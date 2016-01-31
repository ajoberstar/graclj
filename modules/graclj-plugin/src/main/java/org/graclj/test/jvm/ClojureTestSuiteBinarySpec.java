package org.graclj.test.jvm;

import org.graclj.test.jvm.tasks.ClojureTest;
import org.gradle.jvm.JvmBinarySpec;
import org.gradle.jvm.internal.DependencyResolvingClasspath;
import org.gradle.jvm.internal.WithDependencies;
import org.gradle.model.Managed;
import org.gradle.testing.base.TestSuiteBinarySpec;
import org.gradle.testing.base.TestSuiteTaskCollection;

@Managed
public interface ClojureTestSuiteBinarySpec extends TestSuiteBinarySpec, JvmBinarySpec, WithDependencies {
    /**
     * Provides direct access to important build tasks of JVM test suites.
     */
    interface ClojureTestSuiteTasks extends TestSuiteTaskCollection {
        ClojureTest getRun();
    }

    @Override
    ClojureTestSuiteSpec getTestSuite();

    JvmBinarySpec getTestedBinary();

    @Override
    ClojureTestSuiteTasks getTasks();

    DependencyResolvingClasspath getRuntimeClasspath();

    String getClojureVersion();

    void setClojureVersion(String version);
}
