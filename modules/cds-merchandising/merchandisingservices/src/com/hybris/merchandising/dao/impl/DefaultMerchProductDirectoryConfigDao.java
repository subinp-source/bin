/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hybris.merchandising.dao.MerchProductDirectoryConfigDao;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;


/**
 * Default implementation of {@link MerchProductDirectoryConfigDao}.
 */
public class DefaultMerchProductDirectoryConfigDao extends DefaultGenericDao<MerchProductDirectoryConfigModel>
		implements MerchProductDirectoryConfigDao
{
	/**
	 * Creates DAO for {@link MerchProductDirectoryConfigModel}.
	 */
	public DefaultMerchProductDirectoryConfigDao()
	{
		super(MerchProductDirectoryConfigModel._TYPECODE);
	}

	@Override
	public Collection<MerchProductDirectoryConfigModel> findAllMerchProductDirectoryConfigs()
	{
		return find();
	}

	@Override
	public Optional<MerchProductDirectoryConfigModel> findMerchProductDirectoryConfigByIndexedType(
			final SolrIndexedTypeModel indexedType)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(MerchProductDirectoryConfigModel.INDEXEDTYPE, indexedType);

		final List<MerchProductDirectoryConfigModel> merchProductDirectoryConfigs = find(queryParams);

		return Optional.ofNullable(merchProductDirectoryConfigs).filter(config -> !config.isEmpty())
				.map(pdConfig -> pdConfig.get(0));
	}
}
