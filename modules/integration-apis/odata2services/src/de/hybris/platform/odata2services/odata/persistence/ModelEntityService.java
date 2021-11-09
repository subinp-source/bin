/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;


/**
 * Service to create or update platform items based on an ODataEntry
 */
public interface ModelEntityService
{
	/**
	 * create or update platform items based on an ODataEntry
	 *
	 * @param request Parameter object that holds values for creating or updating an item.
	 * @param createItemStrategy strategy used to create non-existing items
	 * @throws EdmException in case there is an OData related issue
	 * @return the ItemModel that was created or updated
	 * @deprecated use one of the {@link de.hybris.platform.inboundservices.persistence.ContextItemModelService} methods depending
	 * on whether the item creation is allowed or not. Passing item creation strategy is not needed anymore.
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	ItemModel createOrUpdateItem(StorageRequest request, CreateItemStrategy createItemStrategy) throws EdmException;

	/**
	 * gets an itemModel based on the given information.
	 * @param lookupRequest Parameter object that holds values for getting an itemModel
	 * @return the itemModel
	 * @throws EdmException in case there is an OData related issue
	 *
	 * @deprecated use {@link ItemSearchService findUniqueItem(ItemSearchRequest)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	ItemModel lookup(ItemLookupRequest lookupRequest) throws EdmException;

	/**
	 * Searches for item models matching the conditions in the specified request.
	 * @param lookupRequest Parameter object that holds values for getting an itemModel
	 * @return result searching item models by the specified request
	 * @throws EdmException in case there is an OData related issue
	 *
	 * @deprecated use {@link ItemSearchService findItems(ItemSearchRequest)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	ItemLookupResult<ItemModel> lookupItems(ItemLookupRequest lookupRequest) throws EdmException;

	/**
	 * Converts an itemModel into a ODataEntry
	 * @param conversionRequest Parameter object that holds values for getting an ODataEntry
	 * @return The ODataEntry representation
	 * @throws EdmException in case there is an OData related issue
	 */
	ODataEntry getODataEntry(ItemConversionRequest conversionRequest) throws EdmException;

	/**
	 * Adds the alias value for an item's integrationKey to the oDataEntry
	 * @param entitySet Edmx representation of the item
	 * @param oDataEntry request body represented as xml or json
	 * @return The calculated integration key.
	 * @deprecated since 1905. Use an {@link de.hybris.platform.odata2services.odata.schema.IntegrationKeyGenerator} to generate the
	 * key value and instead of accessing the {@link de.hybris.platform.integrationservices.constants.IntegrationservicesConstants#INTEGRATION_KEY_PROPERTY_NAME}
	 * attribute from the {@link ODataEntry} use {@link IntegrationItem#getIntegrationKey()} - the integration item is present in
	 * the request and should be used instead of ODataEntry.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	String addIntegrationKeyToODataEntry(EdmEntitySet entitySet, ODataEntry oDataEntry);

	/**
	 * Counts how many items in the platform match the provided request conditions.
	 * @param lookupRequest a request specifying an item type, at a minimum, and possibly other conditions. For example, the
	 * request may point to the objects nested in the request's base item type and referred by the navigation segments.
	 * @return number of items in the platform matching the request conditions.
	 * @throws EdmException in case there is an OData related issue
	 */
	int count(ItemLookupRequest lookupRequest) throws EdmException;
}
