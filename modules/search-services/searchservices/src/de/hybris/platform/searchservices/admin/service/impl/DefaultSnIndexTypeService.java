/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;


import de.hybris.platform.searchservices.admin.dao.SnIndexConfigurationDao;
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SnIndexTypeService}.
 */
public class DefaultSnIndexTypeService implements SnIndexTypeService
{
	private SnIndexConfigurationDao snIndexConfigurationDao;
	private SnIndexTypeDao snIndexTypeDao;
	private Converter<SnIndexTypeModel, SnIndexType> snIndexTypeConverter;

	@Override
	public List<SnIndexType> getAllIndexTypes()
	{
		final List<SnIndexTypeModel> indexTypes = snIndexTypeDao.find();
		return ConverterUtils.convertAll(indexTypes, snIndexTypeConverter::convert);
	}

	@Override
	public List<SnIndexType> getIndexTypesForIndexConfiguration(final String id)
	{
		final Optional<SnIndexConfigurationModel> indexConfigurationOptional = snIndexConfigurationDao
				.findIndexConfigurationById(id);
		if (!indexConfigurationOptional.isPresent())
		{
			return Collections.emptyList();
		}

		return ConverterUtils.convertAll(indexConfigurationOptional.get().getIndexTypes(), snIndexTypeConverter::convert);
	}

	@Override
	public Optional<SnIndexType> getIndexTypeForId(final String id)
	{
		final Optional<SnIndexTypeModel> indexTypeOptional = snIndexTypeDao.findIndexTypeById(id);
		return indexTypeOptional.map(snIndexTypeConverter::convert);
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

	public SnIndexTypeDao getSnIndexTypeDao()
	{
		return snIndexTypeDao;
	}

	@Required
	public void setSnIndexTypeDao(final SnIndexTypeDao snIndexTypeDao)
	{
		this.snIndexTypeDao = snIndexTypeDao;
	}

	public Converter<SnIndexTypeModel, SnIndexType> getSnIndexTypeConverter()
	{
		return snIndexTypeConverter;
	}

	@Required
	public void setSnIndexTypeConverter(final Converter<SnIndexTypeModel, SnIndexType> snIndexTypeConverter)
	{
		this.snIndexTypeConverter = snIndexTypeConverter;
	}
}
