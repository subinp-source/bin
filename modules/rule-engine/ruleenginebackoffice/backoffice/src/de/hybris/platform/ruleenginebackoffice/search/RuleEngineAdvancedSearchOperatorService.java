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

import com.hybris.backoffice.cockpitng.search.DefaultAdvancedSearchOperatorService;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;


/**
 * Customization of {@link com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService} to accommodate
 * comparison operator replacements for enum types on top of the list provided by {@link DefaultAdvancedSearchOperatorService}
 */
public class RuleEngineAdvancedSearchOperatorService extends DefaultAdvancedSearchOperatorService
{
	private AdvancedSearchConfiguration configuration;

	private Predicate<DataAttribute> isApplicableCondition;

	@Override
	public Collection<ValueComparisonOperator> getAvailableOperators(final DataAttribute dataAttribute)
	{
		Collection<ValueComparisonOperator> operators = super.getAvailableOperators(dataAttribute);

		if (dataAttribute.getDefinedType().isEnum() && getIsApplicableCondition().test(dataAttribute))
		{
			operators = operators.stream()
					.map(operator -> getConfiguration().getOperatorReplacements().getOrDefault(operator, operator))
					.distinct().collect(toList());
		}

		return operators;
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

	protected Predicate<DataAttribute> getIsApplicableCondition()
	{
		return isApplicableCondition;
	}

	@Required
	public void setIsApplicableCondition(final Predicate<DataAttribute> isApplicableCondition)
	{
		this.isApplicableCondition = isApplicableCondition;
	}
}
