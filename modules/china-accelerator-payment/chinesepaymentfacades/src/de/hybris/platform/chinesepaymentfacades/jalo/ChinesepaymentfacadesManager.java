/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.jalo;

import de.hybris.platform.chinesepaymentfacades.constants.ChinesepaymentfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepaymentfacadesManager extends GeneratedChinesepaymentfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepaymentfacadesManager.class.getName() );
	
	public static final ChinesepaymentfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepaymentfacadesManager) em.getExtension(ChinesepaymentfacadesConstants.EXTENSIONNAME);
	}
	
}
