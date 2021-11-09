/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.validator;

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemModelUtils.isEqual;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.slf4j.Logger;

public class SingleRootItemValidator implements ValidateInterceptor<IntegrationObjectItemModel>
{
	private static final Logger LOG = Log.getLogger(SingleRootItemValidator.class);

	@Override
	public void onValidate(final IntegrationObjectItemModel newItem, final InterceptorContext ctx) throws InterceptorException
	{
		if (newItem.getRoot())
		{
			try
			{
				validateSingleRootItemConstraint(newItem);
			}
			catch (final SingleRootItemConstraintViolationException e)
			{
				LOG.error("Multiple Integration Object Items found with root set to true on '{}' Integration Object.", newItem.getIntegrationObject().getCode());
				LOG.trace("Exception thrown", e);
				throw new InterceptorException(e.getMessage());
			}
		}
	}

	private void validateSingleRootItemConstraint(final IntegrationObjectItemModel newItem)
	{
		final IntegrationObjectItemModel existingRootItem =  newItem.getIntegrationObject().getRootItem();

		if(existingRootItem != null && isNewRootNotTheSameAsExistingRoot(newItem, existingRootItem))
		{
			throw new SingleRootItemConstraintViolationException(newItem.getIntegrationObject().getCode(), existingRootItem, newItem);
		}
	}

	private boolean isNewRootNotTheSameAsExistingRoot(final IntegrationObjectItemModel newItem, final IntegrationObjectItemModel rootItem)
	{
		return !isEqual(rootItem, newItem);
	}
}
