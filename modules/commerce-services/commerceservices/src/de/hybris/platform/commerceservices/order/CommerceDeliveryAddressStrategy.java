/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

/**
 *
 *
 */
public interface CommerceDeliveryAddressStrategy
{
	boolean storeDeliveryAddress(CommerceCheckoutParameter parameter);
}
