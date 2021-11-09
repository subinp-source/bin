/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.b2bfrontend.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.b2bfrontend.constants.Sapproductconfigb2baddonConstants;


/**
 * Jalo Manager class for <code>sapproductconfigb2baddon</code> extension.
 */
public class Sapproductconfigb2baddonManager extends GeneratedSapproductconfigb2baddonManager
{

	/**
	 * factory-method for this class
	 *
	 * @return manager instance
	 */
	public static final Sapproductconfigb2baddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Sapproductconfigb2baddonManager) em.getExtension(Sapproductconfigb2baddonConstants.EXTENSIONNAME);
	}

}
