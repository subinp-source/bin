/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import de.hybris.platform.integrationservices.populator.ItemToMapConversionContext;
import de.hybris.platform.integrationservices.service.IntegrationObjectAndItemMismatchException;
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class DefaultPayloadBuildingRequestDecorator implements OutboundRequestDecorator
{
	private IntegrationObjectConversionService integrationObjectConversionService;

	@Override
	public HttpEntity<Map<String, Object>> decorate(final HttpHeaders httpHeaders, final Map<String, Object> payload,
			final DecoratorContext context, final DecoratorExecution execution)
	{
		final ItemToMapConversionContext conversionContext = toConversionContext(context);
		final Map<String, Object> map =	getIntegrationObjectConversionService().convert(conversionContext);
		payload.putAll(map);

		return execution.createHttpEntity(httpHeaders, payload, context);
	}

	private ItemToMapConversionContext toConversionContext(final DecoratorContext ctx)
	{
		return ctx.getIntegrationObjectItem()
		         .map(type -> new ItemToMapConversionContext(ctx.getItemModel(), type))
		         .orElseThrow(() -> new IntegrationObjectAndItemMismatchException(ctx.getItemModel(), ctx.getIntegrationObject()));
	}

	protected IntegrationObjectConversionService getIntegrationObjectConversionService()
	{
		return integrationObjectConversionService;
	}

	@Required
	public void setIntegrationObjectConversionService(final IntegrationObjectConversionService integrationObjectConversionService)
	{
		this.integrationObjectConversionService = integrationObjectConversionService;
	}
}
