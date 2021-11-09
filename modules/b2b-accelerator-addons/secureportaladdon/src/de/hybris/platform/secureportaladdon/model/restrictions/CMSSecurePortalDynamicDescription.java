/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.model.restrictions;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;


public class CMSSecurePortalDynamicDescription implements DynamicAttributeHandler<String, CMSSecurePortalRestrictionModel>
{

	@Override
	public String get(final CMSSecurePortalRestrictionModel model)
	{

		final StringBuilder result = new StringBuilder();
		{
			final String localizedString = Localization.getLocalizedString("type.CMSSecurePortalRestriction.description.text");
			result.append(localizedString == null ? "CMS Action only applies to the applicable cms components" : localizedString);

		}

		return result.toString();
	}

	@Override
	public void set(final CMSSecurePortalRestrictionModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}
}
