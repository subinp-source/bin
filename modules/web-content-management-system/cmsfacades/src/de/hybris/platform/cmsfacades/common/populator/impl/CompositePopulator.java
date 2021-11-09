/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.populator.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;


/**
 * Composite populator that uses a list of configured populators to populate the target.
 * @param <SOURCE> the type of the source
 * @param <TARGET> the type of the target
 */
public class CompositePopulator<SOURCE, TARGET> implements Populator<SOURCE, TARGET>
{
	private List<Populator<SOURCE, TARGET>> populators;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		getPopulators().forEach(populator -> populator.populate(source, target));
	}

	public List<Populator<SOURCE, TARGET>> getPopulators()
	{
		return populators;
	}

	public void setPopulators(final List<Populator<SOURCE, TARGET>> populators)
	{
		this.populators = populators;
	}
}
