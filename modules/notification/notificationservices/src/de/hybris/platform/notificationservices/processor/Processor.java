/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.processor;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Map;


/**
 * Interface for channel processor
 */
public interface Processor
{


	/**
	 * Render and send the notification
	 *
	 * @param customer
	 *           the customer to send the result
	 * @param dataMap
	 *           the map containing variables
	 */
	void process(final CustomerModel customer, final Map<String, ? extends ItemModel> dataMap);
}
