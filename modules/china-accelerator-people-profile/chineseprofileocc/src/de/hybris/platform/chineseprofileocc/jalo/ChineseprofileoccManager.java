/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileocc.jalo;

import de.hybris.platform.chineseprofileocc.constants.ChineseprofileoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChineseprofileoccManager extends GeneratedChineseprofileoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChineseprofileoccManager.class.getName() );
	
	public static final ChineseprofileoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChineseprofileoccManager) em.getExtension(ChineseprofileoccConstants.EXTENSIONNAME);
	}
	
}
