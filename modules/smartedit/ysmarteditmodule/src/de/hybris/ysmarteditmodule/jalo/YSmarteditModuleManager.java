/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.ysmarteditmodule.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.ysmarteditmodule.constants.YSmarteditModuleConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class YSmarteditModuleManager extends GeneratedYSmarteditModuleManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( YSmarteditModuleManager.class.getName() );
	
	public static final YSmarteditModuleManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YSmarteditModuleManager) em.getExtension(YSmarteditModuleConstants.EXTENSIONNAME);
	}
	
}
