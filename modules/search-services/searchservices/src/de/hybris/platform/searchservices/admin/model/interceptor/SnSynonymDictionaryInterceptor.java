/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.model.interceptor;

import de.hybris.platform.searchservices.core.model.interceptor.AbstractSnInterceptor;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Interceptor for {@link SnSynonymDictionaryModel}.
 */
public class SnSynonymDictionaryInterceptor extends AbstractSnInterceptor<SnSynonymDictionaryModel>
{
	@Override
	public void onValidate(final SnSynonymDictionaryModel synonymDictionary, final InterceptorContext context)
			throws InterceptorException
	{
		validateIdentifier(synonymDictionary, SnSynonymDictionaryModel.ID);
	}
}
