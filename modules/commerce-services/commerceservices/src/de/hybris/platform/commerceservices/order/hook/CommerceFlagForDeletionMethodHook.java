/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.hook;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;


/**
 * A hook strategy to run custom code before/after flagging a cart for deletion
 */
public interface CommerceFlagForDeletionMethodHook
{
	/**
	 * Execute custom logic before flagging a cart for deletion
	 *
	 * @param parameters
	 *           a {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter} parameter object
	 * @throws de.hybris.platform.commerceservices.order.CommerceSaveCartException
	 *
	 */
	void beforeFlagForDeletion(CommerceSaveCartParameter parameters) throws CommerceSaveCartException;

	/**
	 * Execute custom logic after flagging a cart for deletion
	 *
	 * @param parameters
	 *           a {@link CommerceSaveCartParameter} parameter object
	 * @param saveCartResult
	 *           {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult}
	 * @throws CommerceSaveCartException
	 */
	void afterFlagForDeletion(CommerceSaveCartParameter parameters, CommerceSaveCartResult saveCartResult)
			throws CommerceSaveCartException;
}
