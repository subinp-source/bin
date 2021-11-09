/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl.csrf

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import org.springframework.http.HttpHeaders
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class CsrfParametersUnitTest extends Specification {
    private static final String HEADER_CSRF = 'X-CSRF-Token'
    private static final String HEADER_COOKIE = 'Set-Cookie'

    @Test
    @Unroll
    def "throws exception when #field is #value"() {
        when:
        CsrfParameters.create headers(tokens as List, cookies as List)

        then:
        thrown IllegalArgumentException

        where:
        field     | value         | tokens        | cookies
        'token'   | 'not present' | null          | ['oats']
        'token'   | '[]'          | []            | ['chocolate chip']
        'token'   | '[null]'      | [null]        | ['butter']
        'token'   | '[ ]'         | [' ']         | ['banana']
        'token'   | '[ , "ring"]' | [' ', 'ring'] | ['danish']
        'cookies' | 'not present' | ['token']     | null
        'cookies' | '[null]'      | ['token']     | [null]
        'cookies' | '[]'          | ['ring']      | []
        'cookies' | '[ ]'         | ['ring']      | [' ']
    }

    @Test
    @Unroll
    def "CSRF token is #token when the header contains #csrf"() {
        given:
        def params = CsrfParameters.create headers(csrf as List, ['monster'])

        expect:
        params.csrfToken == token

        where:
        csrf              | token
        ['token']         | 'token'
        ['token', null]   | 'token'
        ['token', '']     | 'token'
        ['token', 'ring'] | 'token'
    }

    @Test
    @Unroll
    def "Cookie value is #cookies when the header contains #header"() {
        given:
        def params = CsrfParameters.create headers(['ring'], header as List)

        expect:
        params.csrfCookie == cookies

        where:
        header                | cookies
        ['oats']              | 'oats'
        [' ', 'mint']         | ' ; mint'
        ['oreo', '']          | 'oreo; '
        ['chocolate', 'chip'] | 'chocolate; chip'
    }

    @Test
    def 'CSRF token is immutable'() {
        given:
        def headers = headers('token', 'cookie')
        def params = CsrfParameters.create headers

        when:
        headers.remove HEADER_CSRF

        then:
        params.csrfToken == 'token'
    }

    @Test
    def 'CSRF cookies is immutable'() {
        given:
        def headers = headers('token', 'cookie')
        def params = CsrfParameters.create headers

        when:
        headers.remove HEADER_COOKIE

        then:
        params.csrfCookie == 'cookie'
    }

    @Test
    @Unroll
    def "not equal when other #condition"() {
        given:
        def sample = headers 'token', 'chocolate; mint'

        expect:
        sample != other

        where:
        condition                        | other
        'is null'                        | null
        'has different class'            | Stub(CsrfParameters)
        'has different token'            | headers(['other'], ['chocolate', 'mint'])
        'does not have a cookie'         | headers('token', 'chocolate')
        'has different cookies'          | headers(['token'], ['chocolate', 'chip'])
        'has cookies in different order' | headers(['token'], ['mint', 'chocolate'])
    }

    @Test
    def 'csrf parameters are equal when the token and cookies match'() {
        given:
        def sample = CsrfParameters.create headers(['one'], ['abc', 'xyz'])
        def other = CsrfParameters.create headers(['one', 'another'], ['abc', 'xyz'])

        expect:
        sample == other
        sample.hashCode() == other.hashCode()
    }

    @Test
    @Unroll
    def "hashCode not equal when other #condition"() {
        given:
        def sample = headers 'token', 'chocolate; mint'

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition                        | other
        'has different token'            | headers(['other'], ['chocolate', 'mint'])
        'does not have a cookie'         | headers('token', 'chocolate')
        'has different cookies'          | headers(['token'], ['chocolate', 'chip'])
        'has cookies in different order' | headers(['token'], ['mint', 'chocolate'])
    }

    private static HttpHeaders headers(String token, String cookie) {
        headers([token], [cookie])
    }

    private static HttpHeaders headers(List<String> tokens, List<String> cookies) {
        new HttpHeaders().tap {
            if (tokens != null) addAll HEADER_CSRF, tokens
            if (cookies != null) addAll HEADER_COOKIE, cookies
        }
    }
}
