/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade.impl

import com.github.tomakehurst.wiremock.junit.WireMockRule
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.core.model.product.UnitModel
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.IntegrationAttributeProcessingException
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.scripting.model.ScriptModel
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.apache.http.HttpStatus
import org.junit.Rule
import org.junit.Test
import org.springframework.http.ResponseEntity
import rx.observers.TestSubscriber
import spock.lang.Issue

import javax.annotation.Resource

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder

@IntegrationTest
@Issue('https://jira.hybris.com/browse/IAPI-4067')
class OutboundServiceFacadeVirtualAttributesIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO = 'OutboundServiceFacadeVirtualAttributesIntegrationTestIO'
    private static final String UNIT_IOI_CODE = 'UnitItem'
    private static final String SCRIPT_CODE = 'logLoc'
    private static final String SCRIPT_LOCATION = "model://$SCRIPT_CODE"
    private static final String DESCRIPTOR_CODE = 'retrieveVirtualBatman'
    private static final String DESCRIPTOR_TYPE = 'java.lang.String'
    private static final String VIRTUAL_ATTR_NAME = 'formattedCode'
    private static final String DESTINATION_ENDPOINT = "/odata2webservices/$IO/UnitItems"
    private static final String DESTINATION_ID = 'outboundServiceVirtualAttributeDestId'
    private static final String UNIT_CODE = 'pieces'
    private static final UnitModel unit = new UnitModel(code: UNIT_CODE)

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))

    @Resource
    private OutboundServiceFacade outboundServiceFacade

    private TestSubscriber<ResponseEntity<Map>> subscriber = new TestSubscriber<>()

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root',
                "                                   ; $IO                                   ; $UNIT_IOI_CODE     ; Unit      ; true",
                '$item = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item             ; $attributeName ; $attributeDescriptor',
                "                                            ; $IO:$UNIT_IOI_CODE; code           ; Unit:code",
                'INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation   ; type(code)',
                "                                                         ; $DESCRIPTOR_CODE   ; $SCRIPT_LOCATION; $DESCRIPTOR_TYPE",
                '$item = integrationObjectItem(integrationObject(code), code)[unique = true]',
                'INSERT_UPDATE IntegrationObjectItemVirtualAttribute; $item[unique = true]; attributeName[unique = true]; retrievalDescriptor(code)',
                "                                                   ; $IO:$UNIT_IOI_CODE  ; $VIRTUAL_ATTR_NAME            ; $DESCRIPTOR_CODE       ")
    }

    def setup() {
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$DESTINATION_ENDPOINT")
                .build()
    }

    def cleanup() {
        IntegrationTestUtil.remove UnitModel, { it.code == UNIT_CODE }
        IntegrationTestUtil.remove ScriptModel, { it.code == SCRIPT_CODE }
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == IO }
        IntegrationTestUtil.remove IntegrationObjectVirtualAttributeDescriptorModel, { it.code == DESCRIPTOR_CODE }
    }

    @Test
    def "send a unit out to the destination with a virtual attribute that executes script logic to obtain the attribute value"() {
        given: "Stub response"
        stubFor(post(DESTINATION_ENDPOINT).willReturn(aResponse().withStatus(HttpStatus.SC_CREATED)))

        and: "A script with content with logic that formats the Unit.code"
        String content =
                '''\
                    "import de.hybris.platform.core.model.product.UnitModel
                     final UnitModel unit = (UnitModel) itemModel
                     unit.code.toUpperCase()"
                '''
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                "                    ; $SCRIPT_CODE       ; GROOVY          ; $content")

        and: "Unit virtual attribute changes"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]',
                "                  ;$UNIT_CODE       ;EA"
        )

        when: "a unit item is sent to the outbound facade"
        outboundServiceFacade.send(unit, IO, DESTINATION_ID).subscribe(subscriber)

        then: "destination server stub is called with a payload including the unit's standard and virtual attribute"
        verify(postRequestedFor(urlPathEqualTo(DESTINATION_ENDPOINT))
                .withRequestBody(matchCode(UNIT_CODE))
                .withRequestBody(matchVirtualAttribute(UNIT_CODE.toUpperCase())))

        and:
        subscriber.assertNoErrors()
    }

    @Test
    def "when the virtual attribute script encounters a non systemic error, the IntegrationAttributeProcessingException is thrown"() {
        given: "A groovy script that throws a RuntimeException is defined"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Script; code[unique = true]; scriptType(code); content',
                "                    ; $SCRIPT_CODE       ; GROOVY          ; throw new RuntimeException('Testing when script content throws exception')")

        and: "Unit virtual attribute changes"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]',
                "                  ;$UNIT_CODE       ;EA"
        )

        when: "a unit item is sent to the outbound facade"
        outboundServiceFacade.send(unit, IO, DESTINATION_ID).subscribe(subscriber)

        then:
        subscriber.assertError(IntegrationAttributeProcessingException)
    }

    @Test
    def "when the virtual attribute script encounters a systemic error, the IntegrationAttributeException is thrown"() {
        given: "Unit virtual attribute changes"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Unit;code[unique=true];unitType[unique=true]',
                "                  ;$UNIT_CODE       ;EA"
        )

        when:
        outboundServiceFacade.send(unit, IO, DESTINATION_ID).subscribe(subscriber)

        then:
        subscriber.assertError(IntegrationAttributeException)
    }

    def matchCode(String code) {
        matchingJsonPath("\$.[?(@.code == '$code')]")
    }

    def matchVirtualAttribute(String formattedCode) {
        matchingJsonPath("\$.[?(@.$VIRTUAL_ATTR_NAME == '$formattedCode')]")
    }
}
