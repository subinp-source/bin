/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorContextErrorException;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * This decorator throws a {@link DecoratorContextErrorException} exception when errors are present in
 * the {@link DecoratorContext}.
 * By throwing the exception, the errors in the {@link DecoratorContext} will be populated with the exception message.
 */
public class DefaultContextErrorDecorator implements OutboundRequestDecorator
{
	@Override
	public HttpEntity<Map<String, Object>> decorate(final HttpHeaders httpHeaders, final Map<String, Object> payload,
	                                                final DecoratorContext context, final DecoratorExecution execution)
	{
		if (context.hasErrors())
		{
			throw new DecoratorContextErrorException(context);
		}
		return execution.createHttpEntity(httpHeaders, payload, context);
	}
}
