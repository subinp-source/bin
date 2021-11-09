/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata;

import static de.hybris.platform.odata2services.odata.monitoring.InboundRequestServiceParameter.inboundRequestServiceParameter;

import de.hybris.platform.inboundservices.config.InboundServicesConfiguration;
import de.hybris.platform.inboundservices.model.InboundRequestMediaModel;
import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.enums.HttpMethod;
import de.hybris.platform.integrationservices.service.MediaPersistenceService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.monitoring.InboundRequestService;
import de.hybris.platform.odata2services.odata.monitoring.InboundRequestServiceParameter;
import de.hybris.platform.odata2services.odata.monitoring.RequestBatchEntity;
import de.hybris.platform.odata2services.odata.monitoring.RequestBatchEntityExtractor;
import de.hybris.platform.odata2services.odata.monitoring.ResponseChangeSetEntity;
import de.hybris.platform.odata2services.odata.monitoring.ResponseEntityExtractor;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

/**
 * An implementation of {@link ODataFacade} for adding additional logic such as request logging and persistence of monitoring
 * objects before delegating to the default implementation of the facade.
 */
public class ODataFacadeMonitoringPersistenceProxy implements ODataFacade
{
	private static final Logger LOG = Log.getLogger(ODataFacadeMonitoringPersistenceProxy.class);

	private InboundServicesConfiguration inboundServicesConfiguration;
	private ODataFacade oDataFacade;
	private MediaPersistenceService mediaPersistenceService;
	private InboundRequestService inboundRequestService;
	private RequestBatchEntityExtractor requestEntityExtractor;
	private ResponseEntityExtractor responseEntityExtractor;
	private UserService userService;

	@Override
	public ODataResponse handlePost(final ODataContext context)
	{
		return handleRequest(context);
	}

	@Override
	public ODataResponse handleGetSchema(final ODataContext oDataContext)
	{
		logDebug(oDataContext);
		return getoDataFacade().handleGetSchema(oDataContext);
	}

	@Override
	public ODataResponse handleGetEntity(final ODataContext oDataContext)
	{
		return handleRequest(oDataContext);
	}

	@Override
	public ODataResponse handleRequest(final ODataContext oDataContext)
	{
		logDebug(oDataContext);
		final boolean isMonitoringEnabled = getInboundServicesConfiguration().isMonitoringEnabled();
		final boolean isGetMethod = "GET".equals(oDataContext.getHttpMethod());
		return isMonitoringEnabled && !isGetMethod
				? monitorAndGetResponse(oDataContext)
				: getResponse(oDataContext);
	}

	protected ODataResponse monitorAndGetResponse(final ODataContext oDataContext)
	{
		final List<RequestBatchEntity> requests = getRequestEntityExtractor().extractFrom(oDataContext);
		final ODataResponse oDataResponse = getResponse(oDataContext);
		final List<ResponseChangeSetEntity> responses = getResponseEntityExtractor().extractFrom(oDataResponse);
		final Map<RequestBatchEntity, ResponseChangeSetEntity> requestsAndResponses = toRequestAndResponseMap(requests,
				responses);
		final List<InputStream> contents = extractPayloadContents(requestsAndResponses);
		final List<InboundRequestMediaModel> persistedMedias = getMediaPersistenceService().persistMedias(contents,
				InboundRequestMediaModel.class);
		final HttpMethod httpMethod = extractHttpMethod(oDataContext);

		final InboundRequestServiceParameter param = inboundRequestServiceParameter()
				.withRequests(requests)
				.withResponses(responses)
				.withMedias(persistedMedias)
				.withHttpMethod(httpMethod)
				.withUserId(getUserService().getCurrentUser().getUid())
				.withSapPassport(oDataContext.getRequestHeader(IntegrationservicesConstants.SAP_PASSPORT_HEADER_NAME))
				.build();

		getInboundRequestService().register(param);
		return oDataResponse;
	}

