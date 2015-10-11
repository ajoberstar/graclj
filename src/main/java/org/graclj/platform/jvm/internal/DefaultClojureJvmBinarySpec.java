package org.graclj.platform.jvm.internal;

import org.graclj.platform.jvm.ClojureJvmBinarySpec;
import org.gradle.platform.base.binary.BaseBinarySpec;

import java.io.File;

public class DefaultClojureJvmBinarySpec extends BaseBinarySpec implements ClojureJvmBinarySpec {
    private File classesDir;
    private File jarFile;

    @Override
    public File getClassesDir() {
        return classesDir;
    }

    @Override
    public void setClassesDir(File classesDir) {
        this.classesDir = classesDir;
    }

    @Override
    public File getJarFile() {
        return jarFile;
    }

    @Override
    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }
}
