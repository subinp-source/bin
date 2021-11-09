/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.validator;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;

/**
 * Verify the {@link IntegrationObjectModel} contains a root item. If non exists,
 * a warning is logged. Because of backwards compatibility, no exception is thrown
 * when a root does not exist.
 */
public class RootExistsValidator implements ValidateInterceptor<IntegrationObjectModel>
{
	private static final Logger LOG = Log.getLogger(RootExistsValidator.class);

	@Override
	public void onValidate(final IntegrationObjectModel integrationObjectModel, final InterceptorContext interceptorContext)
	{
		try
		{
			if (hasNoRootItem(integrationObjectModel))
			{
				LOG.warn("IntegrationObject {} does not have a root item assigned. Certain inbound and outbound processes rely on the root item designation and may not function as design.", integrationObjectModel.getCode());
			}
		}
		catch (final SingleRootItemConstraintViolationException e)
		{
			LOG.trace(e.getMessage());
		}
	}

	private boolean hasNoRootItem(final IntegrationObjectModel integrationObjectModel)
	{
		return CollectionUtils.isNotEmpty(integrationObjectModel.getItems()) &&
				integrationObjectModel.getRootItem() == null;
	}
}
