/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao.impl;

import de.hybris.platform.searchservices.admin.dao.SnIndexConfigurationDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link SnIndexConfigurationDao}.
 */
public class DefaultSnIndexConfigurationDao extends DefaultGenericDao<SnIndexConfigurationModel>
		implements SnIndexConfigurationDao
{
	public DefaultSnIndexConfigurationDao()
	{
		super(SnIndexConfigurationModel._TYPECODE);
	}

	@Override
	public Optional<SnIndexConfigurationModel> findIndexConfigurationById(final String id)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnIndexConfigurationModel.ID, id);

		final List<SnIndexConfigurationModel> indexConfigurations = find(queryParams);

		return CollectionUtils.isEmpty(indexConfigurations) ? Optional.empty() : Optional.of(indexConfigurations.get(0));
	}
}
