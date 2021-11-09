/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Interceptor for {@link SnIndexConfigurationModel}.
 */
public class SnIndexConfigurationInterceptor extends AbstractSnInterceptor<SnIndexConfigurationModel>
{
	@Override
	public void onValidate(final SnIndexConfigurationModel indexConfiguration, final InterceptorContext context)
			throws InterceptorException
	{
		validateIdentifier(indexConfiguration, SnIndexConfigurationModel.ID);
	}
}
