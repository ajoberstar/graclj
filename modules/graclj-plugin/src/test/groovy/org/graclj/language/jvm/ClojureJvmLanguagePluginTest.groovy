package org.graclj.language.jvm

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.nio.file.Paths

class ClojureJvmLanguagePluginTest extends Specification {
    private static final URI PLUGIN_REPO = Paths.get(System.properties['plugin.repo']).toUri()

    @Rule TemporaryFolder projectDir = new TemporaryFolder()

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

repositories {
  jcenter()
  mavenLocal()
}

model {
    components {
        main(JvmLibrarySpec)
    }
}

import org.graclj.internal.GracljInternal

class MyRules extends RuleSource {
    @Mutate
    void createTask(ModelMap<Task> tasks, @Path('binaries.mainJar') JarBinarySpec jar, GracljInternal internals) {
        tasks.create('clojureWorks', JavaExec) {
            classpath jar.getJarFile()
            classpath internals.resolve('org.clojure:clojure:1.7.0').getFiles()

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
        def result = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withArguments('build', 'clojureWorks', '--stacktrace')
            .build()
        then: 'the expected tasks were executed'
        result.tasks*.path == [
            ':compileMainJarMainClojure',
            ':copyMainJarMainClojure',
            ':createMainJar',
            ':createMainApiJar',
            ':mainJar',
            ':assemble',
            ':check',
            ':build',
            ':clojureWorks'
        ]
    }
}
