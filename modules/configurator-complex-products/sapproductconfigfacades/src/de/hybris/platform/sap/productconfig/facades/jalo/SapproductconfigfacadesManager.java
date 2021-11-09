/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.productconfig.facades.constants.SapproductconfigfacadesConstants;


/**
 * Jalo Manager class for <code>sapproductconfigfacade</code> extension.
 */
public class SapproductconfigfacadesManager extends GeneratedSapproductconfigfacadesManager
{

	/**
	 * factory-method for this class
	 *
	 * @return manager instance
	 */
	public static final SapproductconfigfacadesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SapproductconfigfacadesManager) em.getExtension(SapproductconfigfacadesConstants.EXTENSIONNAME);
	}

}
