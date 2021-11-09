package de.hybris.platform.inboundservices.interceptor

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel
import de.hybris.platform.apiregistryservices.model.EndpointModel
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import org.junit.Test

@IntegrationTest
class ExposedDestinationIntegrationAPIValidateInterceptorIntegrationTest extends ServicelayerTransactionalSpockSpecification
{
    private static final def INBOUND_PRODUCT_IO = "InboundProductIO"
    private static final def INBOUND_STOCK_LEVEL_IO = "InboundStockLevelIO"
    private static final def ENDPOINT_URL = "http://localhost:9002/endpoint"
    private static final def EXPOSED_DEST_URL = "http://does.not.matter"
    private static final def USERNAME = "user"
    private static final def PASSWORD = "pass"

    def setup() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject ;code[unique = true]',
                "                                       ;$INBOUND_PRODUCT_IO ",
                "                                       ;$INBOUND_STOCK_LEVEL_IO ",
                'INSERT_UPDATE InboundChannelConfiguration ;integrationObject(code)[unique = true] ;authenticationType(code)',
                "                                          ;$INBOUND_PRODUCT_IO                    ;BASIC",
                "                                          ;$INBOUND_STOCK_LEVEL_IO                ;BASIC",
                'INSERT_UPDATE BasicCredential ;id[unique=true] ;username   ;password',
                "                              ;basicCred       ;$USERNAME  ;$PASSWORD ",
                'INSERT_UPDATE DestinationTarget; id[unique=true]   ;destinationChannel(code); template',
                "                               ; target_1          ;DEFAULT                 ; true",
                "                               ; target_2          ;DEFAULT                 ; true",
                'INSERT_UPDATE Endpoint ;id[unique=true] ;version ;specUrl          ;specData ;name ;description',
                "                       ;e1              ;v1      ;$ENDPOINT_URL    ;s1       ;n1   ;des",
                'INSERT_UPDATE ExposedDestination ;id[unique=true] ;url                ;endpoint(id) ;destinationTarget(id) ;active ;credential(id) ;inboundChannelConfiguration(integrationObject(code))',
                "                                 ;dest            ;$EXPOSED_DEST_URL  ;e1           ;target_1              ;true   ;basicCred      ;$INBOUND_PRODUCT_IO"
        )
    }

    def cleanup() {
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll BasicCredentialModel
        IntegrationTestUtil.removeAll DestinationTargetModel
        IntegrationTestUtil.removeAll EndpointModel
        IntegrationTestUtil.removeAll ExposedDestinationModel
    }

    @Test
    def "Impex import case 1: throw exception"() {
        when: "create an ExposedDestination with same InboundChannelConfiguration exposed in the same TargetDestination"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ExposedDestination ;id[unique=true] ;url                  ;endpoint(id) ;destinationTarget(id) ;active ;credential(id) ;inboundChannelConfiguration(integrationObject(code))',
                "                                        ;dest_2          ;$EXPOSED_DEST_URL    ;e1           ;target_1              ;true   ;basicCred      ;$INBOUND_PRODUCT_IO"
        )

        then:
        def e = thrown(AssertionError)
        e.message.contains("has already been exposed in this DestinationTarget")
    }

    @Test
    def "Impex import case 2: no exception will be thrown" () {
        when: "create an ExposedDestination with same InboundChannelConfiguration but different TargetDestination"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ExposedDestination ;id[unique=true] ;url                  ;endpoint(id) ;destinationTarget(id) ;active ;credential(id) ;inboundChannelConfiguration(integrationObject(code))',
                "                                        ;dest_3          ;$EXPOSED_DEST_URL    ;e1           ;target_2              ;true   ;basicCred      ;$INBOUND_PRODUCT_IO"
        )

        then:
        noExceptionThrown()
    }

    @Test
    def "Impex import case 3: no exception will be thrown"() {
        when: "create an ExposedDestination with different InboundChannelConfiguration but same TargetDestination"
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ExposedDestination ;id[unique=true] ;url                  ;endpoint(id) ;destinationTarget(id) ;active ;credential(id) ;inboundChannelConfiguration(integrationObject(code))',
                "                                        ;dest_4          ;$EXPOSED_DEST_URL    ;e1           ;target_1              ;true   ;basicCred      ;$INBOUND_STOCK_LEVEL_IO"
        )

        then:
        noExceptionThrown()
    }
}
