/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service.strategies;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Optional;


/**
 * Get language strategy
 */
public interface NotificationLanguageStrategy
{
	/**
	 * If chineseprofileaddon exist use the email language setting under personal detail page else use current site
	 * language
	 * 
	 * @return language
	 */
	Optional<LanguageModel> getNotificationLanguage(CustomerModel customer);
}
