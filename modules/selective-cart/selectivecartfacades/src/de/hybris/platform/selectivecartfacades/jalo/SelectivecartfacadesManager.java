/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.selectivecartfacades.constants.SelectivecartfacadesConstants;
import org.apache.log4j.Logger;

public class SelectivecartfacadesManager extends GeneratedSelectivecartfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SelectivecartfacadesManager.class.getName() );
	
	public static final SelectivecartfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SelectivecartfacadesManager) em.getExtension(SelectivecartfacadesConstants.EXTENSIONNAME);
	}
	
}
