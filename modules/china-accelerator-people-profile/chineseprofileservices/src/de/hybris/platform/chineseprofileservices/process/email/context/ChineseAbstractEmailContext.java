/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.addressservices.strategies.NameWithTitleFormatStrategy;
import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;

public abstract class ChineseAbstractEmailContext<T extends BusinessProcessModel> extends AbstractEmailContext<T>
{
	private CustomerNameStrategy customerNameStrategy;
	private NameWithTitleFormatStrategy nameWithTitleFormatStrategy;
	private ChineseCustomerAccountService chineseCustomerAccountService;

	public static final String NAME_WITH_TITLE = "nameWithTitle";

	@Override
	public void init(final T businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		localize(businessProcessModel);
		setNameWithTitleInContext();
	}

	@Override
	protected LanguageModel getEmailLanguage(final T businessProcessModel)
	{
		final LanguageModel langModel = new LanguageModel();
		final String language = getCustomer(businessProcessModel).getEmailLanguage();
		if (language == null || language.isEmpty())
		{
			langModel.setIsocode(getChineseCustomerAccountService().getDefaultEmailLanguage());
		}
		else
		{
			langModel.setIsocode(language);
		}
		return langModel;
	}

	protected void localize(final T businessProcessModel)
	{
		final CustomerModel customerModel = getCustomer(businessProcessModel);
		if (customerModel != null)
		{
			final Locale locale = new Locale(getEmailLanguage(businessProcessModel).getIsocode());
			put(TITLE, (customerModel.getTitle() != null && customerModel.getTitle().getName() != null)
					? customerModel.getTitle().getName(locale) : "");
		}
	}

	/**
	 * Set the nameWithTitle in email context
	 */
	public void setNameWithTitleInContext()
	{
		final String[] names = getCustomerNameStrategy().splitName(getDisplayName());

		put(NAME_WITH_TITLE, getNameWithTitleFormatStrategy().getFullnameWithTitleForISOCode(names[0], names[1], getTitle(),
				getEmailLanguage().getIsocode()));

	}

	public String getNameWithTitle()
	{
		return (String) get(NAME_WITH_TITLE);
	}

	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	@Required
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	public NameWithTitleFormatStrategy getNameWithTitleFormatStrategy()
	{
		return nameWithTitleFormatStrategy;
	}

	@Required
	public void setNameWithTitleFormatStrategy(final NameWithTitleFormatStrategy nameWithTitleFormatStrategy)
	{
		this.nameWithTitleFormatStrategy = nameWithTitleFormatStrategy;
	}

	protected ChineseCustomerAccountService getChineseCustomerAccountService()
	{
		return chineseCustomerAccountService;
	}

	@Required
	public void setChineseCustomerAccountService(final ChineseCustomerAccountService chineseCustomerAccountService)
	{
		this.chineseCustomerAccountService = chineseCustomerAccountService;
	}
}
