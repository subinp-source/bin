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
package de.hybris.platform.ruleenginebackoffice.search.builder;

import com.hybris.backoffice.cockpitng.search.builder.impl.GenericConditionQueryBuilder;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.GenericCondition;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Customization of {@link GenericConditionQueryBuilder} as a workaround to an inconsistency in filtering and validation
 * empty value logic. This should be a temporary fix and should be removed once addressed in
 * {@link com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchEngineController#getQueryEntry}
 */
public class RuleEngineGenericConditionQueryBuilder extends GenericConditionQueryBuilder
{
	@Override
	protected GenericCondition createSingleTokenCondition(final SearchQueryData searchQueryData,
			final SearchAttributeDescriptor searchAttributeDescriptor, final Object value,
			final ValueComparisonOperator givenOperator)
	{
		validateParameterNotNull(searchQueryData, "Parameter 'searchQueryData' must not be null!");
		validateParameterNotNull(searchQueryData.getSearchType(), "Parameter 'searchQueryData.searchType' must not be empty!");
		validateParameterNotNull(searchAttributeDescriptor, "Parameter 'searchAttributeDescriptor' must not be null!");

		final ValueComparisonOperator operator = givenOperator != null ? givenOperator :
				searchQueryData.getValueComparisonOperator(searchAttributeDescriptor);

		if (operator.isRequireValue() && value instanceof Collection && CollectionUtils.isEmpty((Collection<?>) value))
		{
			return null;
		}

		return super.createSingleTokenCondition(searchQueryData, searchAttributeDescriptor, value, givenOperator);
	}
}
