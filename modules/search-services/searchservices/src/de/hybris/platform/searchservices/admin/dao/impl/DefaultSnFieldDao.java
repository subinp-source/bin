/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.dao.impl;

import de.hybris.platform.searchservices.admin.dao.SnFieldDao;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link SnFieldDao}.
 */
public class DefaultSnFieldDao extends DefaultGenericDao<SnFieldModel> implements SnFieldDao
{
	public DefaultSnFieldDao()
	{
		super(SnFieldModel._TYPECODE);
	}

	@Override
	public List<SnFieldModel> findFieldsByIndexType(final SnIndexTypeModel indexType)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnFieldModel.INDEXTYPE, indexType);

		return find(queryParams);
	}

	@Override
	public Optional<SnFieldModel> findFieldByIndexTypeAndId(final SnIndexTypeModel indexType, final String id)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(SnFieldModel.INDEXTYPE, indexType);
		queryParams.put(SnFieldModel.ID, id);

		final List<SnFieldModel> fields = find(queryParams);

		return CollectionUtils.isEmpty(fields) ? Optional.empty() : Optional.of(fields.get(0));
	}
}
