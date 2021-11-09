/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import org.springframework.beans.BeanUtils;

/**
 * Populator to convert SmartEditConfigurationModel into ConfigurationData
 */
public class SmarteditConfigurationModelToDataPopulator implements Populator<SmarteditConfigurationModel, ConfigurationData>
{
	@Override
	public void populate(final SmarteditConfigurationModel source, final ConfigurationData target)
			throws ConversionException
	{
		BeanUtils.copyProperties(source, target, getIgnoreProperties());
	}

	/**
	 * gets all properties to be ignored while copying the properties
	 * @return
	 */
	protected String[] getIgnoreProperties()
	{
		return null;
	}
}
