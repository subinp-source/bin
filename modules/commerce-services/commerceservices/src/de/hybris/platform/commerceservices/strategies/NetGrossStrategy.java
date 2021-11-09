/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;



/**
 * Interface for strategy, which allows for overriding the default behavior to retrieve the net/gross setting.
 * 
 * @spring.bean netGrossStrategy
 */
public interface NetGrossStrategy
{
	/**
	 * Method for retrieving the net/gross setting
	 * 
	 * @return the net/gross setting to be used for retrieving price information
	 */
	boolean isNet();
}
