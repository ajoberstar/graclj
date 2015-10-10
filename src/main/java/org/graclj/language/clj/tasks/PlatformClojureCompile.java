package org.graclj.language.clj.tasks;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.jvm.Classpath;
import org.graclj.language.clj.ClojurePlatform;
import org.graclj.language.clj.toolchain.ClojureToolChain;
import org.graclj.language.clj.internal.DownloadingClojureToolChain;

import java.io.File;

public class PlatformClojureCompile extends SourceTask {
    private ClojurePlatform platform;
    private File destinationDir;
    private FileCollection classpath;
    private ClojureToolChain toolChain;

    public PlatformClojureCompile() {
      this.toolChain = new DownloadingClojureToolChain(getProject().getConfigurations(), getProject().getDependencies());
    }

    // @Input
    public ClojurePlatform getPlatform() {
      return platform;
    }

    public void setPlatform(ClojurePlatform platform) {
      this.platform = platform;
    }

    @OutputDirectory
    public File getDestinationDir() {
        return this.destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

    @InputFiles
    public FileCollection getClasspath() {
        return this.classpath;
    }

    public void setClasspath(FileCollection configuration) {
        this.classpath = configuration;
    }

    @TaskAction
    public void compile() {
      Classpath clojure = toolChain.getClojure(platform);
      getProject().javaexec(spec -> {
        spec.classpath(clojure.getFiles());
        spec.classpath(classpath);
        spec.classpath(destinationDir);
        spec.setMain("clojure.main");
        spec.args("--main", "org.graclj.tools.clojure");
        spec.args(destinationDir);
      });
    }
}
