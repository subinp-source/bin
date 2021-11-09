/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hybris.merchandising.dao.MerchIndexingConfigDao;
import com.hybris.merchandising.model.MerchIndexingConfigModel;

/**
 * Default implementation of {@link MerchIndexingConfigDao}.
 */
public class DefaultMerchIndexingConfigDao extends DefaultGenericDao<MerchIndexingConfigModel> implements MerchIndexingConfigDao
{
	/**
	 * Creates DAO for {@link MerchIndexingConfigModel}.
	 */
	public DefaultMerchIndexingConfigDao()
	{
		super(MerchIndexingConfigModel._TYPECODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<MerchIndexingConfigModel> findAllMerchIndexingConfigs()
	{
		return find();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MerchIndexingConfigModel> findMerchIndexingConfigByIndexedType(final SolrIndexedTypeModel indexedType)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(MerchIndexingConfigModel.INDEXEDTYPE, indexedType);

		final List<MerchIndexingConfigModel> merchIndexingConfigs = find(queryParams);

		return merchIndexingConfigs.isEmpty() ? Optional.empty() : Optional.of(merchIndexingConfigs.get(0));
	}

}
