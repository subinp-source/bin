/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservices.odata.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.EdmxProviderValidator
import de.hybris.platform.odata2services.odata.InvalidODataSchemaException
import de.hybris.platform.odata2services.odata.impl.DefaultServiceFactory
import de.hybris.platform.odata2services.odata.security.IntegrationObjectMetadataPermissionService
import de.hybris.platform.odata2webservices.odata.DefaultIntegrationODataRequestHandler
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.junit.Test
import spock.lang.Specification

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@UnitTest
class DefaultODataFacadeUnitTest extends Specification {

    def oDataContext = Stub(ODataContext) {
        getParameter(_) >> Stub(ODataRequest)
    }

    def edmxProviderValidator = Mock(EdmxProviderValidator)
    def serviceFactory = Stub(DefaultServiceFactory)
    def integrationObjectMetadataPermissionService = Stub(IntegrationObjectMetadataPermissionService)

    def successfulResponse = responseWithStatus(HttpStatusCodes.OK)
    def requestHandler = Stub(DefaultIntegrationODataRequestHandler)

    def facade = Spy(DefaultODataFacade) {
        createRequestHandler(_, _) >> requestHandler
    }

    def setup() {
        facade.setEdmxProviderValidator(edmxProviderValidator)
        facade.setODataServiceFactory(serviceFactory)
        facade.setIntegrationObjectMetadataPermissionService(integrationObjectMetadataPermissionService)
    }

    @Test
    def "getSchema delegates to request handler"() {
        given:
        requestHandlerRespondsWith(successfulResponse)

        when:
        def actual = facade.handleGetSchema(oDataContext)

        then:
        successfulResponse == actual
    }

    @Test
    def "getSchema ODataResponse failed validation"() {
        given:
        requestHandlerRespondsWith(successfulResponse)
        responseValidationFailure()

        when:
        facade.handleGetSchema(oDataContext)

        then:
        thrown(InvalidODataSchemaException)
    }

    @Test
    def "getSchema does not validate error response"() {
        given:
        requestHandlerRespondsWith(unsuccessfulResponse())

        when:
        facade.handleGetSchema(oDataContext)

        then:
        0 * edmxProviderValidator.validateResponse(_)
    }

    @Test
    def "handleRequest delegates to request handler"() {
        given:
        requestHandlerRespondsWith(successfulResponse)

        expect:
        facade.handleRequest(oDataContext) == successfulResponse
    }

    def responseValidationFailure() {
        edmxProviderValidator.validateResponse(_) >> { throw Stub(InvalidODataSchemaException) }
    }

    def requestHandlerRespondsWith(final ODataResponse response) {
        requestHandler.handle(_) >> response
    }

    def unsuccessfulResponse() {
        return responseWithStatus(HttpStatusCodes.BAD_REQUEST)
    }

    def responseWithStatus(final HttpStatusCodes status) {
        final ODataResponse response = mock(ODataResponse.class)
        doReturn(status).when(response).getStatus()
        return response
    }
}
