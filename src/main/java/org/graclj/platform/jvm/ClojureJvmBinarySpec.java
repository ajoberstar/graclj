package org.graclj.platform.jvm;

import org.gradle.platform.base.BinarySpec;

import java.io.File;

public interface ClojureJvmBinarySpec extends BinarySpec {
    File getClassesDir();

    void setClassesDir(File classesDir);

    File getJarFile();

    void setJarFile(File jarFile);
}
