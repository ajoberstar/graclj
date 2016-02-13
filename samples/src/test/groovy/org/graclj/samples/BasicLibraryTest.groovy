package org.graclj.samples

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Paths

class BasicLibraryTest extends Specification {
    GradleRunner runner

    def setup() {
        runner = GradleRunner.create()
            .withProjectDir(Paths.get(System.properties['test.projects.root'], 'basic-library').toFile())
            .withGradleVersion(System.properties['test.gradle.version'])
    }

    def 'can publish to clojars'() {
        expect:
        runner
            .withArguments('clean', 'components', 'publishMainPublicationToClojars', '--stacktrace')
            .build()
    }

    def 'can execute tests'() {
        when:
        def result = runner
            .withArguments('clean', 'components', 'testMainJarBinaryTest', '--stacktrace')
            .buildAndFail()
        then:
        result.output =~ /3 tests completed, 1 failed/
    }
}
