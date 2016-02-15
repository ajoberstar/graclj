package org.graclj.language.clj.plugins

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Paths

class ClojureLanguagePluginTest extends Specification {
    private static final URI PLUGIN_REPO = Paths.get(System.properties['test.plugin.repo']).toUri()

    @Rule TemporaryFolder projectDir = new TemporaryFolder()

    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withGradleVersion(System.properties['test.gradle.version'])
    }

    def 'basic clojure config works'() {
        given: 'a build file with basic clojure configuration'
        projectDir.newFile('build.gradle') << """
buildscript {
    repositories {
        maven {
            url = '${PLUGIN_REPO}'
        }
    }
    dependencies {
        classpath 'org.graclj:graclj-plugin:+'
    }
}

apply plugin: 'org.graclj.clojure-lang'
apply plugin: 'jvm-component'
apply plugin: 'maven-publish'

repositories {
  jcenter()
  mavenLocal()
}

model {
    components {
        main(JvmLibrarySpec) {
            dependencies {
                module 'org.clojure:clojure:1.8.0'
            }
        }
    }
}

publishing {
    publications {
        main(MavenPublication) {
            groupId = 'org.graclj.sample'
            version = '0.2.0-SNAPSHOT'
            artifact(tasks.createMainJar)
        }
    }
    repositories {
        maven {
            name = 'project'
            url = file('build/repo')
        }
    }
}

import java.nio.file.Files
import java.util.stream.Collectors

task verifyPublish {
    doLast {
        def repoDir = file('build/repo').toPath()
        def files = Files.walk(repoDir)
            .filter { path -> path.getFileName().toString().endsWith('.jar') }
            .collect(Collectors.toSet())
        def expected = [
            repoDir.resolve("org/graclj/sample/\${project.name}/0.2.0-SNAPSHOT/\${project.name}-0.2.0-SNAPSHOT.jar"),
        ] as Set

        assert files == expected
    }
}

import org.graclj.internal.GracljInternal

class MyRules extends RuleSource {
    @Mutate
    void createTask(ModelMap<Task> tasks, @Path('binaries.mainJar') JarBinarySpec jar, GracljInternal internal) {
        tasks.create('clojureWorks', JavaExec) {
            classpath jar.getJarFile()
            classpath internal.resolve(jar.getLibrary().getDependencies())

            main = 'clojure.main'
            args '--main', 'sample.yay', 'does', 'it', 'work'
        }
    }
}

apply plugin: MyRules
"""
        projectDir.newFolder('src', 'main', 'clojure', 'sample')
        projectDir.newFile('src/main/clojure/sample/yay.clj') << """
(ns sample.yay
    (:require [clojure.string :as str])
    (:gen-class))

(defn my-sample [x] (str/reverse x))

(defn -main [& args]
  (println (map my-sample args)))
"""


        when: 'the build task is executed'
        def result = runner
            .withArguments('clean', 'components', 'build', 'clojureWorks', 'publishMainPublicationToProjectRepository', 'verifyPublish', '--stacktrace')
            .build()
        then: 'the expected tasks were executed'
        result.tasks*.path == [
            ':clean',
            ':components',
            ':compileMainAotJarMainClojure',
            ':processMainAotJarMainClojure',
            ':createMainAotJar',
            ':mainAotApiJar',
            ':mainAotJar',
            ':processMainJarMainClojure',
            ':createMainJar',
            ':mainApiJar',
            ':mainJar',
            ':assemble',
            ':check',
            ':build',
            ':clojureWorks',
            ':generatePomFileForMainPublication',
            ':publishMainPublicationToProjectRepository',
            ':verifyPublish'
        ]
    }

    def 'aot clojure config works'() {
        given: 'a build file with basic clojure configuration'
        projectDir.newFile('build.gradle') << """
buildscript {
    repositories {
        maven {
            url = '${PLUGIN_REPO}'
        }
    }
    dependencies {
        classpath 'org.graclj:graclj-plugin:+'
    }
}

apply plugin: 'org.graclj.clojure-lang'
apply plugin: 'jvm-component'

repositories {
  jcenter()
  mavenLocal()
}

model {
    components {
        main(JvmLibrarySpec) {
            dependencies {
                module 'org.clojure:clojure:1.8.0'
            }
        }
    }
}

import org.graclj.internal.GracljInternal

class MyRules extends RuleSource {
    @Mutate
    void createTask(ModelMap<Task> tasks, @Path('binaries.mainAotJar') JarBinarySpec jar, GracljInternal internal) {
        tasks.create('clojureWorks', JavaExec) {
            dependsOn jar
            classpath jar.getJarFile()
            classpath internal.resolve(jar.getLibrary().getDependencies())

            main = 'sample.yay'
            args 'does', 'it', 'work'
        }
    }
}

apply plugin: MyRules
"""
        projectDir.newFolder('src', 'main', 'clojure', 'sample')
        projectDir.newFile('src/main/clojure/sample/yay.clj') << """
(ns sample.yay
    (:require [clojure.string :as str])
    (:gen-class))

(defn my-sample [x] (str/reverse x))

(defn -main [& args]
  (println (map my-sample args)))
"""


        when: 'the build task is executed'
        def result = runner
            .withArguments('clean', 'components', 'build', 'clojureWorks', '--stacktrace')
            .build()
        then: 'the expected tasks were executed'
        println result.output
        result.tasks*.path == [
            ':clean',
            ':components',
            ':compileMainAotJarMainClojure',
            ':processMainAotJarMainClojure',
            ':createMainAotJar',
            ':mainAotApiJar',
            ':mainAotJar',
            ':processMainJarMainClojure',
            ':createMainJar',
            ':mainApiJar',
            ':mainJar',
            ':assemble',
            ':check',
            ':build',
            ':clojureWorks'
        ]
    }
}
