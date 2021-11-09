/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.integrationkey;

/**
 * Generates an integrationKey based on a type and an element
 * @param <T> The type describing the element structure
 * @param <E> The element carrying the payload
 */
public interface IntegrationKeyValueGenerator<T, E>
{
	/**
	 * Generates a compound key value for a data structure that has one or several key attributes/properties.
	 *
	 * @param type definition of the payload structure that can be used to determine which elements of the payload structure
	 * contain the key value(s).
	 * @param itemData a data structure to generate the key value for.
	 *
	 * @return integration key string representing the compound value of all key elements of the data structure
	 */
	String generate(T type, E itemData);
}
