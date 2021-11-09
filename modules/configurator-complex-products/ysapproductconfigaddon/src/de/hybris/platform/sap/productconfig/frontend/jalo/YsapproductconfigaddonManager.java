/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.frontend.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;

import org.apache.log4j.Logger;


/**
 * YsapproductconfigaddonManager
 */
public class YsapproductconfigaddonManager extends GeneratedYsapproductconfigaddonManager
{
	private static final Logger LOG = Logger.getLogger(YsapproductconfigaddonManager.class.getName());

	/**
	 * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
	 * like registering a jalo session listener. Each manager is created once for each tenant.
	 */
	public YsapproductconfigaddonManager()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("constructor of ysapproductconfigaddonManager called.");
		}
	}

	/**
	 * @return YsapproductconfigaddonManager
	 */
	public static final YsapproductconfigaddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YsapproductconfigaddonManager) em.getExtension(SapproductconfigaddonConstants.EXTENSIONNAME);
	}
}
