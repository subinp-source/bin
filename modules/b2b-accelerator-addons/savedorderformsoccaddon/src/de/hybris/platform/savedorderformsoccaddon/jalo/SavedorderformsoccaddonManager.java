/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.savedorderformsoccaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.savedorderformsoccaddon.constants.SavedorderformsoccaddonConstants;
import org.apache.log4j.Logger;

public class SavedorderformsoccaddonManager extends GeneratedSavedorderformsoccaddonManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( SavedorderformsoccaddonManager.class.getName() );
	
	public static final SavedorderformsoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SavedorderformsoccaddonManager) em.getExtension(SavedorderformsoccaddonConstants.EXTENSIONNAME);
	}
	
}
