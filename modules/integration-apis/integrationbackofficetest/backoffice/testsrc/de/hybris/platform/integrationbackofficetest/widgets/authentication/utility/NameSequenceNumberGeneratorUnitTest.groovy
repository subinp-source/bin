/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.authentication.utility

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationbackoffice.widgets.authentication.utility.impl.NameSequenceNumberGenerator
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class NameSequenceNumberGeneratorUnitTest extends Specification {
    private static final String IO_CODE = "iocode"
    private static final String IO_CODE2 = "ioCode500"

    def flexSearch = Stub(FlexibleSearchService)
    def occNamingConvention = new NameSequenceNumberGenerator(flexSearch)

    @Test
    @Unroll
    def "When endPointIDs are #listOfEndPointIDS and exposedDestinationIDs are #listOfExposedDestinationIDS then the suffix number is #expectedNo"() {
        given:
        flexSearch.search(_) >>> [getSearchResult(listOfEndPointIDS), getSearchResult(listOfExposedDestinationIDS)]

        when:
        int actualNo = occNamingConvention.getGeneratedNumber(IO_CODE)

        then:
        actualNo == expectedNo

        where:
        listOfEndPointIDS                              | listOfExposedDestinationIDS  | expectedNo
        []                                             | []                           | 0
        ["cc-iocode-metadata"]                         | []                           | 1
        []                                             | ["cc-iocode"]                | 1

        ["cc-iocode-metadata"]                         | ["cc-iocode"]                | 1
        ["cc-iocode-1-metadata"]                       | ["cc-iocode"]                | 2
        ["cc-iocode-metadata"]                         | ["cc-iocode-1"]              | 2

        ["cc-iocode-1-metadata"]                       | ["cc-iocode-1"]              | 2
        ["cc-iocode-55-metadata"]                      | ["cc-iocode-55"]             | 56

        ["cc-iocode-1-metadata"]                       | ["cc-iocode-55"]             | 56
        ["cc-iocode-55-metadata"]                      | ["cc-iocode-1"]              | 56

        ["cc-IOCODE-metadata"]                         | ["cc-IOCODE"]                | 0
        ["cc-IOCODE-5-metadata"]                       | ["cc-iocode"]                | 1
        ["cc-iocode-metadata"]                         | ["cc-IOCODE-5"]              | 1

        ["cc-iocode-metadata", "cc-iocode-5-metadata"] | []                           | 6
        []                                             | ["cc-iocode", "cc-iocode-5"] | 6
        ["cc-iocode-metadata", "cc-iocode-5-metadata"] | ["cc-iocode-55"]             | 56

    }

    @Test
    @Unroll
    def "When endPointIDs are #listOfEndPointIDS and exposedDestinationIDs are #listOfExposedDestinationIDS and IOid contains number then the suffix number is #expectedNo"() {
        given:
        flexSearch.search(_) >>> [getSearchResult(listOfEndPointIDS), getSearchResult(listOfExposedDestinationIDS)]

        when:
        int actualNo = occNamingConvention.getGeneratedNumber(IO_CODE2)

        then:
        actualNo == expectedNo

        where:
        listOfEndPointIDS                                      | listOfExposedDestinationIDS            | expectedNo
        ["cc-iocode500-metadata", "cc-iocode500-5-metadata"]   | ["cc-iocode500"]                       | 6
        ["cc-iocode500-metadata", "cc-iocode500-1-metadata"]   | ["cc-iocode500-55", "cc-iocode500-32"] | 56
        ["cc-iocode500-metadata", "cc-iocode500-200-metadata"] | ["cc-iocode500-101"]                   | 201
    }

    @Test
    @Unroll
    def "When endPointIDs is #listOfEndPointIDS and exposedDestinationIDs is #listOfExposedDestinationIDS and ioName is #ioName, then the suffix number is #expectedNo. Testing method stripOutNonDigits."() {
        given:
        flexSearch.search(_) >>> [getSearchResult(listOfEndPointIDS), getSearchResult(listOfExposedDestinationIDS)]

        when:
        int actualNo = occNamingConvention.getGeneratedNumber(ioName)

        then:
        actualNo == expectedNo

        where:
        listOfEndPointIDS              | listOfExposedDestinationIDS | ioName           | expectedNo
        []                             | ["cc-1234-10"]              | "1234"           | 11
        ["cc-1234-10-metadata"]        | []                          | "1234"           | 11
        []                             | ["cc-1234-10"]              | "1234-10"        | 1
        ["cc-1234-10-metadata"]        | []                          | "1234-10"        | 1
        []                             | ["cc-1234-123-7"]           | "1234-123"       | 8
        ["cc-1234-123-7-metadata"]     | []                          | "1234-123"       | 8
        []                             | ["cc-1234-123-10"]          | "1234-123-10"    | 1
        ["cc-1234-123-10-metadata"]    | []                          | "1234-123-10"    | 1
        []                             | ["cc-somename-10"]          | "someName"       | 11
        ["cc-somename-10-metadata"]    | []                          | "someName"       | 11
        []                             | ["cc-somename-10"]          | "someName-10"    | 1
        ["cc-somename-10-metadata"]    | []                          | "someName-10"    | 1
        []                             | ["cc-somename10-10"]        | "someName10-10"  | 1
        ["cc-somename10-10-metadata"]  | []                          | "someName10-10"  | 1
        []                             | ["cc-price-test4-99"]       | "Price-Test4"    | 100
        ["cc-price-test4-99-metadata"] | []                          | "Price-Test4"    | 100
        []                             | ["cc-price-test4-99"]       | "Price-Test4-99" | 1
        ["cc-price-test4-99-metadata"] | []                          | "Price-Test4-99" | 1

    }

    def getSearchResult(listOfID) {
        Stub(SearchResult) {
            getResult() >> listOfID
        }
    }
}
