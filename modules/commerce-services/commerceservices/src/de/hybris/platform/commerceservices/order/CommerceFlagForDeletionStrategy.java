/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A strategy interface for flagging a cart for deletion.
 */
public interface CommerceFlagForDeletionStrategy
{
	/**
	 * Method for explicitly flagging a cart for deletion
	 *
	 * @param parameters
	 *           {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter} parameter object that
	 *           holds the cart to be flagged for deletion along with some additional details such as a name and a
	 *           description for this cart
	 * @return {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 *            if cart cannot be flagged for deletion
	 */
	CommerceSaveCartResult flagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException;
}
