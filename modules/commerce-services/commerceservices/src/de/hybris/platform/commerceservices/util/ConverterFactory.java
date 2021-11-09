/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import org.apache.log4j.Logger;


/**
 * @deprecated Since 6.0. Use {@link de.hybris.platform.converters.impl.AbstractPopulatingConverter} directly instead
 */
@Deprecated(since = "6.0", forRemoval = true)
public class ConverterFactory<SOURCE, TARGET, P extends Populator>
{
	//Logger for anonymous inner class
	private static final Logger LOG = Logger.getLogger(AbstractPopulatingConverter.class);

	public AbstractPopulatingConverter<SOURCE, TARGET> create(final Class<TARGET> targetClass, final P... populators)
	{
		return new AbstractPopulatingConverter<SOURCE, TARGET>()
		{
			@Override
			protected TARGET createTarget()
			{
				try
				{
					return targetClass.newInstance();
				}
				catch (final InstantiationException | IllegalAccessException e)
				{
					LOG.fatal(e);
				}
				return null;
			}

			@Override
			public void populate(final SOURCE source, final TARGET target)
			{
				for (final Populator populator : populators)
				{
					populator.populate(source, target);
				}
			}
		};
	}
}
