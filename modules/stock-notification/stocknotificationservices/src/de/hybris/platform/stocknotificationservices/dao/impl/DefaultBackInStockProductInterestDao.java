/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.dao.impl;

import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.stocknotificationservices.dao.BackInStockProductInterestDao;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * manipulate ProductInterests whose notification type is BACK_IN_STOCK
 *
 * @deprecated Since 1905
 */
@Deprecated(since = "1905", forRemoval= true )
public class DefaultBackInStockProductInterestDao extends DefaultGenericDao<ProductInterestModel> implements BackInStockProductInterestDao
{
	private static final String FIND_PRODUCT_BY_NOTIFICATIONTYPE = "SELECT {" + ProductInterestModel.PK + "} FROM {"
			+ ProductInterestModel._TYPECODE + "} " + "WHERE {" + ProductInterestModel.NOTIFICATIONTYPE + "}=?notificationType"
			+ " AND {" + ProductInterestModel.EXPIRYDATE + "}>?expiryDate";

	public DefaultBackInStockProductInterestDao()
	{
		super(ProductInterestModel._TYPECODE);
	}

	@Override
	public List<ProductInterestModel> findBackInStorkProductInterests()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_BY_NOTIFICATIONTYPE);

		query.addQueryParameter("notificationType", NotificationType.BACK_IN_STOCK);
		query.addQueryParameter("expiryDate", new Date());

		final List<ProductInterestModel> protuctInterests = getFlexibleSearchService().<ProductInterestModel> search(query)
				.getResult();
		if (CollectionUtils.isEmpty(protuctInterests))
		{
			return Collections.emptyList();
		}
		return protuctInterests;
	}

}
