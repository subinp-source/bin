/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.ordermanagementwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants;

@SuppressWarnings("PMD")
public class OrdermanagementwebservicesManager extends GeneratedOrdermanagementwebservicesManager
{
	public static final OrdermanagementwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (OrdermanagementwebservicesManager) em.getExtension(OrdermanagementwebservicesConstants.EXTENSIONNAME);
	}
	
}
