/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.url;

/**
 * Interface used to resolve a URL path for a parametrized type
 * 
 * @param <T>
 *           the type of the source item to resolve into a URL.
 */
public interface UrlResolver<T>
{
	/**
	 * Resolve the url path for the source type.
	 * 
	 * @param source
	 *           the source type.
	 * @return the URL path
	 */
	String resolve(T source);
}
