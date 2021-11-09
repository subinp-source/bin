/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.model;

import de.hybris.platform.acceleratorcms.model.restrictions.CMSActionRestrictionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;


/**
 * @deprecated since 1811
 */
@Deprecated(since = "1811", forRemoval = true)
@HybrisDeprecation(sinceVersion = "1811")
public class CMSActionRestrictionDynamicDescription implements DynamicAttributeHandler<String, CMSActionRestrictionModel>
{

	@Override
	public String get(final CMSActionRestrictionModel model)
	{
		final StringBuilder result = new StringBuilder();
		final String localizedString = Localization.getLocalizedString("type.CMSActionRestriction.description.text");
		result.append(localizedString == null ? "CMS Action only applies to the applicable cms components" : localizedString);

		return result.toString();
	}

	@Override
	public void set(final CMSActionRestrictionModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}
}
