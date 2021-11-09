/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.b2b.model.FutureStockModel;

import java.util.List;


/**
 * DAO for Future Stock.
 *
 */
public interface B2BFutureStockDao
{
	List<FutureStockModel> getFutureStocksByProductCode(final String productCode);
}
