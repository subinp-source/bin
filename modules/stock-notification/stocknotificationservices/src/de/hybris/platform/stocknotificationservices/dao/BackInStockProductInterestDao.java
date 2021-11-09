/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.dao;

import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;

import java.util.List;


/**
 * manipulate ProductInterests whose notification type is BACK_IN_STOCK
 *
 * @deprecated Since 1905
 */
@Deprecated(since = "1905", forRemoval= true )
public interface BackInStockProductInterestDao
{

	/**
	 * retrieve such ProductInterests whose notification type is BACK_IN_STOCK and not expired
	 *
	 * @return The list of ProductInterests to send BACK_IN_STOCK notification
	 *
	 * @deprecated Since 1905. Use {@link ProductInterestDao.findProductInterestsByNotificationType()} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	List<ProductInterestModel> findBackInStorkProductInterests();

}
