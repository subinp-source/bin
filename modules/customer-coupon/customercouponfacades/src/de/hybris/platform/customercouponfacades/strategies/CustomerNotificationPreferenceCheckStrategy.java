/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.strategies;

/**
 * Checks if the current customer subscribe any type of notification when saving a coupon notification
 */
public interface CustomerNotificationPreferenceCheckStrategy
{
	/**
	 * Checks if the current customer subscribe any type of notification
	 *
	 * @return true if the customer has subscribed to at least one notification type and false otherwise
	 */
	Boolean checkCustomerNotificationPreference();
}
