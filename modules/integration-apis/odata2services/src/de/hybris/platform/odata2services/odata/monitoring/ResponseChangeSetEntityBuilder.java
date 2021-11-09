/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.monitoring;

import de.hybris.platform.inboundservices.model.InboundRequestErrorModel;
import de.hybris.platform.integrationservices.util.HttpStatus;

/**
 * A builder for {@link ResponseChangeSetEntity}
 */
public class ResponseChangeSetEntityBuilder
{
	private HttpStatus statusCode;
	private String integrationKey;
	private InboundRequestErrorModel requestError;

	public static ResponseChangeSetEntityBuilder responseChangeSetEntity()
	{
		return new ResponseChangeSetEntityBuilder();
	}

	public ResponseChangeSetEntityBuilder withStatusCode(final String code)
	{
		return withStatusCode(Integer.parseInt(code));
	}

	public ResponseChangeSetEntityBuilder withStatusCode(final int code)
	{
		statusCode = HttpStatus.valueOf(code);
		return this;
	}

	public ResponseChangeSetEntityBuilder withIntegrationKey(final String key)
	{
		integrationKey = key;
		return this;
	}

	public ResponseChangeSetEntityBuilder withRequestError(final InboundRequestErrorModel error)
	{
		requestError = error;
		return this;
	}

	public ResponseChangeSetEntity build()
	{
		return new ResponseChangeSetEntity(integrationKey, statusCode, requestError);
	}
}
