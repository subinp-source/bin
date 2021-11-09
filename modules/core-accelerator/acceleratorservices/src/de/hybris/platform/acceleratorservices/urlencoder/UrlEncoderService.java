/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.urlencoder;

import de.hybris.platform.acceleratorservices.urlencoder.attributes.UrlEncodingAttributeManager;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Collection;
import java.util.Map;

/**
 *   Service class that to have centralized access to encoding attribute values
 */
public interface UrlEncoderService
{
	/**
	 *Checks if UrlEncoding for SEO is enabled
	 *
	 * @return boolean
	 */
	boolean isUrlEncodingEnabledForCurrentSite();

	/**
	 *  Returns the url pattern for encoding
	 *
	 * @return urlPattern for encoding
	 */
	String getUrlEncodingPattern();

	/**
	 *
	 * Checks if store language is one of the attributes for url encoding
	 *
	 * @return  if language is enabled for encoding
	 */
	boolean isLanguageEncodingEnabled();

	/**
	 * Check if store currency is one of the attributes for url encoding
	 *
	 * @return if currency is enabled for encoding
	 */
	boolean isCurrencyEncodingEnabled();

	/**
	 *
	 * @return  collection of attributes/properties to encode
	 */
	Collection<String> getEncodingAttributesForSite();

	/**
	 * Determines the url encoding pattern for email flows.
	 *
	 * @param businessProcessModel
	 * @return  the url pattern to encode for email links
	 */
	String getUrlEncodingPatternForEmail(BusinessProcessModel businessProcessModel);

	/**
	 *  Returns the url pattern for encoding
	 *
	 * @return urlPattern for encoding
	 */
	String getCurrentUrlEncodingPattern();

	/**
	 * Collection of attributes and attributemanages to determine the encoding pattern
	 *
	 * @return urlEncodingAttrManagerMap
	 */
	Map<String, UrlEncodingAttributeManager> getUrlEncodingAttrManagerMap();
}
