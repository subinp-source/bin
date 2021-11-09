/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Interceptor for {@link SnFieldModel}.
 */
public class SnFieldInterceptor extends AbstractSnInterceptor<SnFieldModel>
{
	@Override
	public void onValidate(final SnFieldModel field, final InterceptorContext context) throws InterceptorException
	{
		validateIdentifier(field, SnFieldModel.ID);
	}
}
