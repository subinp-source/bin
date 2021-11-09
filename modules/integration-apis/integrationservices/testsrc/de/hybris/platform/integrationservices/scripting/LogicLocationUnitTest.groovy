/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.scripting

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class LogicLocationUnitTest extends Specification {

    @Test
    @Unroll
    def "a LogicLocation is created when the URI is '#uri'"() {
        given:
        def logicLocation = LogicLocation.from(uri)

        expect:
        with(logicLocation) {
            location == uri
            scheme == LogicLocationScheme.MODEL
        }

        where:
        uri << ['model://a', 'model://a/1', 'model:////#$(#SKDJF']
    }

    @Test
    @Unroll
    def "an exception is thrown when creating a LogicLocation with an invalid URI '#uri'"() {
        when:
        LogicLocation.from(uri)

        then:
        thrown CannotCreateLogicLocationException

        where:
        uri << [null, '', 'classpath://abc', 'model', 'model:/', 'model://', 'model:// ']
    }

    @Test
    @Unroll
    def "isValue is #valid when the uri is '#uri'"() {
        expect:
        LogicLocation.isValid(uri) == valid

        where:
        uri                    | valid
        null                   | false
        ''                     | false
        'abc'                  | false
        'file://123'           | false
        'model'                | false
        'model:'               | false
        'model:/'              | false
        'model://'             | false
        'model:// '            | false
        'model://   abc'       | false
        'model:///'            | true
        'model://&*@/(*@)(*@#' | true
        'model://a/1'          | true
        'model://a'            | true
        'MODEL://abc'          | true
        'MoDeL://abc'          | true
    }

    @Test
    @Unroll
    def "two LogicLocations are equal when #condition"() {
        expect:
        location1 == location2

        where:
        condition      | location1                                    | location2
        'same object'  | LogicLocation.from('model://someLocation') | location1
        'same content' | LogicLocation.from('model://someLocation') | LogicLocation.from('model://someLocation')
    }

    @Test
    @Unroll
    def "two LogicLocations are not equal when the second location #condition"() {
        given:
        def location1 = LogicLocation.from('model://someLocation')

        expect:
        location1 != location2

        where:
        condition               | location2
        'is null'               | null
        'has different content' | LogicLocation.from('model://someOtherLocation')
        'is another type'       | new Integer(1)

    }

    @Test
    @Unroll
    def "two LogicLocations have the same hashcode when #condition"() {
        expect:
        location1.hashCode() == location2.hashCode()

        where:
        condition      | location1                                    | location2
        'same object'  | LogicLocation.from('model://someLocation') | location1
        'same content' | LogicLocation.from('model://someLocation') | LogicLocation.from('model://someLocation')
    }

    @Test
    @Unroll
    def "two LogicLocations have different hashcodes when the second location #condition"() {
        given:
        def location1 = LogicLocation.from('model://someLocation')

        expect:
        location1.hashCode() != location2.hashCode()

        where:
        condition               | location2
        'has different content' | LogicLocation.from('model://someOtherLocation')
        'is another type'       | new Integer(1)
    }

    @Test
    def "LogicLocation toString contains the instantiated information"() {
        given:
        def aLocation = 'someLocation'
        def logicLocation = LogicLocation.from("model://$aLocation")

        expect:
        def str = logicLocation.toString()
        str.contains LogicLocationScheme.MODEL.name()
        str.contains aLocation
    }
}
