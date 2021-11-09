package de.hybris.platform.odata2webservices.update

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.inboundservices.config.DefaultInboundServicesConfiguration
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test

import javax.annotation.Resource

@IntegrationTest
class InboundChannelConfigurationSystemUpdateIntegrationTest extends ServicelayerSpockSpecification {
    private static final String IO_TEST = "IO_TEST_1"

    @Resource
    InboundChannelConfigurationSystemUpdater inboundChannelConfigurationSystemUpdater
    @Resource(name = "defaultInboundServicesConfiguration")
    DefaultInboundServicesConfiguration inboundServicesConfiguration
    boolean savedLegacyFlag

    def setup() {
        savedLegacyFlag = inboundServicesConfiguration.isLegacySecurity()
    }

    def cleanup() {
        setLegacyModeFlag(savedLegacyFlag)
        IntegrationTestUtil.removeAll InboundChannelConfigurationModel
        IntegrationTestUtil.remove IntegrationObjectModel, { io -> (io.code == IO_TEST) }
    }

    def setLegacyModeFlag(boolean b) {
        inboundServicesConfiguration.setLegacySecurity(b)
    }

    @Test
    def "inbound channel config is created for an IO that does not have one"() {
        given:
        def ioTestModel_1 = IntegrationObjectTestUtil.createIntegrationObject(IO_TEST)
        and:
        setLegacyModeFlag(false)

        when:
        inboundChannelConfigurationSystemUpdater.associateIntegrationObjectWithInboundConfigChannel()

        then:
        def iccTestModel_1 = IntegrationObjectTestUtil.findInboundChannelConfigurationObject(ioTestModel_1.getCode())
        iccTestModel_1 != null
        iccTestModel_1.getAuthenticationType() == AuthenticationType.BASIC
    }

    @Test
    def "inbound channel config with assigned IO are not modified"() {
        given:
        def ioTestModel_1 = IntegrationObjectTestUtil.createIntegrationObject(IO_TEST)
        IntegrationObjectTestUtil.createInboundChannelConfigurationModel(ioTestModel_1, AuthenticationType.OAUTH)
        and:
        setLegacyModeFlag(false)

        when:
        inboundChannelConfigurationSystemUpdater.associateIntegrationObjectWithInboundConfigChannel()

        then:
        def iccTestModel_1 = IntegrationObjectTestUtil.findInboundChannelConfigurationObject(ioTestModel_1.getCode())
        iccTestModel_1.getIntegrationObject() == ioTestModel_1
        iccTestModel_1.getAuthenticationType() == AuthenticationType.OAUTH
    }

    @Test
    def "non-inbound IO's are not assigned to an inbound channel config"() {
        given:
        def ioTestModel_1 = IntegrationObjectTestUtil.createNullIntegrationTypeIntegrationObject(IO_TEST)
        and:
        setLegacyModeFlag(false)

        when:
        inboundChannelConfigurationSystemUpdater.associateIntegrationObjectWithInboundConfigChannel()

        then: 'inbound channel creation is skipped'
        IntegrationObjectTestUtil.findInboundChannelConfigurationObject(ioTestModel_1.getCode()) == null
    }
}
