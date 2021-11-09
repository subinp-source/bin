/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service.strategies.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.service.strategies.NotificationLanguageStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Optional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultEmailNotificationLanguageStrategy implements NotificationLanguageStrategy
{
	private CommonI18NService commonI18NService;

	@Override
	public Optional<LanguageModel> getNotificationLanguage(final CustomerModel customer)
	{
		try
		{
			final String emailLanguage = (String) PropertyUtils.getProperty(customer, "emailLanguage");
			return Optional.of(commonI18NService.getLanguage(emailLanguage));
		}
		catch (final Exception e)//NOSONAR
		{
			return Optional.empty();
		}
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}



}
