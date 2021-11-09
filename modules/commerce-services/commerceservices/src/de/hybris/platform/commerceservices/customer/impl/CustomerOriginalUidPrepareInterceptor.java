/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.apache.commons.lang.StringUtils;


/**
 * Changes uid of {@link CustomerModel} to lowercase
 */
public class CustomerOriginalUidPrepareInterceptor implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof CustomerModel)
		{
			final CustomerModel customer = (CustomerModel) model;

			if (ctx.isNew(customer))
			{
				adjustUid(customer);
			}
			else if (ctx.isModified(model, CustomerModel.ORIGINALUID) || ctx.isModified(model, CustomerModel.UID))
			{
				adjustUid(customer);
			}
		}
	}


	protected void adjustUid(final CustomerModel customer)
	{
		final String original = customer.getOriginalUid();
		final String uid = customer.getUid();
		if (StringUtils.isNotEmpty(uid))
		{
			final String lowerCaseUid = StringUtils.lowerCase(uid);

			if (!uid.equals(lowerCaseUid))
			{
				customer.setUid(lowerCaseUid);
				customer.setOriginalUid(uid);
			}
			else if (!uid.equalsIgnoreCase(original))
			{
				customer.setOriginalUid(uid);
			}
		}
		else if (StringUtils.isNotEmpty(original))
		{
			customer.setUid(original.toLowerCase());
		}
	}
}
