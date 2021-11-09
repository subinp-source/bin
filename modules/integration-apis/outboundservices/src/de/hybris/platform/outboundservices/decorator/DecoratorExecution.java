/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

/**
 * Represents a Decorator execution step.
 */
public interface DecoratorExecution
{
	/**
	 * Calls the next decorator in the chain.
	 *
	 * @param httpHeaders The headers to be used for the outgoing request.
	 * @param payload     The payload used for the request
	 * @param context     Some extra information that can be used by decorators.
	 * @return An {@link HttpEntity} containing the result of the decoration.
	 */
	HttpEntity<Map<String, Object>> createHttpEntity(HttpHeaders httpHeaders, Map<String, Object> payload,
	                                                 DecoratorContext context);
}
