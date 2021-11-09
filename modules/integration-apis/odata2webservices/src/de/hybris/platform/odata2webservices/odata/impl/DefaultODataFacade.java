/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.impl;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.ODATA_REQUEST;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.EdmxProviderValidator;
import de.hybris.platform.odata2services.odata.ODataWebException;
import de.hybris.platform.odata2services.odata.security.IntegrationObjectMetadataPermissionService;
import de.hybris.platform.odata2webservices.odata.DefaultIntegrationODataRequestHandler;
import de.hybris.platform.odata2webservices.odata.ODataFacade;

import org.apache.http.HttpStatus;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * A default implementation of the {@link ODataFacade}. This implementation delegates to the odata classes to derive the
 * ODataResponse for the request.
 */
public class DefaultODataFacade implements ODataFacade
{
	private static final Logger LOG = Log.getLogger(DefaultODataFacade.class);

	private ODataServiceFactory oDataServiceFactory;
	private EdmxProviderValidator edmxProviderValidator;
	private IntegrationObjectMetadataPermissionService integrationObjectMetadataPermissionService;

	@Override
	public ODataResponse handleGetSchema(final ODataContext oDataContext)
	{
		integrationObjectMetadataPermissionService.checkMetadataPermission(oDataContext);
		final ODataResponse oDataResponse = handleRequest(oDataContext);
		return validatedResponse(oDataResponse);
	}

	@Override
	public ODataResponse handleGetEntity(final ODataContext oDataContext)
	{
		return handleRequest(oDataContext);
	}

	@Override
	public ODataResponse handlePost(final ODataContext oDataContext)
	{
		return handleRequest(oDataContext);
	}

	@Override
	public ODataResponse handleRequest(final ODataContext oDataContext)
	{
		return getResponse(oDataContext);
	}

	private ODataResponse getResponse(final ODataContext oDataContext)
	{
		final ODataService oDataService = getODataService(oDataContext);
		return createRequestHandler(oDataContext, oDataService).handle(odataRequest(oDataContext));
	}

	protected ODataRequest odataRequest(final ODataContext oDataContext)
	{
		return (ODataRequest) oDataContext.getParameter(ODATA_REQUEST);
	}

	private static ODataService getODataService(final ODataContext oDataContext)
	{
		try
		{
			return oDataContext.getService();
		}
		catch (final ODataException e)
		{
			LOG.error("Cannot get ODataService from ODataContext: {}", oDataContext, e);
			throw new ODataWebException("Error while trying to get ODataService from the ODataContext.", e);
		}
	}

	protected DefaultIntegrationODataRequestHandler createRequestHandler(final ODataContext oDataContext,
	                                                                     final ODataService oDataService)
	{
		return DefaultIntegrationODataRequestHandler.createHandler(oDataServiceFactory, oDataService, oDataContext);
	}

	protected ODataResponse validatedResponse(final ODataResponse oDataResponse)
	{
		if (oDataResponse.getStatus().getStatusCode() < HttpStatus.SC_MULTIPLE_CHOICES)
		{
			getEdmxProviderValidator().validateResponse(oDataResponse);
		}
		return oDataResponse;
	}

	@Required
	public void setODataServiceFactory(final ODataServiceFactory oDataServiceFactory)
	{
		this.oDataServiceFactory = oDataServiceFactory;
	}

	protected EdmxProviderValidator getEdmxProviderValidator()
	{
		return edmxProviderValidator;
	}

	@Required
	public void setEdmxProviderValidator(final EdmxProviderValidator edmxProviderValidator)
	{
		this.edmxProviderValidator = edmxProviderValidator;
	}

	@Required
	public void setIntegrationObjectMetadataPermissionService(final IntegrationObjectMetadataPermissionService integrationObjectMetadataPermissionService)
	{
		this.integrationObjectMetadataPermissionService = integrationObjectMetadataPermissionService;
	}

}
