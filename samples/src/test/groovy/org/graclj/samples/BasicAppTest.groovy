package org.graclj.samples

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Paths

class BasicAppTest extends Specification {
    File projectDir = Paths.get(System.properties['projects.root'], 'basic-app').toFile()

    def 'can execute main class'() {
        when:
        def result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments('clean', 'components', 'run', '--stacktrace')
            .build()
        then:
        result.output =~ /\(third second first\)/
    }
}
