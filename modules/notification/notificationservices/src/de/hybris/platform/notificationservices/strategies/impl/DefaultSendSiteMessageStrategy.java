/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageForCustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.notificationservices.strategies.SendSiteMessageStrategy;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SendSiteMessageStrategy}
 */
public class DefaultSendSiteMessageStrategy implements SendSiteMessageStrategy
{

	private ModelService modelService;

	@Override
	public void sendMessage(final CustomerModel customer, final SiteMessageModel message)
	{
		final SiteMessageForCustomerModel messageForCustomer = getModelService().create(SiteMessageForCustomerModel.class);
		messageForCustomer.setCustomer(customer);
		messageForCustomer.setMessage(message);
		messageForCustomer.setSentDate(Calendar.getInstance().getTime());
		getModelService().save(messageForCustomer);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
