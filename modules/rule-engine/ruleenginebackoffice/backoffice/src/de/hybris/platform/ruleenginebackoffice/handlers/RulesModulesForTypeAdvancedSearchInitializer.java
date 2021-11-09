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
package de.hybris.platform.ruleenginebackoffice.handlers;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.ruleengine.enums.RuleType;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Optional;

import static de.hybris.platform.ruleengine.model.AbstractRulesModuleModel.RULETYPE;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;


/**
 * Advanced search initializer for rules modules that performs filtering based on RuleType
 */
public class RulesModulesForTypeAdvancedSearchInitializer implements AdvancedSearchInitializer
{
	private RuleType ruleType;

	@Override
	public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode)
	{
		if (nonNull(searchData))
		{
			removeExistingCondition(searchData);

			final FieldType fieldType = new FieldType();
			fieldType.setDisabled(Boolean.FALSE);
			fieldType.setSelected(Boolean.TRUE);
			fieldType.setName(RULETYPE);
			searchData.addCondition(fieldType, ValueComparisonOperator.EQUALS, getRuleType());
		}
	}

	protected void removeExistingCondition(final AdvancedSearchData searchData)
	{
		final List<SearchConditionData> conditions = searchData.getConditions(RULETYPE);
		if (isNotEmpty(conditions))
		{
			conditions.clear();
		}
	}

	public RuleType getRuleType()
	{
		return ruleType;
	}

	@Required
	public void setRuleType(final RuleType ruleType)
	{
		this.ruleType = ruleType;
	}
}
