/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;

import de.hybris.platform.searchservices.admin.dao.SnSynonymDictionaryDao;
import de.hybris.platform.searchservices.admin.service.SnSynonymDictionaryService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.searchservices.admin.data.SnSynonymDictionary;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link SnSynonymDictionaryService}.
 */
public class DefaultSnSynonymDictionaryService implements SnSynonymDictionaryService
{
	private SnSynonymDictionaryDao snSynonymDictionaryDao;
	private Converter<SnSynonymDictionaryModel, SnSynonymDictionary> snSynonymDictionaryConverter;

	@Override
	public Optional<SnSynonymDictionary> getSynonymDictionaryForId(final String id)
	{
		final Optional<SnSynonymDictionaryModel> indexSynonymDictionaryOptional = snSynonymDictionaryDao.findSynonymDictionaryById(id);
		return indexSynonymDictionaryOptional.map(snSynonymDictionaryConverter::convert);
	}

	public SnSynonymDictionaryDao getSnSynonymDictionaryDao()
	{
		return snSynonymDictionaryDao;
	}

	@Required
	public void setSnSynonymDictionaryDao(final SnSynonymDictionaryDao snSynonymDictionaryDao)
	{
		this.snSynonymDictionaryDao = snSynonymDictionaryDao;
	}

	public Converter<SnSynonymDictionaryModel, SnSynonymDictionary> getSnSynonymDictionaryConverter()
	{
		return snSynonymDictionaryConverter;
	}

	@Required
	public void setSnSynonymDictionaryConverter(final Converter<SnSynonymDictionaryModel, SnSynonymDictionary> snSynonymDictionaryConverter)
	{
		this.snSynonymDictionaryConverter = snSynonymDictionaryConverter;
	}
}
