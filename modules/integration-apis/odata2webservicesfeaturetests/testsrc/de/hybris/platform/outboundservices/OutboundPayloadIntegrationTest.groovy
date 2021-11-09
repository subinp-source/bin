/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices

import com.github.tomakehurst.wiremock.junit.WireMockRule
import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.odata2webservicesfeaturetests.model.TestIntegrationItemModel
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.util.OutboundMonitoringRule
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Rule
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import rx.observers.TestSubscriber
import spock.lang.Issue

import javax.annotation.Resource
import java.text.SimpleDateFormat

import static com.github.tomakehurst.wiremock.client.WireMock.containing
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath
import static com.github.tomakehurst.wiremock.client.WireMock.ok
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.client.WireMock.verify
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.findAny
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx
import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.removeAll
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder

@IntegrationTest
class OutboundPayloadIntegrationTest extends ServicelayerSpockSpecification {

    private static final def TEST_IO = 'OutboundPayloadTest'
    private static final def DESTINATION_ENDPOINT = "/odata2webservices/$TEST_IO/GenericItems"
    private static final def DESTINATION_ID = 'outboundpayloadtest'
    private static final def ITEM_CODE = 'OutboundPayloadTestProduct'

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .dynamicHttpsPort()
            .keystorePath("resources/devcerts/platform.jks")
            .keystorePassword('123456'))

    @Resource
    private OutboundServiceFacade outboundServiceFacade

    @Rule
    public OutboundMonitoringRule outboundMonitoringRule = OutboundMonitoringRule.disabled()

    private TestSubscriber<ResponseEntity<Map>> subscriber = new TestSubscriber<>()

    def setup() {
        consumedDestinationBuilder()
                .withId(DESTINATION_ID)
                .withUrl("https://localhost:${wireMockRule.httpsPort()}/$DESTINATION_ENDPOINT")
                .build()

        stubFor(post(urlEqualTo(DESTINATION_ENDPOINT)).willReturn(ok()))
    }

    def cleanup() {
        removeAll IntegrationObjectModel
        ConsumedDestinationBuilder.cleanup()
    }

    @Issue(['https://jira.hybris.com/browse/IAPI-3925', 'https://jira.hybris.com/browse/IAPI-3959', 'https://cxjira.sap.com/browse/IAPI-3960'])
    @Test
    def "primitives in a collection are wrapped in an object"() {
        def dateFormatter = new SimpleDateFormat('YYYY-MM-dd-HH:mm:ss')
        def dateString = dateFormatter.format(new Date())
        def enumValue = "POST"

        given: 'test integration item containing a collection of dates'
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $TEST_IO           ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]    ; type(code)',
                "                                   ; $TEST_IO                              ; TestIntegrationItem    ; TestIntegrationItem",
                "                                   ; $TEST_IO                              ; HttpMethod             ; HttpMethod",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:HttpMethod                                                ; code                         ; HttpMethod:code",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; code                         ; TestIntegrationItem:code",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; dateCollection               ; TestIntegrationItem:dateCollection",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; enumList                     ; TestIntegrationItem:enumList                      ;$TEST_IO:HttpMethod"
        )

        and: 'a TestIntegrationItem'
        importImpEx(
                'INSERT_UPDATE TestIntegrationItem; code[unique = true]; dateCollection[dateformat = YYYY-MM-dd-HH:mm:ss]; enumList(code)',
                "                                 ; $ITEM_CODE         ; $dateString                                     ; $enumValue")


        when:
        outboundServiceFacade.send(item(TestIntegrationItemModel, ITEM_CODE), TEST_IO, DESTINATION_ID).subscribe(subscriber)

        then: 'verify the primitive is wrapped in an object such as { "value": "xyz" } and enum as {"code":"abc"}'
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT))
                .withRequestBody(matchingJsonPath("\$.dateCollection[0][?(@.value == '/Date(${dateFormatter.parse(dateString).getTime()})/')]"))
                .withRequestBody(matchingJsonPath("\$.enumList[0][?(@.code == '$enumValue')]")))


        and: "observable contains response with OK HTTP status"
        subscriber.getOnNextEvents().get(0).getStatusCode() == HttpStatus.OK

        cleanup:
        removeAll TestIntegrationItemModel
    }

    @Issue('https://jira.hybris.com/browse/IAPI-3887')
    @Test
    def 'primitives are in the appropriate format in the payload'() {
        given: 'a TestIntegration IO with primitive attributes'
        importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "                               ; $TEST_IO           ; INBOUND",

                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $TEST_IO                              ; TestIntegrationItem            ; TestIntegrationItem",

                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; bigDecimal                  ; TestIntegrationItem:bigDecimal",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; string                      ; TestIntegrationItem:string",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveInteger            ; TestIntegrationItem:primitiveInteger",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveLong               ; TestIntegrationItem:primitiveLong",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveBoolean            ; TestIntegrationItem:primitiveBoolean",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveChar               ; TestIntegrationItem:primitiveChar",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; date                        ; TestIntegrationItem:date",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveDouble             ; TestIntegrationItem:primitiveDouble",
                "                                            ; $TEST_IO:TestIntegrationItem                                       ; primitiveFloat              ; TestIntegrationItem:primitiveFloat")

        and: 'a TestIntegrationItem'
        def bigDecimal = BigDecimal.valueOf(12121212.333333)
        def string = "i'm a string"
        def integer = 8675309
        def longVal = 1001010101001010L
        def boolVal = true
        def character = 'c'
        def dateFormatter = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
        def dateString = dateFormatter.format(new Date())
        def doubleVal = 242342.2343
        def floatVal = 5656565.8f

        importImpEx(
                "INSERT_UPDATE TestIntegrationItem; code[unique = true] ; bigDecimal ; string ; primitiveInteger ; primitiveLong    ; primitiveBoolean ; primitiveChar ; date[dateformat = 'yyyy-MM-dd HH:mm:ss'] ; primitiveDouble  ; primitiveFloat",
                "                                 ; $ITEM_CODE          ; $bigDecimal; $string; $integer         ; $longVal         ; $boolVal         ; $character    ; $dateString                              ; $doubleVal       ; $floatVal")

        when:
        outboundServiceFacade.send(item(TestIntegrationItemModel, ITEM_CODE), TEST_IO, DESTINATION_ID).subscribe(subscriber)

        then:
        verify(postRequestedFor(urlEqualTo(DESTINATION_ENDPOINT))
                .withRequestBody(matchingJsonPath('$.bigDecimal', containing('12121212.3')))
                .withRequestBody(matchingJsonPath('$.string', containing(string)))
                .withRequestBody(matchingJsonPath('$.primitiveInteger', containing("$integer")))
                .withRequestBody(matchingJsonPath('$.primitiveLong', containing("$longVal")))
                .withRequestBody(matchingJsonPath('$.primitiveBoolean', containing("$boolVal")))
                .withRequestBody(matchingJsonPath('$.primitiveChar', containing("$character")))
                .withRequestBody(matchingJsonPath('$.date', containing("/Date(${dateFormatter.parse(dateString).getTime()})/")))
                .withRequestBody(matchingJsonPath('$.primitiveDouble', containing("$doubleVal")))
                .withRequestBody(matchingJsonPath('$.primitiveFloat', containing('5656566.0'))))

        and: "observable contains response with OK HTTP status"
        subscriber.getOnNextEvents().get(0).getStatusCode() == HttpStatus.OK

        cleanup:
        removeAll TestIntegrationItemModel
    }

    def item(Class type, String code) {
        findAny(type, { code == it.code })
                .orElse(null)
    }
}
