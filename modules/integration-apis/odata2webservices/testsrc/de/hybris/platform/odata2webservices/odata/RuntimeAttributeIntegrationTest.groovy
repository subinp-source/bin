/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.integrationservices.util.RuntimeAttributeDescriptorBuilder
import de.hybris.platform.odata2services.odata.ODataContextGenerator
import de.hybris.platform.odata2webservices.odata.builders.ODataRequestBuilder
import de.hybris.platform.odata2webservices.odata.builders.PathInfoBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.AutoCleanup
import spock.lang.Unroll

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class RuntimeAttributeIntegrationTest extends ServicelayerSpockSpecification {
    private static final String TEST_NAME = "RuntimeAttributesModeling"
    private static final String CATALOG_TYPE = "Catalog"
    private static final String ENTITY_NAME = "AttributeDescriptors"
    private static final String QUALIFIER = "testRuntimeAttribute$TEST_NAME"
    private static final String SERVICE_NAME = "RuntimeAttributeService"
    private static final String STRING_TYPE = "java.lang.String"

    @AutoCleanup("cleanup")
    def runtimeAttributeDescriptorBuilder = RuntimeAttributeDescriptorBuilder.attributeDescriptor()
            .withQualifier(QUALIFIER)
            .withEnclosingType(CATALOG_TYPE)
            .withOptional(true)
            .withLocalized(false)
            .withPartOf(false)
            .withGenerate(false)
            .withUnique(false)

    @Resource(name = 'oDataContextGenerator')
    private ODataContextGenerator contextGenerator
    @Resource(name = "defaultODataFacade")
    private ODataFacade facade

    def setupSpec() {
        importCsv '/impex/essentialdata-runtimeattributes.impex', 'UTF-8'
    }


    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { icc -> icc?.integrationObject?.code == SERVICE_NAME}
        IntegrationTestUtil.remove IntegrationObjectModel, { io -> io.code == SERVICE_NAME}
    }

    @Test
    @Unroll
    def "POSTing creates optional primitive RuntimeAttribute of #primitiveType.simpleName type"() {
        given: 'a payload with runtime attribute descriptor of type #primitiveType'
        def body = json()
                .withField("qualifier", primitiveType.simpleName)
                .withField("enclosingType", json().withCode(CATALOG_TYPE))
                .withField("attributeType", json().withCode(primitiveType.name))
                .withField("optional", true)
                .withField("localized", false)
                .withField("partOf", false)
                .withField("generate", false)
                .build()

        when: 'runtime attribute descriptor is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest oDataPostContext(ENTITY_NAME, body)

        then: 'the attribute is created'
        response.getStatus() == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.qualifier") == primitiveType.getSimpleName()
        and: 'the attribute exists in the type system'
        def attribute = typeSystemAttribute(CATALOG_TYPE, primitiveType.simpleName)
        attribute.present
        and: 'the type system attribute has correct type and optional values'
        with(attribute.get()) {
            optional
            attributeType.code == primitiveType.name
        }

        cleanup:
        attribute.ifPresent { IntegrationTestUtil.remove(it) }

        where:
        primitiveType << [String, Integer, Float, Double, Boolean, Date]
    }

    @Test
    def 'POSTing creates optional composed RuntimeAttribute'() {
        given: 'a payload with runtime attribute descriptor of type Country'
        def runtimeQualifier = "catalogCountry$TEST_NAME"
        def runtimeAttributeType = "Country"
        def body = json()
                .withField("qualifier", runtimeQualifier)
                .withField("enclosingType", json().withCode(CATALOG_TYPE))
                .withField("attributeType", json().withCode(runtimeAttributeType))
                .withField("optional", true)
                .withField("localized", false)
                .withField("partOf", false)
                .withField("generate", false)
                .build()

        when: 'runtime attribute descriptor is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest oDataPostContext(ENTITY_NAME, body)

        then: 'attribute is created'
        response.getStatus() == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.qualifier") == runtimeQualifier
        and: 'the attribute exists in the type system'
        def attribute = typeSystemAttribute(CATALOG_TYPE, runtimeQualifier)
        attribute.present
        and: 'the type system attribute has correct type and optional values'
        with(attribute.get()) {
            optional
            !primitive
            attributeType.code == runtimeAttributeType
        }

        cleanup:
        attribute.ifPresent { IntegrationTestUtil.remove(it) }
    }

    @Test
    def "POSTing creates localized primitive RuntimeAttribute"() {
        given: 'a payload with localized runtime attribute descriptor'
        def runtimeAttributeQualifier = "localized$TEST_NAME"
        def runtimeAttributeType = String.name
        def body = json()
                .withField("qualifier", runtimeAttributeQualifier)
                .withField("enclosingType", json().withCode(CATALOG_TYPE))
                .withField("attributeType", json().withCode("localized:$runtimeAttributeType"))
                .withField("optional", true)
                .withField("localized", true)
                .withField("partOf", false)
                .withField("generate", false)
                .build()

        when: 'runtime attribute descriptor is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest oDataPostContext(ENTITY_NAME, body)

        then: 'the attribute is created'
        response.getStatus() == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.qualifier") == runtimeAttributeQualifier
        and: 'the attribute exists in the type system'
        def attribute = typeSystemAttribute(CATALOG_TYPE, runtimeAttributeQualifier)
        attribute.present
        and: 'the type system attribute has correct type and localized values'
        with(attribute.get()) {
            localized
            attributeType.code == "localized:$runtimeAttributeType"
        }

        cleanup:
        attribute.ifPresent { IntegrationTestUtil.remove it }
    }

    @Test
    @Unroll
    def "POSTing creates unique #kind RuntimeAttribute"() {
        given: 'a payload with unique runtime attribute descriptor'
        def runtimeAttributeQualifier = "uniqueRuntimeAttribute"
        def body = json()
                .withField("qualifier", runtimeAttributeQualifier)
                .withField("enclosingType", json().withCode(CATALOG_TYPE))
                .withField("attributeType", json().withCode(type))
                .withField("optional", true)
                .withField("localized", false)
                .withField("partOf", false)
                .withField("generate", false)
                .withField("unique", true)
                .build()

        when: 'runtime attribute descriptor is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest oDataPostContext(ENTITY_NAME, body)

        then:
        response.getStatus() == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.qualifier") == runtimeAttributeQualifier
        and: 'the attribute exists in the type system'
        def attribute = typeSystemAttribute(CATALOG_TYPE, runtimeAttributeQualifier)
        attribute.present
        and: 'the type system attribute has correct type and unique values'
        with(attribute.get()) {
            unique
            attributeType.code == type
        }

        cleanup:
        attribute.ifPresent { IntegrationTestUtil.remove it }

        where:
        kind        | type
        'primitive' | String.name
        'composed'  | 'Country'
    }

    @Test
    def "POSTing a different attributeType for an existing Attribute Descriptor throws a 400 error"() {
        given: 'a runtime attribute descriptor of type String'
        def uniqueRuntimeAttributeQualifier = QUALIFIER
        def newRuntimeAttributeType = "java.lang.Integer"

        runtimeAttributeDescriptorBuilder.attributeDescriptor()
                .withQualifier(QUALIFIER)
                .withEnclosingType(CATALOG_TYPE)
                .withAttributeType(STRING_TYPE)
                .setup()

        def body = json()
                .withField("qualifier", uniqueRuntimeAttributeQualifier)
                .withField("enclosingType", json().withCode(CATALOG_TYPE))
                .withField("attributeType", json().withCode(newRuntimeAttributeType))
                .withField("optional", true)
                .withField("localized", false)
                .withField("partOf", false)
                .withField("generate", false)
                .build()

        when: 'same runtime attribute descriptor with different attribute type is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest(oDataPostContext(ENTITY_NAME, body))

        then:
        response.getStatus() == HttpStatusCodes.BAD_REQUEST
        def json = JsonObject.createFrom response.getEntityAsStream()
        json.getString("error.innererror") == "${QUALIFIER}|${CATALOG_TYPE}"
        json.getString("error.code") == "invalid_attribute_value"

        cleanup:
        IntegrationTestUtil.removeSafely(AttributeDescriptorModel) { it.qualifier == QUALIFIER }
    }

    @Test
    def "POSTing an Attribute Descriptor with same definition as a type system attribute is successful"() {
        given: 'a runtime attribute descriptor of type String'
        def attribute = IntegrationTestUtil.findAny(AttributeDescriptorModel) {
            it.qualifier == 'name' && it.enclosingType.code == 'Catalog' && it.attributeType.code == "localized:java.lang.String"
        }.orElse(null)
        def body = json()
                .withField("qualifier", attribute.qualifier)
                .withField("enclosingType", json().withCode(attribute.enclosingType.code))
                .withField("attributeType", json().withCode(attribute.attributeType.code))
                .withField("optional", true)
                .withField("localized", true)
                .withField("partOf", false)
                .withField("generate", false)
                .build()

        when: 'runtime attribute descriptor with same definition is POSTed to the Runtime Attributes IO'
        def response = facade.handleRequest(oDataPostContext(ENTITY_NAME, body))

        then:
        response.getStatus() == HttpStatusCodes.CREATED
        def json = JsonObject.createFrom response.getEntityAsStream()
        json.getString("d.qualifier") == attribute.qualifier

        cleanup:
        IntegrationTestUtil.removeSafely(AttributeDescriptorModel) { it.qualifier == QUALIFIER }
    }

    @Test
    def "GET request to RuntimeAttribute IO returns created Runtime Attribute Descriptors"() {
        given: 'a runtime attribute descriptor of type String'
        runtimeAttributeDescriptorBuilder.attributeDescriptor()
                .withQualifier(QUALIFIER)
                .withEnclosingType(CATALOG_TYPE)
                .withAttributeType(STRING_TYPE)
                .setup()

        def context = oDataGetContext("AttributeDescriptors", [$filter: "qualifier eq '$QUALIFIER'" as String])

        when: 'GET request to RuntimeAttribute IO is executed'
        ODataResponse response = facade.handleRequest context

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.results[0].qualifier") == QUALIFIER
    }

    @Test
    def "PATCH request to existing Runtime Attribute Descriptor modifies its previous value"() {
        given: 'an optional runtime attribute descriptor exists'
        runtimeAttributeDescriptorBuilder.attributeDescriptor()
                .withQualifier(QUALIFIER)
                .withEnclosingType(CATALOG_TYPE)
                .withAttributeType(STRING_TYPE)
                .withOptional(true)
                .setup()
        and: 'the payload overrides optional to false'
        def body = json().withField("optional", false).build()

        when: 'PATCH request is executed to Runtime Attributes IO'
        ODataResponse response = facade.handleRequest oDataPatchContext("$QUALIFIER|$CATALOG_TYPE", body)

        then:
        response.getStatus() == HttpStatusCodes.OK
        def json = JsonObject.createFrom response.entityAsStream
        json.getString("d.qualifier") == QUALIFIER
        and: 'the attribute has changed to required in the type system'
        !typeSystemAttribute(CATALOG_TYPE, QUALIFIER).map({ it.optional }).orElse(true)

        cleanup:
        IntegrationTestUtil.removeSafely(AttributeDescriptorModel) { it.qualifier == QUALIFIER }
    }

    @Test
    def "DELETE request to existing Runtime Attribute Descriptor deletes it"() {
        given: 'a runtime attribute descriptor exists'
        runtimeAttributeDescriptorBuilder.attributeDescriptor()
                .withQualifier(QUALIFIER)
                .withEnclosingType(CATALOG_TYPE)
                .withAttributeType(STRING_TYPE)
                .setup()

        when: 'DELETE request is executed to Runtime Attributes IO'
        ODataResponse response = facade.handleRequest oDataDeleteContext("${QUALIFIER}|${CATALOG_TYPE}")

        then: 'the attribute is deleted'
        response.getStatus() == HttpStatusCodes.OK
        and: 'does not exist in the type system'
        typeSystemAttribute(CATALOG_TYPE, QUALIFIER).empty
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

    ODataContext oDataGetContext(String entitySetName, String key) {
        def request = ODataRequestBuilder.oDataGetRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(entitySetName)
                        .withEntityKeys(key))
                .build()

        contextGenerator.generate request
    }

    ODataContext oDataPostContext(String entitySetName, String content) {
        def request = ODataFacadeTestUtils
                .oDataPostRequest(SERVICE_NAME, entitySetName, content, APPLICATION_JSON_VALUE)

        contextGenerator.generate request
    }

    ODataContext oDataPatchContext(String key, String body) {
        def request = ODataRequestBuilder.oDataPatchRequest()
                .withAccepts(APPLICATION_JSON_VALUE)
                .withContentType(APPLICATION_JSON_VALUE)
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_NAME)
                        .withEntityKeys(key))
                .withBody(body)
                .build()

        contextGenerator.generate request
    }

    ODataContext oDataDeleteContext(String key) {
        def request = ODataRequestBuilder.oDataDeleteRequest()
                .withPathInfo(PathInfoBuilder.pathInfo()
                        .withServiceName(SERVICE_NAME)
                        .withEntitySet(ENTITY_NAME)
                        .withEntityKeys(key))
                .withAccepts(APPLICATION_JSON_VALUE)
                .build()
        contextGenerator.generate request
    }

    private static Optional<AttributeDescriptorModel> typeSystemAttribute(String type, String attr) {
        IntegrationTestUtil.findAny(AttributeDescriptorModel) {
            it.qualifier == attr && it.enclosingType.code == type
        } as Optional<AttributeDescriptorModel>
    }
}