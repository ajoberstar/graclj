package org.graclj.samples

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Paths

class BasicAppTest extends Specification {
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(Paths.get(System.properties['samples.projects.root'], 'basic-app').toFile())
            .withGradleVersion(System.properties['samples.gradle.version'])
    }

    def 'can execute main class'() {
        when:
        def result = runner
            .withArguments('clean', 'components', 'run', '--stacktrace')
            .build()
        then:
        result.output =~ /\(third second first\)/
    }
}
