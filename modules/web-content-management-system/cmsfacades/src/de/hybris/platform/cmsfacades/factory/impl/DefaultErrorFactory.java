/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.factory.impl;

import de.hybris.platform.cmsfacades.factory.ErrorFactory;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


/**
 * Default implementation of {@link ErrorFactory}
 */
public class DefaultErrorFactory implements ErrorFactory
{

	@Override
	public Errors createInstance(final Object object)
	{
		return new BeanPropertyBindingResult(object, object.getClass().getSimpleName());
	}
}
