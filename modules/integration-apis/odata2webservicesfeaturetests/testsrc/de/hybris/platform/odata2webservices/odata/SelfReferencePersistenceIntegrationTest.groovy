/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext

@IntegrationTest
class SelfReferencePersistenceIntegrationTest extends ServicelayerSpockSpecification {
    private static final String OBJECT = 'SelfReference'

    @Resource(name = "defaultODataFacade")
    ODataFacade facade

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "; $OBJECT",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique=true]; code[unique = true]; type(code)',
                "                                   ; $OBJECT                             ; TestItem           ; TestIntegrationItem",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code)',
                "; $OBJECT:TestItem ; code       ; TestIntegrationItem:code",
                "; $OBJECT:TestItem ; otherItem  ; TestIntegrationItem:otherItem ; $OBJECT:TestItem")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll TestIntegrationItemModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def "can persist an item referencing itself"() {
        given:
        def payload = json()
                .withCode('I1')
                .withField('otherItem', json().withCode('I1'))
        def request = postRequest().withBody(payload)

        when:
        def response = facade.handleRequest(createContext(request))

        then:
        response.status == HttpStatusCodes.CREATED
        def persistedItem = retrieveItem('I1')
        persistedItem?.otherItem == persistedItem
    }

    ODataRequestBuilder postRequest() {
        def pathInfoBuilder = PathInfoBuilder.pathInfo()
                .withServiceName(OBJECT)
                .withEntitySet('TestItems')
        ODataRequestBuilder.oDataPostRequest()
                .withPathInfo(pathInfoBuilder)
                .withContentType('application/json')
                .withAccepts('application/json')
    }

    TestIntegrationItemModel retrieveItem(def code) {
        IntegrationTestUtil.findAny(TestIntegrationItemModel, { it.code == code })
                .orElse(null)
    }
}
