/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants;
import org.apache.log4j.Logger;

public class GeneratedSmarteditwebservicesManager extends GeneratedGeneratedSmarteditwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( GeneratedSmarteditwebservicesManager.class.getName() );
	
	public static final GeneratedSmarteditwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (GeneratedSmarteditwebservicesManager) em.getExtension(SmarteditwebservicesConstants.EXTENSIONNAME);
	}
	
}
