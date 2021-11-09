/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicepromotionfacades.jalo;

import de.hybris.platform.assistedservicepromotionfacades.constants.AssistedservicepromotionfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AssistedservicepromotionfacadesManager extends GeneratedAssistedservicepromotionfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( AssistedservicepromotionfacadesManager.class.getName() );
	
	public static final AssistedservicepromotionfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AssistedservicepromotionfacadesManager) em.getExtension(AssistedservicepromotionfacadesConstants.EXTENSIONNAME);
	}
	
}
