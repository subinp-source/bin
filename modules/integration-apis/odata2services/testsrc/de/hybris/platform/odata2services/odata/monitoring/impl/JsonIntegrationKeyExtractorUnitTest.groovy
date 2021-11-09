/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.monitoring.impl

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.olingo.odata2.api.commons.HttpContentType
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test
import spock.lang.Issue
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class JsonIntegrationKeyExtractorUnitTest extends Specification {
    private static final String INTEGRATION_KEY = 'integrationKeyReturnedFromOdataEntry'

    def extractor = new JsonIntegrationKeyExtractor()

    @Test
    @Unroll
    def "isApplicable = #res when response is #input"() {
        expect:
        extractor.isApplicable(input) == res

        where:
        input                                | res
        null                                 | false
        HttpContentType.APPLICATION_JSON     | true
        HttpContentType.APPLICATION_XML_UTF8 | false
    }

    @Test
    @Unroll
    def "extracts '#value' integration key value when response #condition"() {
        expect:
        extractor.extractIntegrationKey(response as String, status.statusCode) == value

        where:
        condition                                          | response                                                          | value           | status
        'body is null'                                     | null                                                              | ''              | HttpStatusCodes.CREATED
        'body is empty'                                    | ''                                                                | ''              | HttpStatusCodes.OK
        'contains it in the error/innererror element'      | errorResponseInputStream("\"innererror\": \"$INTEGRATION_KEY\",") | INTEGRATION_KEY | HttpStatusCodes.BAD_REQUEST
        'contains error but no innererror element'         | errorResponseInputStream('')                                      | ''              | HttpStatusCodes.INTERNAL_SERVER_ERROR
        'contains it in the /d/integrationKey element'     | responseInputStream("\"integrationKey\": \"$INTEGRATION_KEY\",")  | INTEGRATION_KEY | HttpStatusCodes.CREATED
        'contains entity but no /d/integrationKey element' | responseInputStream('')                                           | ''              | HttpStatusCodes.OK
    }

    @Test
    @Issue('https://jira.hybris.com/browse/STOUT-2541')
    @Unroll
    def "extracted #element element value is decoded"() {
        expect:
        extractor.extractIntegrationKey(response as String, status.statusCode) == 'men@work'

        where:
        element          | response                                                | status
        'innererror'     | errorResponseInputStream('"innererror": "men%40work",') | HttpStatusCodes.BAD_REQUEST
        'integrationKey' | responseInputStream('"integrationKey": "men%40work",')  | HttpStatusCodes.CREATED
    }

    private static String responseInputStream(final String integrationKeyElement) {
        """{
			"d": {
				$integrationKeyElement
				"name": "Trix",
				"description": "Trix are for kids",
				"code": "trix"
			}
		}"""
    }

    private static String errorResponseInputStream(final String integrationKeyElement) {
        """{
			"error": {
				$integrationKeyElement
				"code": " codeValue",
				"message": {
					"lang": "en",
					"value": "message"
				}
			}
		}"""
    }
}