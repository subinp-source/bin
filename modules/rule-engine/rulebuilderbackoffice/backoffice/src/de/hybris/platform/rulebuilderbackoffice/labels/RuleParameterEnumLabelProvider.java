/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.rulebuilderbackoffice.labels;

import de.hybris.platform.ruleengineservices.definitions.RuleParameterEnum;
import de.hybris.platform.servicelayer.i18n.L10NService;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.cockpitng.labels.LabelProvider;


public class RuleParameterEnumLabelProvider implements LabelProvider<RuleParameterEnum>
{
	private L10NService l10NService;

	public L10NService getL10NService()
	{
		return l10NService;
	}

	@Required
	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	@Override
	public String getLabel(final RuleParameterEnum ruleParameterEnum)
	{
		final String localizationKey = ruleParameterEnum.getClass().getName() + "." + ruleParameterEnum + ".name";
		return l10NService.getLocalizedString(localizationKey.toLowerCase(Locale.ENGLISH));
	}

	@Override
	public String getDescription(final RuleParameterEnum ruleParameterEnum)
	{
		return ruleParameterEnum.toString();
	}

	@Override
	public String getIconPath(final RuleParameterEnum ruleParameterEnum)
	{
		return null;
	}
}
