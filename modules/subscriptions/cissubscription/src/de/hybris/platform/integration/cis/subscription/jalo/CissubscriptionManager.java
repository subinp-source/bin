/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.jalo;

import de.hybris.platform.integration.cis.subscription.constants.CissubscriptionConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CissubscriptionManager extends GeneratedCissubscriptionManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( CissubscriptionManager.class.getName() );
	
	public static final CissubscriptionManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CissubscriptionManager) em.getExtension(CissubscriptionConstants.EXTENSIONNAME);
	}
	
}
