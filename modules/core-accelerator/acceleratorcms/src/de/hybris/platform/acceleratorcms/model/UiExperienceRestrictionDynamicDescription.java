/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.model;

import de.hybris.platform.acceleratorcms.model.restrictions.CMSUiExperienceRestrictionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.localization.Localization;

import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated since 1811
 */
@Deprecated(since = "1811", forRemoval = true)
@HybrisDeprecation(sinceVersion = "1811")
public class UiExperienceRestrictionDynamicDescription implements DynamicAttributeHandler<String, CMSUiExperienceRestrictionModel>
{
	private TypeService typeService;

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Override
	public String get(final CMSUiExperienceRestrictionModel model)
	{
		final UiExperienceLevel uiExperience = model.getUiExperience();

		final StringBuilder result = new StringBuilder();
		if (uiExperience != null)
		{
			final EnumerationValueModel enumerationValue = getTypeService().getEnumerationValue(uiExperience);
			if (enumerationValue != null)
			{
				final String localizedString = Localization.getLocalizedString("type.CMSUiExperienceRestriction.description.text");
				result.append(localizedString == null ? "Page only applies on experience level:" : localizedString);
				result.append(' ').append(enumerationValue.getName());
			}
		}

		return result.toString();
	}

	@Override
	public void set(final CMSUiExperienceRestrictionModel model, final String value)
	{
		throw new UnsupportedOperationException();
	}
}
