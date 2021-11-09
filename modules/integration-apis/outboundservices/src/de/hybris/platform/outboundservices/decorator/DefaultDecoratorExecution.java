/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

import java.util.Iterator;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class DefaultDecoratorExecution implements DecoratorExecution
{
	private final Iterator<OutboundRequestDecorator> iterator;

	/**
	 * Instantiates this decorator execution
	 *
	 * @param it an iterable containing the decorators to be applied to the outgoing request.
	 */
	public DefaultDecoratorExecution(final Iterable<OutboundRequestDecorator> it)
	{
		this(it.iterator());
	}

	/**
	 * Instantiates this decorator execution
	 *
	 * @param it an iterator of the decorators to be applied to the outgoing request.
	 */
	public DefaultDecoratorExecution(final Iterator<OutboundRequestDecorator> it)
	{
		iterator = it;
	}

	@Override
	public HttpEntity<Map<String, Object>> createHttpEntity(final HttpHeaders httpHeaders, final Map<String, Object> payload,
	                                                        final DecoratorContext context)
	{
		if (!iterator.hasNext())
		{
			return new HttpEntity<>(payload, httpHeaders);
		}

		final OutboundRequestDecorator decorator = iterator.next();
		return decorator.decorate(httpHeaders, payload, context, this);
	}
}
