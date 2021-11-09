/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * Describes request for searching item(s) existing in the platform.
 */
public interface ItemSearchRequest
{
	/**
	 * Retrieves pagination specification associated with the item search. If the pagination parameters are present, then items should
	 * be retrieved in the page (batch) mode according to that specification. Otherwise, all items should be retrieved.
	 *
	 * @return an {@code Optional} containing the {@code PaginationParameters}, if pagination was specified for this item lookup, or
	 * an {@code Optional.empty()} otherwise.
	 */
	Optional<PaginationParameters> getPaginationParameters();

	/**
	 * Specifies the metadata describing the requested item type.
	 *
	 * @return descriptor of an item type within the context integration object, for which data are being requested.
	 */
	@NotNull TypeDescriptor getTypeDescriptor();

	/**
	 * Retrieves integration item that contains key attributes of the item to be found.
	 *
	 * @return a search key represented as an item. If search should be done by an item key, then {@code IntegrationItem} containing the
	 * key attributes values must be present in this request. However, when search should result in multiple items found, then this
	 * method returns {@code Optional.empty()}.
	 */
	Optional<IntegrationItem> getRequestedItem();

	/**
	 * Specifies conditions the items to retrieve should match.
	 *
	 * @return conditions to be used for searching requested items.
	 */
	WhereClauseConditions getFilter();

	/**
	 * Specifies whether total number of items matching this request should be included in the response or not.
	 *
	 * @return {@code true}, if the search result must include total number of matching items; {@code false}, if the search result
	 * needs to contain item(s) only and does not need total count. Keep in mind that requesting total count to be included in the
	 * search result may incur performance penalty.
	 */
	boolean includeTotalCount();

	/**
	 * Specifies whether the search response should include number of matching items only.
	 *
	 * @return {@code true}, if the search result should contain only count of the matching items but no matching items themselves;
	 * {@code false}, if the items must be included. Note, {@code true} result of this call implies that {@code includeTotalCount()} also
	 * returns {@code true}.
	 */
	boolean isCountOnly();

	/**
	 * Specifies conditions the items to retrieve should be ordered by
	 *
	 * @return order by expression list {@code List<OrderExpression>}
	 */
	default List<OrderExpression> getOrderBy()
	{
		return Collections.emptyList();
	}

	/**
	 * Get accepted locale extracted from the "Accept-Language" header locale associated with the request.
	 *
	 * @return accepted locale {@code Locale}
	 */
	default Locale getAcceptLocale()
	{
		return Locale.ENGLISH;
	}

}
