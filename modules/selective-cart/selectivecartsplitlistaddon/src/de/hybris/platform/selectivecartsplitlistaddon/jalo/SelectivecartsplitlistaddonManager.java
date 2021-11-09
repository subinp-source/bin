/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartsplitlistaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.selectivecartsplitlistaddon.constants.SelectivecartsplitlistaddonConstants;
import org.apache.log4j.Logger;

public class SelectivecartsplitlistaddonManager extends GeneratedSelectivecartsplitlistaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SelectivecartsplitlistaddonManager.class.getName() );
	
	public static final SelectivecartsplitlistaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SelectivecartsplitlistaddonManager) em.getExtension(SelectivecartsplitlistaddonConstants.EXTENSIONNAME);
	}
	
}
