/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.runtime.mock.constants.SapproductconfigruntimemockConstants;


/**
 * Jalo Manager class for <code>sapproductconfigruntimemock</code> extension.
 */
public class SapproductconfigruntimemockManager extends GeneratedSapproductconfigruntimemockManager
{

	/**
	 * factory-method for this class
	 *
	 * @return manager instance
	 */
	public static final SapproductconfigruntimemockManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SapproductconfigruntimemockManager) em.getExtension(SapproductconfigruntimemockConstants.EXTENSIONNAME);
	}

}
