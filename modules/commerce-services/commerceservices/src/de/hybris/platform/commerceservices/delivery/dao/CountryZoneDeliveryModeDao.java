/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.delivery.dao;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * DAO to find zone delivery modes.
 */
public interface CountryZoneDeliveryModeDao extends Dao
{
	/**
	 * Find the delivery modes for the delivery country, currency and net flag.
	 *
	 * @param deliveryCountry
	 *           the delivery country
	 * @param currency
	 *           the cart currency
	 * @param net
	 *           the net flag
	 * @return the matching delivery modes
	 *
	 * @deprecated Since 5.0. Use findDeliveryModes(AbstractOrderModel abstractOrder) instead.
	 */
	@Deprecated(since = "5.0", forRemoval = true)
	Collection<DeliveryModeModel> findDeliveryModesByCountryAndCurrency(CountryModel deliveryCountry, CurrencyModel currency,
			Boolean net);

	/**
	 * Find the delivery modes for the delivery country, currency, net flag and store.
	 *
	 * @param order
	 *           the order with search parameters.
	 * @return the matching delivery modes
	 */
	Collection<DeliveryModeModel> findDeliveryModes(AbstractOrderModel order);

}
