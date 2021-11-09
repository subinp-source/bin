/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.cmssmartedit.jalo;

import de.hybris.cmssmartedit.constants.CmsSmarteditConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CmsSmarteditManager extends GeneratedCmsSmarteditManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( CmsSmarteditManager.class.getName() );
	
	public static final CmsSmarteditManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CmsSmarteditManager) em.getExtension(CmsSmarteditConstants.EXTENSIONNAME);
	}
	
}
