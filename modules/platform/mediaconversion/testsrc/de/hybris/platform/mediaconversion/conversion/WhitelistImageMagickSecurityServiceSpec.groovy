/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.conversion

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class WhitelistImageMagickSecurityServiceSpec extends Specification {

    private ImageMagickSecurityService service = new StubImageMagickSecurityService()

    class StubImageMagickSecurityService extends ImageMagickSecurityService {

        @Override
        String getCommandsFromConfig(final String configName) {
            return "ab,cd,ef,gh"
        }

        @Override
        String getValidationType() {
            return "whitelist"
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
        command                                                    | _
        "-ab"                                                      | _
        "-cd"                                                      | _
        "-ef"                                                      | _
        "-gh"                                                      | _
        "+ab"                                                      | _
        "+cd"                                                      | _
        "+ef"                                                      | _
        "+gh"                                                      | _
        ""                                                         | _
        " "                                                        | _
        " 44 55 aa "                                               | _
        "       "                                                  | _
        "no commands"                                              | _
        "*no_command too"                                          | _
        "convert -ab sth +ab sth -ef sth+sth +gh sth-sth file.jpg" | _
    }

    @Test
    @Unroll
    def "test invalid command"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        !result.isSecure()

        where:
        command                                                     | _
        "-AB"                                                       | _
        "-CD"                                                       | _
        "-EF"                                                       | _
        "-GH"                                                       | _
        "+AB"                                                       | _
        "+CD"                                                       | _
        "+EF"                                                       | _
        "+GH"                                                       | _
        "+abc"                                                      | _
        "-11"                                                       | _
        "-"                                                         | _
        "   -   sth"                                                | _
        "convert -ab sth +def file.jpg"                             | _
        "convert -abc sth +ab sth -ef sth+sth +gh sth-sth file.jpg" | _
    }

    @Test
    @Unroll
    def "test valid command list"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        result.isSecure()

        where:
        command                                                                                 | _
        ["-ab"]                                                                                 | _
        ["-cd"]                                                                                 | _
        ["-ef"]                                                                                 | _
        ["-gh"]                                                                                 | _
        ["+ab"]                                                                                 | _
        ["+cd"]                                                                                 | _
        ["+ef"]                                                                                 | _
        ["+gh"]                                                                                 | _
        ["asasa"]                                                                               | _
        ["a---_++++sasa"]                                                                       | _
        ["99999"]                                                                               | _
        ["99aaa999---"]                                                                         | _
        ["99aaa999--+"]                                                                         | _
        ["-ab", "+cd", "-ef", "+gh"]                                                            | _
        ["+ab", "-cd", "+ef", "-gh"]                                                            | _
        ["no", "commands"]                                                                      | _
        ["*no", "command", "too"]                                                               | _
        ["-ab", "valid", "-ef", "commands"]                                                     | _
        [" "]                                                                                   | _
        []                                                                                      | _
        ["      "]                                                                              | _
        ["convert", "-ab", "sth", "+ab", "sth", "-ef", "sth+sth", "+gh", "sth-sth", "file.jpg"] | _
    }

    @Test
    @Unroll
    def "test invalid command list"() {
        when:
        def result = service.isCommandSecure(command)

        then:
        !result.isSecure()

        where:
        command                                                                                         | _
        ["-AB"]                                                                                         | _
        ["-CD"]                                                                                         | _
        ["-EF"]                                                                                         | _
        ["-GH"]                                                                                         | _
        ["+AB"]                                                                                         | _
        ["+CD"]                                                                                         | _
        ["+EF"]                                                                                         | _
        ["+GH"]                                                                                         | _
        ["-"]                                                                                           | _
        ["-99999"]                                                                                      | _
        ["+99999"]                                                                                      | _
        ["-99aaa999---"]                                                                                | _
        ["+99aaa999---"]                                                                                | _
        ["-99aaa999--+"]                                                                                | _
        ["+99aaa999--+"]                                                                                | _
        ["-ab", "+cd", "-ef", "+invalid"]                                                               | _
        ["+ab", "-", "+ef", "-gh"]                                                                      | _
        ["some", "+commands"]                                                                           | _
        ["*some", "commands", "-too"]                                                                   | _
        ["-ab", "-invalid", "-ef", "commands"]                                                          | _
        [" - "]                                                                                         | _
        [" + "]                                                                                         | _
        ["  - +    "]                                                                                   | _
        ["convert", "-ab", "sth", "+ab", "sth", "-ef", "sth+sth", "+gh", "-aaa", "sth-sth", "file.jpg"] | _
    }
}
