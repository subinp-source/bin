/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.url.impl;

import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.ordersplitting.model.VendorModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * URL resolver for VendorModel. The pattern could be of the form: /v/{vendor-code}
 */
public class VendorUrlResolver extends AbstractUrlResolver<VendorModel>
{

	private String pattern;

	@Override
	protected String resolveInternal(final VendorModel vendor)
	{
		String url = getPattern();
		if (url.contains("{vendor-code}"))
		{
			url = url.replace("{vendor-code}", vendor.getCode());
		}
		return url;
	}


	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

}
