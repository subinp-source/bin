/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.dao;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;



/**
 * manipulate Customer lated to notification
 */
public interface NotifyCustomerDao
{

	/**
	 * find customers which are created before the install of notificationservices
	 */
	List<CustomerModel> findOldCustomer();

}