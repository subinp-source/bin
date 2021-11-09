/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookbackoffice.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.webhookbackoffice.constants.WebhookbackofficeConstants;
import org.apache.log4j.Logger;

public class WebhookbackofficeManager extends GeneratedWebhookbackofficeManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( WebhookbackofficeManager.class.getName() );
	
	public static final WebhookbackofficeManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WebhookbackofficeManager) em.getExtension(WebhookbackofficeConstants.EXTENSIONNAME);
	}
	
}
