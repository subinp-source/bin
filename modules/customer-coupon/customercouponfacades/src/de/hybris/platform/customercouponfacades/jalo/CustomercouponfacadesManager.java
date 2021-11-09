/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.jalo;

import de.hybris.platform.customercouponfacades.constants.CustomercouponfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class CustomercouponfacadesManager extends GeneratedCustomercouponfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponfacadesManager.class.getName() );
	
	public static final CustomercouponfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponfacadesManager) em.getExtension(CustomercouponfacadesConstants.EXTENSIONNAME);
	}
	
}
