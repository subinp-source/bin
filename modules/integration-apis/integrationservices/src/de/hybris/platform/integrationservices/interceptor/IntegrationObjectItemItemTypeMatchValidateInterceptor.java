/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Collection;

public class IntegrationObjectItemItemTypeMatchValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemModel>
{
	private static final String INVALID_MSG_TEMPLATE = "An IntegrationObjectItem of type %s cannot have an itemTypeMatch" +
			" set to %s. The permitted itemTypeMatch values for an item of this type are %s.";

	@Override
	public void onValidate(final IntegrationObjectItemModel itemModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		final ItemTypeMatchEnum itemTypeMatch = itemModel.getItemTypeMatch();
		final Collection<ItemTypeMatchEnum> validTypeMatches = itemModel.getAllowedItemTypeMatches();
		if (validTypeMatches != null && itemTypeMatch != null && !validTypeMatches.contains(itemTypeMatch))
		{
			throw new InterceptorException(
					String.format(INVALID_MSG_TEMPLATE, itemModel.getType().getCode(), itemTypeMatch, validTypeMatches), this);
		}
	}
}
