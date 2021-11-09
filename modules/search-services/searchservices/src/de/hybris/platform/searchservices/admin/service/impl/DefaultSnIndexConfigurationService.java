/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;


import de.hybris.platform.searchservices.admin.dao.SnIndexConfigurationDao;
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnIndexConfigurationService}.
 */
public class DefaultSnIndexConfigurationService implements SnIndexConfigurationService
{
	private SnIndexConfigurationDao snIndexConfigurationDao;
	private Converter<SnIndexConfigurationModel, SnIndexConfiguration> snIndexConfigurationConverter;

	@Override
	public List<SnIndexConfiguration> getAllIndexConfigurations()
	{
		final List<SnIndexConfigurationModel> indexConfigurations = snIndexConfigurationDao.find();
		return ConverterUtils.convertAll(indexConfigurations, snIndexConfigurationConverter::convert);
	}

	@Override
	public Optional<SnIndexConfiguration> getIndexConfigurationForId(final String id)
	{
		final Optional<SnIndexConfigurationModel> indexConfigurationOptional = snIndexConfigurationDao.findIndexConfigurationById(id);
		return indexConfigurationOptional.map(snIndexConfigurationConverter::convert);
	}

	public SnIndexConfigurationDao getSnIndexConfigurationDao()
	{
		return snIndexConfigurationDao;
	}

	@Required
	public void setSnIndexConfigurationDao(final SnIndexConfigurationDao snIndexConfigurationDao)
	{
		this.snIndexConfigurationDao = snIndexConfigurationDao;
	}

	public Converter<SnIndexConfigurationModel, SnIndexConfiguration> getSnIndexConfigurationConverter()
	{
		return snIndexConfigurationConverter;
	}

	@Required
	public void setSnIndexConfigurationConverter(
			final Converter<SnIndexConfigurationModel, SnIndexConfiguration> snIndexConfigurationConverter)
	{
		this.snIndexConfigurationConverter = snIndexConfigurationConverter;
	}
}
