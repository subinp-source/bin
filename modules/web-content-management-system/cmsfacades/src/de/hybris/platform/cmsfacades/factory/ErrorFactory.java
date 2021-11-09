/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.factory;

import org.springframework.validation.Errors;


/**
 * Factory to creates instance of {@link Errors}
 */
public interface ErrorFactory
{

	/**
	 * Creates instance of {@link Errors}
	 * 
	 * @param object
	 *           the object target to be wrapped
	 * @return a {@link Errors} instance
	 */
	Errors createInstance(final Object object);
}
