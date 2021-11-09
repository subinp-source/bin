/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.monitoring;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.inboundservices.model.InboundRequestErrorModel;
import de.hybris.platform.inboundservices.model.InboundRequestMediaModel;
import de.hybris.platform.inboundservices.model.InboundRequestModel;
import de.hybris.platform.integrationservices.enums.HttpMethod;
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Builds an {@link InboundRequestModel}
 */
public class InboundRequestBuilder
{
	private RequestBatchEntity request;
	private ResponseChangeSetEntity response;
	private InboundRequestMediaModel media;
	private HttpMethod httpMethod;
	private UserModel user;
	private String sapPassport;

	private InboundRequestBuilder()
	{
		// prevent external instantiation
	}

	public static InboundRequestBuilder builder()
	{
		return new InboundRequestBuilder();
	}

	public InboundRequestBuilder withRequest(final RequestBatchEntity request)
	{
		this.request = request;
		return this;
	}

	public InboundRequestBuilder withResponse(final ResponseChangeSetEntity response)
	{
		this.response = response;
		return this;
	}

	public InboundRequestBuilder withMedia(final InboundRequestMediaModel media)
	{
		this.media = media;
		return this;
	}

	public InboundRequestBuilder withHttpMethod(final HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
		return this;
	}

	public InboundRequestBuilder withUser(final UserModel user)
	{
		this.user = user;
		return this;
	}

	public InboundRequestBuilder withSapPassport(final String sapPassport)
	{
		this.sapPassport = sapPassport;
		return this;
	}

	public InboundRequestModel build()
	{
		Preconditions.checkArgument(request != null, "RequestBatchEntity cannot be null");

		final InboundRequestModel inboundRequestModel = new InboundRequestModel();
		inboundRequestModel.setMessageId(request.getMessageId());
		inboundRequestModel.setType(request.getIntegrationObjectType());
		inboundRequestModel.setHttpMethod(httpMethod);
		inboundRequestModel.setPayload(media);
		inboundRequestModel.setIntegrationKey(integrationKey());
		inboundRequestModel.setErrors(errors());
		inboundRequestModel.setStatus(status());
		inboundRequestModel.setUser(user);
		inboundRequestModel.setSapPassport(sapPassport);
		return inboundRequestModel;
	}

	protected String integrationKey()
	{
		return response != null && StringUtils.isNotBlank(response.getIntegrationKey())
				? response.getIntegrationKey()
				: request.getIntegrationKey();
	}

	protected IntegrationRequestStatus status()
	{
		if (response != null)
		{
			return response.isSuccessful() ? IntegrationRequestStatus.SUCCESS : IntegrationRequestStatus.ERROR;
		}
		return null;
	}

	protected Set<InboundRequestErrorModel> errors()
	{
		if (response != null && response.getRequestError().isPresent())
		{
			return Collections.singleton(response.getRequestError().get());
		}
		return Collections.emptySet();
	}
}
