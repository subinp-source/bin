/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons.v2.spock

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest
import groovyx.net.http.HttpResponseDecorator
import org.junit.Before
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_OK

@ManualTest
@Unroll
class ExtendedBaseSitesTest extends AbstractSpockFlowTest {
	@Before
	public void ignoreIf() {
		org.junit.Assume.assumeTrue(Config.getBoolean("ycommercewebservicestest.enableAccTest", false))
	}

	/*
	This test is checking for presence of cmsoccaddon which is responsible for displaying urlPatters field.
	So far we don't have possibility to check if acceleratorwebserviceaddon is installed. This addons is responsible for field: urlEncodingAttributes
	Those two fields have to be initialized for junit_cmssite table by file: /commercewebservices-module/ycommercewebservicestest/resources/ycommercewebservicestest/import/coredata/stores/wsTest/site.impex
	*/

	def "Client retrieves a base sites with fields equals full: #format"() {
		when:
		HttpResponseDecorator response = restClient.get(
				path: getBasePath() + '/basesites',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				contentType: format)

		then:
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			// line below should be tested by cmsoccaddon
			//data.baseSites.any { it.urlPatterns != null }
			// line below should be tested by acceleratorwebserviceaddon
			data.baseSites.any { it.urlEncodingAttributes != null }
		}

		where:
		format << [JSON, XML]
	}

	def "Client retrieves express checkout information from basesites store: #format"() {
		when:
		HttpResponseDecorator response = restClient.get(path: getBasePath() + '/basesites',
				query: ['fields': FIELD_SET_LEVEL_FULL],
				contentType: format
		)

		then: "retrieves base site with store, which contains information about an express checkout"
		with(response) {
			if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
			status == SC_OK
			isNotEmpty(data.baseSites)
			data.baseSites.every { baseSite ->
				if (isNotEmpty(baseSite.stores)) {
					return baseSite.stores.every { baseStore ->
						return baseStore.expressCheckoutEnabled != null
					}
				}
				return false
			}
		}
		where:
		format << [JSON, XML]
	}
}
