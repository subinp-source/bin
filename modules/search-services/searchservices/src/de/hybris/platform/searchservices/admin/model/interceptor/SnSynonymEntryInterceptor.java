/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.searchservices.model.SnSynonymEntryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Interceptor for {@link SnSynonymEntryModel}.
 */
public class SnSynonymEntryInterceptor extends AbstractSnInterceptor<SnSynonymEntryModel>
{
	@Override
	public void onValidate(final SnSynonymEntryModel synonymEntry, final InterceptorContext context) throws InterceptorException
	{
		validateIdentifier(synonymEntry, SnSynonymEntryModel.ID);
	}
}
