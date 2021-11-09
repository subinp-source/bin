/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search

import de.hybris.bootstrap.annotations.UnitTest
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PaginationParametersUnitTest extends Specification {
    private static final def OFFSET = 6
    private static final def SIZE = 2

    @Test
    @Unroll
    def "offset is #offset when create() called with #param value"() {
        given:
        def pagination = PaginationParameters.create(param, 0)

        expect:
        pagination.pageStart == offset

        where:
        param | offset
        -3    | 0
        0     | 0
        5     | 5
    }

    @Test
    @Unroll
    def "page size is #size when create() called with #param value"() {
        given:
        def pagination = PaginationParameters.create(0, param)

        expect:
        pagination.pageSize == size

        where:
        param | size
        -1    | 0
        0     | 0
        1     | 1
    }

    @Test
    @Unroll
    def "equals for PaginationParameters{offset=6, pageSize=2} is #res when other item is #other"() {
        expect:
        PaginationParameters.create(OFFSET, SIZE).equals(other) == res

        where:
        other                                     | res
        null                                      | false
        'offset=6, pageSize=2'                    | false
        PaginationParameters.create(0, SIZE)      | false
        PaginationParameters.create(OFFSET, 0)    | false
        PaginationParameters.create(OFFSET, SIZE) | true
    }

    @Test
    @Unroll
    def "PaginationParameters{offset=6, pageSize=2}.hashCode() == PaginationParameters{offset=#offset, pageSize=#size}.hashCode() is #res"() {
        expect:
        PaginationParameters.create(OFFSET, SIZE).hashCode().equals(PaginationParameters.create(offset, size).hashCode()) == res

        where:
        offset | size | res
        0      | SIZE | false
        OFFSET | 0    | false
        OFFSET | SIZE | true
    }
}
