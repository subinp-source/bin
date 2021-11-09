/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.DescriptorFactory;

/**
 * Common superclass for all descriptors produced by {@link DefaultDescriptorFactory}
 */
public class AbstractDescriptor
{
	private static final DescriptorFactory DEFAULT_FACTORY = new DefaultDescriptorFactory();
	private final DescriptorFactory factory;

	AbstractDescriptor(final DescriptorFactory f)
	{
		factory = f != null
				? f
				: DEFAULT_FACTORY;
	}

	/**
	 * Retrieves the abstract factory to be used for creating dependent objects.
	 * @return a factory instance
	 */
	protected final DescriptorFactory getFactory()
	{
		return factory;
	}
}
