/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.subscription.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.subscriptionservices.subscription.CustomerResolutionService;

import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nullable;


/**
 * Default implementation of the {@link CustomerResolutionService} using the {@link UserService} to resolve the current
 * customer.
 */
public class DefaultCustomerResolutionService implements CustomerResolutionService
{
	private UserService userService;
	private I18NService i18NService;

	@Override
	@Nullable
	public CustomerModel getCurrentCustomer()
	{
		if (getUserService().getCurrentUser() instanceof CustomerModel)
		{
			return (CustomerModel) userService.getCurrentUser();
		}
		return null;
	}

	@Override
	@Nullable
	public String getCurrencyIso()
	{
		if (getCurrentCustomer() != null && getCurrentCustomer().getSessionCurrency() != null)
		{
			return getCurrentCustomer().getSessionCurrency().getIsocode();
		}
		if (getI18NService().getCurrentJavaCurrency() != null)
		{
			return getI18NService().getCurrentJavaCurrency().getCurrencyCode();
		}
		return null;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18NService)
	{
		this.i18NService = i18NService;
	}

}
