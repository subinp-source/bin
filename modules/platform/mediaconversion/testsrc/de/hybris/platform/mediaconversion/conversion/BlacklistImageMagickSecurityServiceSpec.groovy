/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.conversion

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class BlacklistImageMagickSecurityServiceSpec extends Specification {

    private ImageMagickSecurityService service = new StubImageMagickSecurityService()

    class StubImageMagickSecurityService extends ImageMagickSecurityService {

        @Override
        String getCommandsFromConfig(final String configName) {
            return "foo,bar,baz,boom"
        }

        @Override
        String getValidationType() {
            return "blacklist"
        }
    };

    @Test
    @Unroll
    def "test valid command"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        result.isSecure()

        where:
        command                                                       | _
        ""                                                            | _
        " "                                                           | _
        "nice command, it ain't gonna offend anyone ;)"               | _
        "-nice +command, -it +ain't -gonna +offend -anyone ;)"        | _
        "-nice +command, -it +ain't -gonna +offend -anyone ;)"        | _
        "foo bar baz boom it's ok as long as not prefixed as command" | _
    }

    @Test
    @Unroll
    def "test invalid command"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        !result.isSecure()

        where:
        command                            | _
        "something -foo really -bar wrong" | _
        "something +foo really -bar wrong" | _
        "something -foo really +bar wrong" | _
        "something +foo really +bar wrong" | _
    }

    @Test
    @Unroll
    def "test valid command list"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        result.isSecure()

        where:
        command                                                                   | _
        [" "]                                                                     | _
        []                                                                        | _
        ["nice", "command", "it", "ain't", "gonna", "offend", "anyone ;)"]        | _
        ["-nice", "+command", "-it", "+ain't", "*gonna", " offend", "0anyone ;)"] | _
        ["-222", "+&&"]                                                           | _
        ["something", "foo", "not", "so", "bar", "bad"]                           | _
        ["something", "*foo", "not", "so", "*bar", "bad"]                         | _
    }

    @Test
    @Unroll
    def "test invalid command list"() {
        when:
        def result = service.isCommandSecure(command)
        println(command)

        then:
        !result.isSecure()

        where:
        command                                               | _
        ["something", "-foo", "really", "-bar", "wrong"]      | _
        ["something", "+foo", "really", "+bar", "wrong"]      | _
        ["something", "+bazinga", "really", "-boom", "wrong"] | _
    }
}
