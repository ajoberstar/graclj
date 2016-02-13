package orrg.ajoberstar.gradle.hack

import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.wrapper.Download
import org.gradle.wrapper.Install
import org.gradle.wrapper.Logger
import org.gradle.wrapper.PathAssembler
import org.gradle.wrapper.WrapperConfiguration

/**
 * Include this in your project's buildSrc, then add a dependency to your project:
 * compile new GradleDist(project, '2.11').asFileTree
 *
 * This is a complete hack and uses internal Gradle APIs. This occassionally causes
 * it to break when upgrading the Gradle version of your build due to changes in the
 * Wrapper APIs. Tends to be pretty easy to update at that point though.
 */
class GradleDist {
    private final Project project
    private final URI uri

    private GradleDist(Project project, URI uri) {
        this.project = project
        this.uri = uri
    }

    FileTree getAsFileTree() {
        Logger logger = new Logger(true)
        Install install = new Install(logger, new Download(logger, 'gradle', ''), new PathAssembler(project.gradle.gradleUserHomeDir))
        WrapperConfiguration config = new WrapperConfiguration()
        config.distribution = uri
        File file = install.createDist(config)
        return project.fileTree(dir:file, include:'**/*.jar')
    }

    static GradleDist create(Project project, String version) {
        return new GradleDist(project, new URI("https://services.gradle.org/distributions/gradle-${version}-bin.zip"))
    }

    static GradleDist createNightly(Project project, String version) {
        return new GradleDist(project, new URI("https://services.gradle.org/distributions-snapshots/gradle-${version}-bin.zip"))
    }
}
