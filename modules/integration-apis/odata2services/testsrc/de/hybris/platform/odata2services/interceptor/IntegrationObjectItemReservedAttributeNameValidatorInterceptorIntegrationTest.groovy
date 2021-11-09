package de.hybris.platform.odata2services.interceptor

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test
import spock.lang.Unroll

@IntegrationTest
class IntegrationObjectItemReservedAttributeNameValidatorInterceptorIntegrationTest extends ServicelayerSpockSpecification{

    private static final def IO = 'StandardIO'

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $IO                                   ; SavedQuery         ; SavedQuery",
                "                                   ; $IO                                   ; Order              ; Order",
                "                                   ; $IO                                   ; Product            ; Product")
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
    }

    @Test
    @Unroll
    def "exception is thrown when attribute name is a reserved attribute #attrName"() {
        when:
        IntegrationTestUtil.importImpEx(
                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor',
                "                                             ; $IO:SavedQuery      ; $attrName                    ; SavedQuery:code",
        )

        then:
        def e = thrown AssertionError
        e.message.contains "[$attrName]"

        where:
        attrName << ['localizedAttributes', 'integrationKey']
    }
}
