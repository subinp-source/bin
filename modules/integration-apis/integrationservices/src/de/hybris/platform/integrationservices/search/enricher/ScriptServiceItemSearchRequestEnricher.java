/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.enricher;

import de.hybris.platform.integrationservices.search.ConjunctiveOperator;
import de.hybris.platform.integrationservices.search.ImmutableItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.SimplePropertyWhereClauseCondition;
import de.hybris.platform.scripting.model.ScriptModel;

import javax.validation.constraints.NotNull;

/**
 * For Scripts, Commerce keeps a history of the Script when the content changes, i.e.
 * a new item is created for the new content and is marked active. The previous active
 * one is now marked as inactive. When searching just with the code, The ScriptService may
 * fail with a non unique item error because Commerce returns all the Scripts instead of
 * just the active one. This class enriches the search so only the active ones are returned
 * when using the ScriptService.
 */
public class ScriptServiceItemSearchRequestEnricher implements ItemSearchRequestEnricher
{
	@Override
	public ItemSearchRequest enrich(@NotNull final ItemSearchRequest request)
	{
		if (isScriptFromScriptService(request))
		{
			final var activeCondition = SimplePropertyWhereClauseCondition.eq("active", 1);
			final var enrichedConditions = activeCondition.toWhereClauseConditions()
			                                              .join(request.getFilter(), ConjunctiveOperator.AND);
			return ImmutableItemSearchRequest.itemSearchRequestBuilder(request)
			                                 .withFilter(enrichedConditions)
			                                 .build();
		}
		return request;
	}

	private boolean isScriptFromScriptService(final ItemSearchRequest request)
	{
		return ScriptModel._TYPECODE.equals(request.getTypeDescriptor().getTypeCode()) &&
				"ScriptService".equals(request.getTypeDescriptor().getIntegrationObjectCode());
	}
}