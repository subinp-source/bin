/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingsmarteditConstants;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class MerchandisingsmarteditManager extends GeneratedMerchandisingsmarteditManager
{
	public static final MerchandisingsmarteditManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MerchandisingsmarteditManager) em.getExtension(MerchandisingsmarteditConstants.EXTENSIONNAME);
	}
}
