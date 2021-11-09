/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.IntegrationObjectItemModelBuilder
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.IntegrationObjectModelBuilder.integrationObject
import static de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil.findIntegrationObjectItemByCodeAndIntegrationObject
import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.handleRequest
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.postRequestBuilder
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class IntegrationServicePersistenceIntegrationTest extends ServicelayerSpockSpecification {
    private static final SERVICE_NAME = "IntegrationService"
    private static final ITEMS_ENTITY_SET = "IntegrationObjectItems"
    private static final OBJECT_CODE = "MyIntegrationObjectTest"

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setup() {
        // for IntegrationApi
        importData("/impex/essentialdata-integrationservices.impex", "UTF-8")
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    def "Successfully create item as root when no root item exists on the same IO"() {
        given:
        final IntegrationObjectModel objectModel = integrationObject()
                .withCode(OBJECT_CODE)
                .build()

        and:
        IntegrationObjectItemModelBuilder
                .integrationObjectItem()
                .withCode("Product")
                .withIntegrationObject(objectModel)
                .build()

        and:
        final ODataRequest request = postRequest(ITEMS_ENTITY_SET, rootItem('Category'))

        when:
        final ODataResponse response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.CREATED
        findIntegrationObjectItemByCodeAndIntegrationObject('Category', objectModel)?.root
    }

    @Test
    def "Fails to create item as root when a root item already exists on the same IO"() {
        given:
        final IntegrationObjectModel objectModel = integrationObject()
                .withCode(OBJECT_CODE)
                .build()

        and:
        IntegrationObjectItemModelBuilder
                .integrationObjectItem()
                .withCode("Product")
                .asRoot()
                .withIntegrationObject(objectModel)
                .build()

        final ODataRequest request = postRequest(ITEMS_ENTITY_SET, rootItem('Category'))

        when:
        final ODataResponse response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        !findIntegrationObjectItemByCodeAndIntegrationObject('Category', objectModel)
    }

    @Test
    def "Change an existing root item to not root"() {
        given:
        final String itemCode = "Category"

        and:
        final IntegrationObjectModel objectModel = integrationObject()
                .withCode(OBJECT_CODE)
                .build()
        and:
        IntegrationObjectItemModelBuilder
                .integrationObjectItem()
                .withCode(itemCode)
                .asRoot()
                .withIntegrationObject(objectModel)
                .build()

        and:
        final ODataRequest request = postRequest(ITEMS_ENTITY_SET, item(itemCode))

        when:
        def response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.CREATED
        !findIntegrationObjectItemByCodeAndIntegrationObject(itemCode, objectModel)?.root
    }

    @Test
    def "Successfully send same root item twice"() {
        given:
        final String itemCode = "Category"

        and:
        final IntegrationObjectModel objectModel = integrationObject()
                .withCode(OBJECT_CODE)
                .build()
        and:
        IntegrationObjectItemModelBuilder
                .integrationObjectItem()
                .withCode(itemCode)
                .asRoot()
                .withIntegrationObject(objectModel)
                .build()

        and:
        final ODataRequest request = postRequest(ITEMS_ENTITY_SET, rootItem(itemCode))

        when:
        def response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.CREATED
        findIntegrationObjectItemByCodeAndIntegrationObject(itemCode, objectModel)?.root
    }

    @Test
    def "Change existing item to root when no root item exists on the same IO"() {
        given:
        final String itemCode = "Category"

        and:
        final IntegrationObjectModel objectModel = integrationObject()
                .withCode(OBJECT_CODE)
                .build()
        and:
        IntegrationObjectItemModelBuilder
                .integrationObjectItem()
                .withCode(itemCode)
                .withIntegrationObject(objectModel)
                .build()

        and:
        def request = postRequest(ITEMS_ENTITY_SET, rootItem(itemCode))

        when:
        def response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.CREATED
        findIntegrationObjectItemByCodeAndIntegrationObject(itemCode, objectModel)?.root
    }

    @Test
    def "Create IntegrationObject with multiple items where only 1 item as root"() {
        given:
        final ODataRequest request = postRequest("IntegrationObjects", json()
                .withCode(OBJECT_CODE)
                .withFieldValues("items",
                        rootItem("Product"),
                        item("Catalog")))

        when:
        final ODataResponse response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.CREATED
        def objectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(OBJECT_CODE)
        objectModel.items.size() == 2
    }

    @Test
    def "Create IntegrationObject with multiple items where there are multiple roots"() {
        given:
        def request = postRequest("IntegrationObjects", json()
                .withCode(OBJECT_CODE)
                .withFieldValues("items",
                        rootItem("Product"),
                        rootItem("Catalog")))

        when:
        def response = handleRequest(facade, request)

        then:
        response.status == HttpStatusCodes.BAD_REQUEST
        !IntegrationObjectTestUtil.findIntegrationObjectByCode(OBJECT_CODE)
    }

    @Test
    @Issue('https://cxjira.sap.com/browse/IAPI-4076')
    def 'Creates attributes from the second occurrence of the item in payload'() {
        given: 'the IO payload contains Customer item twice and the second occurrence has attribute(s)'
        def payload = json()
                .withCode(OBJECT_CODE)
                .withFieldValues("items",
                        item('Customer'),
                        item('Customer').withFieldValues('attributes', uniqueAttribute('Customer', 'uid')))

        when: "the payload is sent to $SERVICE_NAME"
        def response = handleRequest facade, postRequest('IntegrationObjects', payload)

        then: 'request is successful'
        response.status == HttpStatusCodes.CREATED
        and: 'the created IO contains attribute(s) of the second item occurrence'
        IntegrationTestUtil.findAny(IntegrationObjectItemAttributeModel, {
            it.attributeName == 'uid' && it.integrationObjectItem.integrationObject.code == OBJECT_CODE
        }).present
    }

    private static ODataRequest postRequest(final String entitySet, final JsonBuilder requestBody) {
        postRequestBuilder(SERVICE_NAME, entitySet, APPLICATION_JSON_VALUE)
                .withAcceptLanguage(Locale.ENGLISH)
                .withBody(requestBody)
                .build()
    }

    private static JsonBuilder item(String itemCode) {
        json()
                .withCode(itemCode)
                .withField("type", json().withCode(itemCode))
                .withField("root", false)
                .withField("integrationObject", json().withCode(OBJECT_CODE))
    }

    private static JsonBuilder rootItem(String itemCode) {
        json()
                .withCode(itemCode)
                .withField("type", json().withCode(itemCode))
                .withField("root", true)
                .withField("integrationObject", json().withCode(OBJECT_CODE))
    }

    private static JsonBuilder uniqueAttribute(String type, String name) {
        attribute(type, name).withField('unique', true)
    }

    private static JsonBuilder attribute(String type, String name) {
        json()
                .withField('attributeName', name)
                .withField('attributeDescriptor', json()
                        .withField('qualifier', name)
                        .withField('enclosingType', json().withCode(type)))
    }
}
