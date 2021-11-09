/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao.impl;

import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link SnIndexTypeDao}.
 */
public class DefaultSnIndexTypeDao extends DefaultGenericDao<SnIndexTypeModel> implements SnIndexTypeDao
{
	public DefaultSnIndexTypeDao()
	{
		super(SnIndexTypeModel._TYPECODE);
	}

	@Override
	public List<SnIndexTypeModel> findIndexTypesByIndexConfiguration(final SnIndexConfigurationModel indexConfiguration)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnIndexTypeModel.INDEXCONFIGURATION, indexConfiguration);

		return find(queryParams);
	}

	@Override
	public Optional<SnIndexTypeModel> findIndexTypeById(final String id)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnIndexTypeModel.ID, id);

		final List<SnIndexTypeModel> indexTypes = find(queryParams);

		return CollectionUtils.isEmpty(indexTypes) ? Optional.empty() : Optional.of(indexTypes.get(0));
	}
}
