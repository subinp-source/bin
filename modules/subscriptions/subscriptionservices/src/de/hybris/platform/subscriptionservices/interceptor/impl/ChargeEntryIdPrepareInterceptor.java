/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscriptionservices.model.ChargeEntryModel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;


/**
 * Create a unique id for new {@link ChargeEntryModel}s if not yet set.
 */
public class ChargeEntryIdPrepareInterceptor implements PrepareInterceptor
{

	private KeyGenerator chargeEntryIDGenerator;

	@Override
	public void onPrepare(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{

		if (model instanceof ChargeEntryModel)
		{
			final ChargeEntryModel chargeEntry = (ChargeEntryModel) model;
			final String id = chargeEntry.getId();
			if (StringUtils.isEmpty(id))
			{
				chargeEntry.setId(this.chargeEntryIDGenerator.generate().toString());
			}
		}
	}

	@Required
	public void setChargeEntryIDGenerator(final KeyGenerator chargeEntryIDGenerator)
	{
		this.chargeEntryIDGenerator = chargeEntryIDGenerator;
	}

}
