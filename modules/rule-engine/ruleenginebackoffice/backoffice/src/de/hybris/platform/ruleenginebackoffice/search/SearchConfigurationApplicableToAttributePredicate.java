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
package de.hybris.platform.ruleenginebackoffice.search;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import org.springframework.beans.factory.annotation.Required;

import java.util.function.Predicate;


public class SearchConfigurationApplicableToAttributePredicate implements Predicate<DataAttribute>
{
	private AdvancedSearchConfiguration configuration;

	@Override
	public boolean test(final DataAttribute attribute)
	{
		return getConfiguration().getApplicableTypeCodes().contains(attribute.getValueType().getCode());
	}

	protected AdvancedSearchConfiguration getConfiguration()
	{
		return configuration;
	}

	@Required
	public void setConfiguration(final AdvancedSearchConfiguration configuration)
	{
		this.configuration = configuration;
	}
}
