/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.filter.impl;

import static de.hybris.platform.odata2services.filter.impl.WhereClauseConditionUtil.NO_RESULT_CONDITIONS;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.NavigationPropertyWithIntegrationKeyWhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseCondition;
import de.hybris.platform.integrationservices.search.WhereClauseConditions;
import de.hybris.platform.odata2services.filter.FilterProcessingException;
import de.hybris.platform.odata2services.filter.IntegrationKeyNestedFilteringNotSupportedException;
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator;

import java.util.Optional;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;

/**
 * This strategy creates a {@link WhereClauseCondition} from a navigation property's integrationKey.
 * For example, if filtering by catalogVersion/integrationKey eq 'Staged|Default', this strategy looks up the CatalogVersion with
 * version = 'Staged' and catalog.id = 'Default', then creates a where clause condition containing the CatalogVersion's PK.
 */
public class NavigationPropertyWithIntegrationKeyVisitingStrategy extends AbstractNavigationPropertyVisitingStrategy
{
	private IntegrationKeyToODataEntryGenerator integrationKeyConverter;

	@Override
	public boolean isApplicable(final BinaryExpression expression, final BinaryOperator operator, final Object leftResult,
	                            final Object rightResult)
	{
		if (leftResult instanceof EdmEntitySet && expression.getLeftOperand() instanceof MemberExpression &&
				getLeftOperandPropertyName(expression).contains("integrationKey"))
		{
			if (BinaryOperator.EQ == operator)
			{
				return true;
			}
			throw new IntegrationKeyNestedFilteringNotSupportedException();
		}
		return false;
	}

	@Override
	protected WhereClauseConditions createWhereClauseConditionWithOperator(final BinaryExpression expression,
	                                                                       final BinaryOperator operator, final Object leftResult,
	                                                                       final Object rightResult)
	{
		try
		{
			final EdmEntitySet entitySet = (EdmEntitySet) leftResult;
			final String integrationKey = (String) rightResult;
			final ODataEntry entry = getIntegrationKeyConverter().generate(entitySet, integrationKey);
			final ItemSearchRequest itemLookupRequest = getItemLookupRequestFactory().create(getContext(), entitySet, entry,
					integrationKey);
			final Optional<ItemModel> itemModel = getItemSearchService().findUniqueItem(itemLookupRequest);
			return itemModel
					.map(item -> toWhereClauseConditions(expression, item))
					.orElse(NO_RESULT_CONDITIONS);
		}
		catch (final EdmException e)
		{
			throw new FilterProcessingException(e);
		}
	}

	private WhereClauseConditions toWhereClauseConditions(final BinaryExpression expression, final ItemModel itemModel)
	{
		final String navPropertyName = getLeftOperandNavPropertyName(expression);
		return new NavigationPropertyWithIntegrationKeyWhereClauseCondition(navPropertyName, itemModel.getPk().toString())
				.toWhereClauseConditions();
	}

	protected IntegrationKeyToODataEntryGenerator getIntegrationKeyConverter()
	{
		return integrationKeyConverter;
	}

	public void setIntegrationKeyConverter(final IntegrationKeyToODataEntryGenerator integrationKeyConverter)
	{
		this.integrationKeyConverter = integrationKeyConverter;
	}
}