	protected ODataResponse getResponse(final ODataContext oDataContext)
	{
		return getoDataFacade().handleRequest(oDataContext);
	}

	private Map<RequestBatchEntity, ResponseChangeSetEntity> toRequestAndResponseMap(final List<RequestBatchEntity> requests,
	                                                                                 final List<ResponseChangeSetEntity> responses)
	{
		final Map<RequestBatchEntity, ResponseChangeSetEntity> requestsAndResponses = new LinkedHashMap<>();
		IntStream.range(0, Math.min(requests.size(), responses.size()))
		         .forEach(i -> requestsAndResponses.put(requests.get(i), responses.get(i)));
		return requestsAndResponses;
	}

	private List<InputStream> extractPayloadContents(final Map<RequestBatchEntity, ResponseChangeSetEntity> requestsAndResponses)
	{
		final List<InputStream> contents = Lists.newArrayList();
		requestsAndResponses.forEach((request, response) -> {
			try (final InputStream payload = request.getContent())
			{
				final boolean retentionEnabled = response.isSuccessful() ?
						getInboundServicesConfiguration().isPayloadRetentionForSuccessEnabled() :
						getInboundServicesConfiguration().isPayloadRetentionForErrorEnabled();
				contents.add(retentionEnabled ? payload : null);
			}
			catch (final IOException e)
			{
				LOG.error("An exception occurred while getting content from request", e);
			}
		});
		return contents;
	}

	private HttpMethod extractHttpMethod(final ODataContext oDataContext)
	{
		return StringUtils.isBlank(oDataContext.getHttpMethod()) ? null : HttpMethod.valueOf(oDataContext.getHttpMethod());
	}

	private void logDebug(final ODataContext context)
	{
		if (LOG.isDebugEnabled())
		{
			try
			{
				LOG.debug("Processing {} {}", context.getHttpMethod(), context.getPathInfo().getRequestUri());
			}
			catch (final ODataException e)
			{
				/*
				 Do nothing - don't break the request handling because request cannot be logged.
				 The problem will manifest itself anyway
				*/
				LOG.trace("Exception while creating Debug log: ", e);
			}
		}
	}

	@Required
	public void setoDataFacade(final ODataFacade oDataFacade)
	{
		this.oDataFacade = oDataFacade;
	}

	protected ODataFacade getoDataFacade()
	{
		return oDataFacade;
	}

	@Required
	public void setMediaPersistenceService(final MediaPersistenceService mediaPersistenceServiceImpl)
	{
		this.mediaPersistenceService = mediaPersistenceServiceImpl;
	}

	protected MediaPersistenceService getMediaPersistenceService()
	{
		return mediaPersistenceService;
	}

	@Required
	public void setInboundRequestService(final InboundRequestService inboundRequestService)
	{
		this.inboundRequestService = inboundRequestService;
	}

	protected InboundRequestService getInboundRequestService()
	{
		return inboundRequestService;
	}

	@Required
	public void setInboundServicesConfiguration(final InboundServicesConfiguration inboundServicesConfiguration)
	{
		this.inboundServicesConfiguration = inboundServicesConfiguration;
	}

	protected InboundServicesConfiguration getInboundServicesConfiguration()
	{
		return inboundServicesConfiguration;
	}

	@Required
	public void setRequestEntityExtractor(final RequestBatchEntityExtractor extractor)
	{
		requestEntityExtractor = extractor;
	}

	protected RequestBatchEntityExtractor getRequestEntityExtractor()
	{
		return requestEntityExtractor;
	}

	@Required
	public void setResponseEntityExtractor(final ResponseEntityExtractor extractor)
	{
		responseEntityExtractor = extractor;
	}

	protected ResponseEntityExtractor getResponseEntityExtractor()
	{
		return responseEntityExtractor;
	}

	@Required
	public void setUserService(final UserService service)
	{
		userService = service;
	}

	private UserService getUserService()
	{
		return userService;
	}
}
