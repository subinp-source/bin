/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;


/**
 * Interceptor for {@link SnSearchProviderConfigurationModel}.
 */
public class SnSearchProviderConfigurationInterceptor extends AbstractSnInterceptor<AbstractSnSearchProviderConfigurationModel>
		implements ValidateInterceptor<AbstractSnSearchProviderConfigurationModel>
{
	@Override
	public void onValidate(final AbstractSnSearchProviderConfigurationModel searchProviderConfiguration,
			final InterceptorContext context) throws InterceptorException
	{
		validateIdentifier(searchProviderConfiguration, AbstractSnSearchProviderConfigurationModel.ID);
	}
}
