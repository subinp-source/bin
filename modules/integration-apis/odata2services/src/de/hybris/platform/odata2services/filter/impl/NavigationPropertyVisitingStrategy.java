/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl;

import static de.hybris.platform.odata2services.filter.impl.WhereClauseConditionUtil.NO_RESULT_CONDITIONS;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchResult;
import de.hybris.platform.integrationservices.search.NavigationPropertyWhereClauseCondition;
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseConditions;
import de.hybris.platform.odata2services.filter.FilterProcessingException;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;

/**
 * This strategy creates a {@link WhereClauseCondition} from a navigation property's sub-property that is not an integrationKey.
 * For example, if filtering by catalogVersion/version eq 'Staged', this strategy looks up all CatalogVersions with
 * version = 'Staged' and creates a where clause condition containing the PKs of all the matching CatalogVersions.
 */
public class NavigationPropertyVisitingStrategy extends AbstractNavigationPropertyVisitingStrategy
{
	@Override
	public boolean isApplicable(final BinaryExpression expression, final BinaryOperator operator, final Object leftResult,
	                            final Object rightResult)
	{
		return leftResult instanceof EdmEntitySet && expression.getLeftOperand() instanceof MemberExpression &&
				!getLeftOperandPropertyName(expression).contains("integrationKey");
	}

	protected WhereClauseConditions createWhereClauseConditionWithOperator(final BinaryExpression expression,
	                                                                       final BinaryOperator operator,
	                                                                       final Object leftResult,
	                                                                       final Object rightResult)
	{
		try
		{
			final String property = getLeftOperandPropertyName(expression);
			final WhereClauseCondition condition = SimplePropertyWhereClauseCondition.withCompareOperator(property, rightResult,
					getOperatorConverter().convert(operator));
			final ItemSearchRequest itemLookupRequest = getItemLookupRequestFactory().createWithFilter(
					getContext(), (EdmEntitySet) leftResult, new WhereClauseConditions(condition));
			final ItemSearchResult<ItemModel> itemModels = getItemSearchService().findItems(itemLookupRequest);
			if (itemModels.getTotalCount().orElse(0) > 0)
			{
				final String navPropertyName = getLeftOperandNavPropertyName(expression);
				final String pks = itemModels.getItems()
				                             .stream()
				                             .map(m -> m.getPk().toString())
				                             .reduce("", (a, b) -> a + b + ",");
				return new NavigationPropertyWhereClauseCondition(navPropertyName,
						pks.substring(0, pks.length() - 1)).toWhereClauseConditions();
			}
		}
		catch (final EdmException e)
		{
			throw new FilterProcessingException(e);
		}
		return NO_RESULT_CONDITIONS;
	}
}
