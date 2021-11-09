/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao.impl;

import de.hybris.platform.searchservices.admin.dao.SnSynonymDictionaryDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link SnSynonymDictionaryDao}.
 */
public class DefaultSnSynonymDictionaryDao extends DefaultGenericDao<SnSynonymDictionaryModel> implements SnSynonymDictionaryDao
{
	public DefaultSnSynonymDictionaryDao()
	{
		super(SnSynonymDictionaryModel._TYPECODE);
	}

	@Override
	public Optional<SnSynonymDictionaryModel> findSynonymDictionaryById(final String code)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnSynonymDictionaryModel.ID, code);

		final List<SnSynonymDictionaryModel> synonymDictionaries = find(queryParams);

		return CollectionUtils.isEmpty(synonymDictionaries) ? Optional.empty() : Optional.of(synonymDictionaries.get(0));
	}
}
