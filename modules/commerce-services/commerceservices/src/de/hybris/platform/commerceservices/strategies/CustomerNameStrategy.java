/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

public interface CustomerNameStrategy
{
	/**
	 * Retrieves first and lastname by splitting the given name at the last found space separator.
	 * 
	 * @param name
	 * @return array containing first- and lastname
	 */
	String[] splitName(String name);

	/**
	 * Retrieves the name by combining firstname and lastname separated by a space character.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return name
	 */
	String getName(String firstName, String lastName);
}
