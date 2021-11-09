/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.delivery.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


public interface PickupDeliveryModeDao extends Dao
{
	List<DeliveryModeModel> findPickupDeliveryModesForAbstractOrder(AbstractOrderModel abstractOrder);
}
