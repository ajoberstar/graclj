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
        classpath 'org.graclj:graclj:+'
    }
}

apply plugin: 'org.graclj.clojure-lang'

repositories {
  jcenter()
  mavenLocal()
}

import org.graclj.platform.jvm.*

model {
    components {
        main(ClojureJvmLibrarySpec)
    }
}

//class MyRules extends RuleSource {
//    @Mutate
//    void createTask(ModelMap<Task> tasks, @Path('tasks.mainJar') Task jar) {
//        tasks.create('clojureWorks', JavaExec) {
//            classpath jar
//            main = 'sample.yay'
//            args 'does', 'it', 'work'
//        }
//    }
//}

//apply plugin: MyRules
"""
        projectDir.newFolder('src', 'main', 'clojure', 'sample')
        projectDir.newFile('src/main/clojure/sample/yay.jvm') << """
(ns 'sample.yay'
    (:require [clojure.string :as str]))

(defn my-sample [x] (str/reverse x))

(defn -main [& args]
  (println (map my-sample args)))
"""


        when: 'the build task is executed'
        def result = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withArguments('build', '--stacktrace')
            .build()
        then: 'the expected tasks were executed'
        result.tasks*.path == [
            ':compileMainJarMainClojure',
            ':createMainJar',
            ':mainJar',
            ':assemble',
            ':check',
            ':build'
        ]
    }
}
