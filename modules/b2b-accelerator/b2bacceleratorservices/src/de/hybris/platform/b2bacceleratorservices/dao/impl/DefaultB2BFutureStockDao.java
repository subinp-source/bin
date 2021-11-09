/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao.impl;

import de.hybris.platform.b2b.model.FutureStockModel;
import de.hybris.platform.b2bacceleratorservices.dao.B2BFutureStockDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation for {@link B2BFutureStockDao}.
 */
public class DefaultB2BFutureStockDao extends DefaultGenericDao<FutureStockModel> implements B2BFutureStockDao
{

	public DefaultB2BFutureStockDao()
	{
		super(FutureStockModel._TYPECODE);
	}

	@Override
	public List<FutureStockModel> getFutureStocksByProductCode(final String productCode)
	{
		final Map<String, String> params = new HashMap<>();
		params.put(FutureStockModel.PRODUCTCODE, productCode);
		return find(params);
	}

}
