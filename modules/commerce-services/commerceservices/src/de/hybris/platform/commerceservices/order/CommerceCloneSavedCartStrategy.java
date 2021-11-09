/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A strategy for cloning user save carts.
 */
public interface CommerceCloneSavedCartStrategy
{
	CommerceSaveCartResult cloneSavedCart(CommerceSaveCartParameter parameter) throws CommerceSaveCartException;
}
