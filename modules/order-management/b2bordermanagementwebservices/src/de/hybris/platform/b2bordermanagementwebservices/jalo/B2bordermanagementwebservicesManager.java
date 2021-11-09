/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.b2bordermanagementwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.b2bordermanagementwebservices.constants.B2bordermanagementwebservicesConstants;

@SuppressWarnings("PMD")
public class B2bordermanagementwebservicesManager extends GeneratedB2bordermanagementwebservicesManager
{
	public static final B2bordermanagementwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2bordermanagementwebservicesManager) em.getExtension(B2bordermanagementwebservicesConstants.EXTENSIONNAME);
	}
}
