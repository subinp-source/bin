/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicepromotionaddon.jalo;

import de.hybris.platform.assistedservicepromotionaddon.constants.AssistedservicepromotionaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class AssistedservicepromotionaddonManager extends GeneratedAssistedservicepromotionaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( AssistedservicepromotionaddonManager.class.getName() );
	
	public static final AssistedservicepromotionaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AssistedservicepromotionaddonManager) em.getExtension(AssistedservicepromotionaddonConstants.EXTENSIONNAME);
	}
	
}
