/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.facade.impl;

import de.hybris.platform.odata2webservices.facade.IntegrationDataFacade;
import de.hybris.platform.odata2webservices.odata.ODataFacade;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;

/**
 * Default implementation of the {@link IntegrationDataFacade}
 */
public class DefaultIntegrationDataFacade implements IntegrationDataFacade
{
	private ODataFacade oDataFacade;
	private Converter<ODataResponse, ResponseEntity<String>> oDataResponseToResponseEntityConverter;
	private Converter<HttpServletRequest, ODataContext> httpServletRequestToODataContextConverter;

	@Override
	public ResponseEntity<String> convertAndHandleRequest(final HttpServletRequest request)
	{
		final ODataContext requestContext = httpServletRequestToODataContextConverter.convert(request);
		final ODataResponse oDataResponse = oDataFacade.handleRequest(requestContext);
		return oDataResponseToResponseEntityConverter.convert(oDataResponse);
	}

	@Override
	public ResponseEntity<String> convertAndHandleSchemaRequest(final HttpServletRequest request)
	{
		final ODataContext requestContext = httpServletRequestToODataContextConverter.convert(request);
		final ODataResponse oDataResponse = oDataFacade.handleGetSchema(requestContext);
		return oDataResponseToResponseEntityConverter.convert(oDataResponse);
	}

	/**
	 * Sets the oDataFacade for {@link DefaultIntegrationDataFacade}
	 *
	 * @param oDataFacade the oDataFacade that is set
	 */
	@Required
	public void setoDataFacade(final ODataFacade oDataFacade)
	{
		this.oDataFacade = oDataFacade;
	}

	/**
	 * Sets the {@code Converter} to be used by this facade
	 *
	 * @param oDataResponseToResponseEntityConverter the converter that is set
	 */
	@Required
	public void setoDataResponseToResponseEntityConverter(
			final Converter<ODataResponse, ResponseEntity<String>> oDataResponseToResponseEntityConverter)
	{
		this.oDataResponseToResponseEntityConverter = oDataResponseToResponseEntityConverter;
	}

	@Required
	public void setHttpServletRequestToODataContextConverter(
			final Converter<HttpServletRequest, ODataContext> httpServletRequestToODataContextConverter)
	{
		this.httpServletRequestToODataContextConverter = httpServletRequestToODataContextConverter;
	}
}
