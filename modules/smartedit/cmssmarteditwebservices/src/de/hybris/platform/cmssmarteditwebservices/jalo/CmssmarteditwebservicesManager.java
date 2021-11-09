/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;

@SuppressWarnings("PMD")
public class CmssmarteditwebservicesManager extends GeneratedCmssmarteditwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( CmssmarteditwebservicesManager.class.getName() );
	
	public static final CmssmarteditwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CmssmarteditwebservicesManager) em.getExtension(CmssmarteditwebservicesConstants.EXTENSIONNAME);
	}
	
}
