/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.europe1.model.PriceRowModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataIntegrationKeyValueIntegrationTest extends ServicelayerSpockSpecification {
    private static final def SERVICE_NAME = 'IntegrationService'

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade
    @Resource(name = "oDataSchemaGenerator")
    private SchemaGenerator generator

    def setupIntegrationService() {
        importCsv("/impex/essentialdata-integrationservices.impex", "UTF-8")
    }

    def setupPriceRow() {
        importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
                "; TestPriceRow; INBOUND",

                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code)",
                "; TestPriceRow; PriceRow; PriceRow; true; ALL_SUB_AND_SUPER_TYPES",

                "INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
                "; TestPriceRow:PriceRow; creationtime; PriceRow:creationtime;              ; true",
                "; TestPriceRow:PriceRow; price       ; PriceRow:price       ;              ; true"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == SERVICE_NAME }
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == SERVICE_NAME }
    }

    @Test
    @Issue('https://cxjira.sap.com/browse/IAPI-3263')
    def "GET IntegrationObjectItemAttributes include the expected integrationKeys"() {
        given: "A large page size to make sure the first page will include all attributes that we expect"
        setupIntegrationService()
        def params = ['\$top': '1000']
        def context = oDataGetContext("IntegrationObjectItemAttributes", params)

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        json.getCollectionOfObjects("d.results").size() == 45
        json.getCollectionOfObjects('d.results[*].integrationKey').containsAll(
                'integrationObject|InboundChannelConfiguration|IntegrationService',
                'authenticationType|InboundChannelConfiguration|IntegrationService',
                'code|AuthenticationType|IntegrationService',
                'code|IntegrationObject|IntegrationService',
                'integrationType|IntegrationObject|IntegrationService',
                'items|IntegrationObject|IntegrationService',
                'code|IntegrationObjectItem|IntegrationService',
                'type|IntegrationObjectItem|IntegrationService',
                'itemTypeMatch|IntegrationObjectItem|IntegrationService',
                'integrationObject|IntegrationObjectItem|IntegrationService',
                'root|IntegrationObjectItem|IntegrationService',
                'attributes|IntegrationObjectItem|IntegrationService',
                'classificationAttributes|IntegrationObjectItem|IntegrationService',
                'virtualAttributes|IntegrationObjectItem|IntegrationService',
                'attributeName|IntegrationObjectItemAttribute|IntegrationService',
                'returnIntegrationObjectItem|IntegrationObjectItemAttribute|IntegrationService',
                'attributeDescriptor|IntegrationObjectItemAttribute|IntegrationService',
                'unique|IntegrationObjectItemAttribute|IntegrationService',
                'autoCreate|IntegrationObjectItemAttribute|IntegrationService',
                'integrationObjectItem|IntegrationObjectItemAttribute|IntegrationService',
                'attributeName|IntegrationObjectItemClassificationAttribute|IntegrationService',
                'classAttributeAssignment|IntegrationObjectItemClassificationAttribute|IntegrationService',
                'integrationObjectItem|IntegrationObjectItemClassificationAttribute|IntegrationService',
                'returnIntegrationObjectItem|IntegrationObjectItemClassificationAttribute|IntegrationService',
                'attributeName|IntegrationObjectItemVirtualAttribute|IntegrationService',
                'integrationObjectItem|IntegrationObjectItemVirtualAttribute|IntegrationService',
                'retrievalDescriptor|IntegrationObjectItemVirtualAttribute|IntegrationService',
                'code|IntegrationObjectVirtualAttributeDescriptor|IntegrationService',
                'logicLocation|IntegrationObjectVirtualAttributeDescriptor|IntegrationService',
                'type|IntegrationObjectVirtualAttributeDescriptor|IntegrationService',
                'id|ClassificationSystem|IntegrationService',
                'catalog|ClassificationSystemVersion|IntegrationService',
                'version|ClassificationSystemVersion|IntegrationService',
                'classificationClass|ClassAttributeAssignment|IntegrationService',
                'classificationAttribute|ClassAttributeAssignment|IntegrationService',
                'code|ClassificationClass|IntegrationService',
                'catalogVersion|ClassificationClass|IntegrationService',
                'code|ClassificationAttribute|IntegrationService',
                'systemVersion|ClassificationAttribute|IntegrationService',
                'qualifier|AttributeDescriptor|IntegrationService',
                'enclosingType|AttributeDescriptor|IntegrationService',
                'code|ComposedType|IntegrationService',
                'code|Type|IntegrationService',
                'code|IntegrationType|IntegrationService',
                'code|ItemTypeMatchEnum|IntegrationService'
        )
    }

    @Test
    def "Attribute of Calendar type could be transformed to String and used as a part of integrationKey."() {
        given:
        setupPriceRow()
        importImpEx(
                "INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]",
                "                  ;piecesTest           ;EA",

                "INSERT_UPDATE Currency;isocode[unique=true]",
                "                      ;USD",

                "INSERT_UPDATE PriceRow;price[unique=true];currency(isocode)[unique=true];unit(code)[default=piecesTest]",
                "                      ;1555             ;USD")

        def context = oDataContextForPriceRows(['$filter': "price eq 1555"])

        when:
        ODataResponse response = facade.handleRequest(context)

        then:
        def json = extractEntitiesFrom response
        def millisecondsOfCreationtime = json.getString("d.results[0].creationtime") =~ /Date\((.*)\)/
        def millisecondsOfIntegrationKey = json.getString("d.results[0].integrationKey") =~ /(.*)\|/
        millisecondsOfCreationtime[0][1] == millisecondsOfIntegrationKey[0][1]

        cleanup:
        IntegrationTestUtil.removeSafely UnitModel, { it.code == 'piecesTest' }
        IntegrationTestUtil.removeSafely PriceRowModel, { it.price == 1555 }
        IntegrationTestUtil.removeSafely CurrencyModel, { it.isocode == 'USD' }
    }

    ODataContext oDataGetContext(String entitySetName) {
        oDataGetContext(entitySetName, [:])
    }

    ODataContext oDataGetContext(String entitySetName, Map params) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(entitySetName))
                .withParameters(params)
                .build()

        contextGenerator.generate request
    }

    def extractEntitiesFrom(ODataResponse response) {
        extractBodyWithExpectedStatus(response, HttpStatusCodes.OK)
    }

    def extractBodyWithExpectedStatus(ODataResponse response, HttpStatusCodes expStatus) {
        assert response.getStatus() == expStatus
        JsonObject.createFrom response.getEntity() as InputStream
    }

    ODataContext oDataContextForPriceRows(Map params) {
        def request = requestBuilderForPriceRow(params, 'PriceRows').build()
        contextGenerator.generate request
    }

    def requestBuilderForPriceRow(Map params, String entitySetName) {
        ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withParameters(params)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName("TestPriceRow")
                        .withEntitySet(entitySetName))
    }
}
