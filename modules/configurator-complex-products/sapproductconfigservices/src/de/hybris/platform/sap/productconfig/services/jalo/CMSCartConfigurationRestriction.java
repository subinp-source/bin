/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.localization.Localization;


/**
 * CMS Restruction for configurable products within cart
 */
public class CMSCartConfigurationRestriction extends GeneratedCMSCartConfigurationRestriction
{

	@Override
	public String getDescription(final SessionContext ctx)
	{
		return Localization.getLocalizedString("type.CMSCartConfigurationRestriction.description.text");
	}

}
