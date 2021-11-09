/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.DefaultUrlRenderer
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import org.junit.Test
import org.zkoss.zul.impl.XulElement
import spock.lang.Specification

@UnitTest
class DefaultUrlRendererUnitTest extends Specification {

    private DefaultUrlRenderer defaultUrlRenderer = new DefaultUrlRenderer()
    private static final String IO_CODE = "ioCode"

    @Test
    def "check the correctness of the dynamically added URL column"() {
        given:
        def inboundChannelConfigurationModel = Stub(InboundChannelConfigurationModel) {
            getIntegrationObject() >> Stub(IntegrationObjectModel) {
                getCode() >> IO_CODE
            }
        }
        def xulElement = Spy(XulElement)

        when:
        defaultUrlRenderer.render(xulElement, null, inboundChannelConfigurationModel, null, null)

        then: 'The Div element should have a class'
        xulElement.children[0].sclass == "yw-compound-renderer-container"
        and: 'The Label element inside the Div element should have a value'
        xulElement.children[0].children[0].value == "https://<your-host-name>/odata2webservices/ioCode"
    }
}