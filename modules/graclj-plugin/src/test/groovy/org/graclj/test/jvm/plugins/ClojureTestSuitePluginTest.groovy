package org.graclj.test.jvm.plugins

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Paths

class ClojureTestSuitePluginTest extends Specification {
    private static final URI PLUGIN_REPO = Paths.get(System.properties['plugin.repo']).toUri()

    @Rule TemporaryFolder projectDir = new TemporaryFolder()

    def setup() {
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
apply plugin: 'org.graclj.clojure-test-suite'

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
    testSuites {
        test(JUnitTestSuiteSpec) {
            jUnitVersion '4.12'
            testing \$.components.main
        }
    }
}
"""
        projectDir.newFolder('src', 'main', 'clojure', 'sample')
        projectDir.newFile('src/main/clojure/sample/code.clj') << """
(ns sample.code
    (:require [clojure.string :as str]))

(defn my-sample [x] (str/reverse x))
"""

        projectDir.newFolder('src', 'test', 'clojure', 'sample')
        projectDir.newFile('src/test/clojure/sample/test.clj') << """
(ns sample.test
  (:require [clojure.test :refer :all]
            [sample.code :refer :all]))

(deftest my-sample-works
  (is (= "rac" (my-sample "car"))))

(deftest my-sample-fails
  (is (= "car" (my-sample "car"))))
"""
    }

    def 'executing all tests works'() {
        when: 'the test task is executed for all tests'
        def result = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withArguments('clean', 'testMainJarBinaryTest', '--stacktrace')
            .buildAndFail()
        then: 'then one test passes and one succeeds'
        result.tasks(TaskOutcome.FAILED)*.path == [':testMainJarBinaryTest']
        result.output =~ /2 tests completed, 1 failed/
    }

    def 'executing some tests works'() {
        when: 'the test task is executed for one test'
        def result = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withArguments('clean', 'testMainJarBinaryTest', '--tests', '*.my-sample-works', '--stacktrace')
            .build()
        then: 'then one test passes'
        result.task(':testMainJarBinaryTest').outcome == TaskOutcome.SUCCESS
    }
}
