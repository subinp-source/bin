/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.addons

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.markers.CollectOutputFromTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v1.BaseWSTest
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(CollectOutputFromTest.class)
@ManualTest
class AccProductTests extends BaseWSTest {

	final firstName = "Sven"
	final lastName = "Haiges"
	final titleCode = "dr"
	final public static password = "test"

	@Before
	public void ignoreIf() {
		org.junit.Assume.assumeTrue(Config.getBoolean("ycommercewebservicestest.enableAccTest", false)
				&& Config.getBoolean("ycommercewebservicestest.enableV1", false))
	}

	@Test
	@Category(AvoidCollectingOutputFromTest.class)
	void testSearchProductsFacetJSON() {
		def con = testUtil.getSecureConnection("/products?query=camera&sort=topRated", 'GET', 'JSON', HttpURLConnection.HTTP_OK)
		def response = testUtil.verifiedJSONSlurper(con, false, false)

		// Verify facets
		assert response.facets.size() > 0
		def facet = response.facets.find { it.name == 'Stores' }
		assert facet != null
		assert facet.multiSelect
		assert facet.visible

		facet = response.facets.find { it.name == 'Category' }
		assert facet != null
		assert !facet.multiSelect
		assert facet.category
		assert facet.visible
		assert facet.values.size() > 0
		assert facet.topValues.size() > 0
	}

	@Test
	void testSearchProductsFacetLocationJSON() {
		def uid = registerUserJSON()
		def access_token = testUtil.getAccessToken(uid, password);
		def postBody = "location=munich"

		def con = testUtil.getSecureConnection("/customers/current/location", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def cookie = con.getHeaderField('Set-Cookie')

		assert cookie: 'No cookie present, cannot keep session'

		def cookieNoPath = cookie.split(';')[0]

		con = testUtil.getSecureConnection("/products?query=camera&pageSize=5", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = testUtil.verifiedJSONSlurper(con)

		// Verify facets
		assert response.facets.size() > 0
		def facetIdx = response.facets.findIndexOf { it.name == 'Stores' }
		assert facetIdx > -1
		def valueIdx = response.facets[facetIdx].values.findIndexOf { it.name == 'WS-Sapporo Ana Hotel Sapporo' }
		assert valueIdx > -1
		assert response.facets[facetIdx].values[valueIdx].query == 'camera:relevance:availableInStores:WS-Sapporo+Ana+Hotel+Sapporo'
		assert response.facets[facetIdx].values[valueIdx].count > 0

		postBody = "location=tokio"

		testUtil.getSecureConnection("/customers/current/location", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		con = testUtil.getSecureConnection("/products?query=camera&pageSize=5", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = testUtil.verifiedJSONSlurper(con)

		// Verify facets
		assert response.facets.size() > 0
		facetIdx = response.facets.findIndexOf { it.name == 'Stores' }
		assert facetIdx > -1
		valueIdx = response.facets[facetIdx].values.findIndexOf { it.name == 'WS-Ichikawa' }
		assert valueIdx > -1
		assert response.facets[facetIdx].values[valueIdx].query == 'camera:relevance:availableInStores:WS-Ichikawa'
		assert response.facets[facetIdx].values[valueIdx].count > 0
	}

	def registerUserJSON() {
		def client_credentials_token = testUtil.getClientCredentialsToken()
		def randomUID = System.currentTimeMillis()
		def body = "login=${randomUID}@sven.de&password=${password}&firstName=${firstName}&lastName=${lastName}&titleCode=${titleCode}"
		def con = testUtil.getSecureConnection("/customers", 'POST', 'JSON', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		return "${randomUID}@sven.de"
	}
}
