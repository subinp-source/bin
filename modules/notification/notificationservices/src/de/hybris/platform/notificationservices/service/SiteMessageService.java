/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageForCustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageModel;

import java.util.List;
import java.util.Locale;


/**
 * Service to provide methods for site message
 */
public interface SiteMessageService
{

	/**
	 * Gets paginated site messages for the given type
	 *
	 * @param customer
	 *           the specific customer
	 * @param type
	 *           the given message type
	 * @param searchPageData
	 *           pagination parameters
	 * @return paginated result
	 */
	SearchPageData<SiteMessageForCustomerModel> getPaginatedMessagesForType(CustomerModel customer, SiteMessageType type,
			SearchPageData searchPageData);

	/**
	 * Gets all paginated site messages
	 *
	 * @param customer
	 *           the specific customer
	 * @param searchPageData
	 *           pagination parameters
	 * @return paginated result
	 */
	SearchPageData<SiteMessageForCustomerModel> getPaginatedMessagesForCustomer(CustomerModel customer, SearchPageData searchPageData);

	/**
	 * Creates a site message by given title, link, content, message type, notification type and locale
	 * 
	 * @deprecated since 1905, replace with createMessage(String title, String content, SiteMessageType type, ItemModel
	 *             externalItem, NotificationType notificationType)
	 *
	 * @param title
	 *           the message title
	 * @param content
	 *           the message content
	 * @param type
	 *           the message type
	 * @param externalItem
	 *           the related item model
	 * @param notificationType
	 *           the notification type
	 * @param locale
	 *           the locale, if null, default current locale
	 * @return the created message model
	 */
	@Deprecated(since = "1905", forRemoval= true )
	SiteMessageModel createMessage(String title, String content, SiteMessageType type, ItemModel externalItem,
			NotificationType notificationType, Locale locale);

	/**
	 * Creates a site message by given subject, link, body, message type, notification type and locale
	 *
	 * @param subject
	 *           the message subject
	 * @param body
	 *           the message body
	 * @param type
	 *           the message type
	 * @param externalItem
	 *           the related item model
	 * @param notificationType
	 *           the notification type
	 * @return the created message model
	 */
	SiteMessageModel createMessage(String subject, String body, SiteMessageType type, ItemModel externalItem,
			NotificationType notificationType);

	/**
	 * Gets all site message of the customer
	 * 
	 * @param customer
	 *           the specific customer
	 * @return list of site message for customer
	 */
	List<SiteMessageForCustomerModel> getSiteMessagesForCustomer(CustomerModel customer);
}
