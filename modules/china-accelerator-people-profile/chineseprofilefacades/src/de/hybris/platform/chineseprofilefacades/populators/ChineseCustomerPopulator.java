/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.populators;

import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;

import org.apache.commons.lang3.StringUtils;


public class ChineseCustomerPopulator implements Populator<CustomerModel, CustomerData>
{
	private final ChineseCustomerAccountService chineseCustomerAccountService;

	public ChineseCustomerPopulator(final ChineseCustomerAccountService chineseCustomerAccountService)
	{
		this.chineseCustomerAccountService = chineseCustomerAccountService;
	}
	@Override
	public void populate(final CustomerModel source, final CustomerData target)
	{
		final String emailLanguage = source.getEmailLanguage();
		if (StringUtils.isNotEmpty(emailLanguage))
		{
			target.setEmailLanguage(emailLanguage);
		}
		else
		{
			target.setEmailLanguage(getChineseCustomerAccountService().getDefaultEmailLanguage());
		}

		final String mobileNumber = source.getMobileNumber();
		if (StringUtils.isNotEmpty(mobileNumber))
		{
			target.setMobileNumber(mobileNumber);
		}
	}

	protected ChineseCustomerAccountService getChineseCustomerAccountService()
	{
		return chineseCustomerAccountService;
	}
}
