/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types;

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Exception thrown when searching for a component type that could not be found or does not exist.
 */
public class ComponentTypeNotFoundException extends BusinessException
{
	private static final long serialVersionUID = -5978659209875421565L;

	public ComponentTypeNotFoundException(String message)
	{
		super(message);
	}

}
