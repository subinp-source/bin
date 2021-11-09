/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.scripting

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LogicLocationSchemeUnitTest extends Specification {

    @Test
    def 'from returns an existing scheme'() {
        expect:
        LogicLocationScheme.from('model') == LogicLocationScheme.MODEL
    }

    @Test
    @Unroll
    def "from throws an exception when #scheme does not match any enumerations"() {
        when:
        LogicLocationScheme.from(unsupportedScheme)

        then:
        def e = thrown UnsupportedLogicLocationSchemeException
        e.message == "$unsupportedScheme is unsupported"

        where:
        unsupportedScheme << ['', null, 'unsupportedScheme']
    }
}
