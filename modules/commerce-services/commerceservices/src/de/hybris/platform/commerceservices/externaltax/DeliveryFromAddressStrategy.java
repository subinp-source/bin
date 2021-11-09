/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.externaltax;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

/**
 *  A strategy to get a ship from address used for external tax calculation.
 */

public interface DeliveryFromAddressStrategy
{

	AddressModel getDeliveryFromAddressForOrder(final AbstractOrderModel orderModel);
}
