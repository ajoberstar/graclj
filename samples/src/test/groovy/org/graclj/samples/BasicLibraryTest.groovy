package org.graclj.samples

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Paths

class BasicLibraryTest extends Specification {
    File projectDir = Paths.get(System.properties['projects.root'], 'basic-library').toFile()

    def 'can publish to clojars'() {
        expect:
        GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments('clean', 'components', 'publishMainPublicationToClojars', '--stacktrace')
            .build()
    }

    def 'can execute tests'() {
        when:
        def result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments('clean', 'components', 'testMainJarBinaryTest', '--stacktrace')
            .buildAndFail()
        then:
        result.output =~ /3 tests completed, 1 failed/
    }
}
