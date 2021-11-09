/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.facade.imp

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2webservices.facade.impl.DefaultIntegrationDataFacade
import de.hybris.platform.odata2webservices.odata.ODataFacade
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

@UnitTest
class DefaultIntegrationDataFacadeUnitTest extends Specification {

    def facade = new DefaultIntegrationDataFacade()
    def httpConvert = Stub(Converter)
    def odataFacade = Stub(ODataFacade)
    def odataResponseConvert = Stub(Converter)
    def request = Stub(HttpServletRequest)
    def odataContext = Stub(ODataContext)
    def odataResponse = Stub(ODataResponse)
    def responseEnity = Stub(ResponseEntity)


    def setup() {
        facade.setoDataFacade(odataFacade)
        facade.setoDataResponseToResponseEntityConverter(odataResponseConvert)
        facade.setHttpServletRequestToODataContextConverter(httpConvert)
    }

    @Test
    def "convert And Handle Request"() {
        given:

        httpConvert.convert(request) >> odataContext
        odataFacade.handleRequest(odataContext) >> odataResponse
        odataResponseConvert.convert(odataResponse) >> responseEnity

        when:
        def actualResult = facade.convertAndHandleRequest(request)

        then:
        responseEnity == actualResult
    }

    @Test
    def "Convert and Handle Schema Request"() {
        given:

        httpConvert.convert(request) >> odataContext
        odataFacade.handleGetSchema(odataContext) >> odataResponse
        odataResponseConvert.convert(odataResponse) >> responseEnity

        when:
        def actualResult = facade.convertAndHandleSchemaRequest(request)

        then:
        responseEnity == actualResult
    }

}
