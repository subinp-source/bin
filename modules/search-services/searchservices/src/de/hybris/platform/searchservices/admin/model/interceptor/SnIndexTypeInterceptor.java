/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Interceptor for {@link SnIndexTypeModel}.
 */
public class SnIndexTypeInterceptor extends AbstractSnInterceptor<SnIndexTypeModel>
{
	@Override
	public void onValidate(final SnIndexTypeModel indexType, final InterceptorContext context) throws InterceptorException
	{
		validateIdentifier(indexType, SnIndexTypeModel.ID);
	}
}
