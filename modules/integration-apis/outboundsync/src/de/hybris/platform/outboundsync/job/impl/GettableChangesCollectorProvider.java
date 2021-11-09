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
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.job.ChangesCollectorProvider;
import de.hybris.platform.outboundsync.job.GettableChangesCollector;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * A {@link ChangesCollectorProvider} that provides {@link GettableChangesCollector}s.
 *
 * The user of this ChangesCollectorProvider needs to supply the full class name of the
 * GettableChangesCollector to instantiate when calling {@link #getCollector()}.
 * @deprecated not used anymore. Replaced by {@link de.hybris.platform.outboundsync.job.ChangesCollectorFactory}
 */
@Deprecated(since = "1905.2003-CEP", forRemoval = true)
public class GettableChangesCollectorProvider implements ChangesCollectorProvider<GettableChangesCollector>
{
	private static final Logger LOG = Log.getLogger(GettableChangesCollectorProvider.class);

	private Class collectorClass;

	/**
	 * Gets a new instance of the {@link GettableChangesCollector}.
	 * If the collector class name supplied can't be instantiated dynamically,
	 * this method tries to instantiate the {@link InMemoryGettableChangesCollector}
	 *
	 * @return A GettableChangesCollector
	 */
	@Override
	public GettableChangesCollector getCollector()
	{
		return createCollector(collectorClass);
	}

	/**
	 * Gets the class for the supplied collector class name.
	 * The class must also be assignable to {@link GettableChangesCollector}.
	 *
	 * @return Collector class or null if the class fails to load or not assignable.
	 */
	protected Class getCollectorClass(final String collectorClassName)
	{
		try
		{
			final Class clazz = Class.forName(collectorClassName);
			if (GettableChangesCollector.class.isAssignableFrom(clazz))
			{
				return clazz;
			}
			else
			{
				LOG.warn("{} is not of GettableChangesCollector type", collectorClassName);
			}
		}
		catch (final ClassNotFoundException e)
		{
			LOG.error("An exception occurred trying to create class {}", collectorClassName, e);
		}
		return null;
	}

	/**
	 * Creates an instance of the {@code clazz} provided. If the instantiation fails
	 * the {@link #defaultCollector()} is returned
	 *
	 * @param clazz Class to instantiate
	 * @return An instance of a {@link GettableChangesCollector}
	 */
	protected GettableChangesCollector createCollector(final Class clazz)
	{
		try
		{
			if (clazz != null)
			{
				return (GettableChangesCollector) clazz.newInstance();
			}
		}
		catch (final InstantiationException | IllegalAccessException e)
		{
			LOG.error("An exception occurred while creating a new instance of {}", clazz.getName(), e);
		}
		return defaultCollector();
	}

	/**
	 * Returns the default {@link GettableChangesCollector}
	 *
	 * @return An instance of GettableChangesCollector
	 */
	protected GettableChangesCollector defaultCollector()
	{
		LOG.info("Using the default changes collector");
		return new InMemoryGettableChangesCollector();
	}

	@Required
	public void setCollectorClass(final String collectorClassName)
	{
		this.collectorClass = getCollectorClass(collectorClassName);
	}
}
