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
package de.hybris.platform.ruleenginebackoffice.widgets;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;

import java.util.List;
import java.util.Optional;


public class RuleEngineAdvancedSearchInitializer implements AdvancedSearchInitializer
{

	public static final String ACTIVE = "currentVersion";

	@Override
	public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode)
	{
		if (nonNull(searchData))
		{
			final List<SearchConditionData> conditions = searchData.getConditions(ACTIVE);
			if (isNotEmpty(conditions))
			{
				conditions.clear();
			}
			final FieldType fieldType = new FieldType();
			fieldType.setDisabled(Boolean.FALSE);
			fieldType.setSelected(Boolean.TRUE);
			fieldType.setName(ACTIVE);
			searchData.addCondition(fieldType, ValueComparisonOperator.EQUALS, Boolean.TRUE);
		}
	}

}
