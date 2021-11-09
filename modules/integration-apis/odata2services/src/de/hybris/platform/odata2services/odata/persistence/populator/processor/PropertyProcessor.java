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
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * Responsible for populating an {@code ODataEntry} from an {@code ItemModel}.
 */
public interface PropertyProcessor
{
	/**
	 * Populates properties of the specified {@code ItemModel}
	 * @param itemModel an item to populate
	 * @param request context information needed for knowing which properties should be populated by the implementations.
	 * @throws EdmException when access to EDM unexpectedly fails.
	 * @deprecated This responsibility is moved to {@link AttributePopulator}. Implement this interface from now on.
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	void processItem(ItemModel itemModel, StorageRequest request) throws EdmException;

	/**
	 * Populates properties of the specified {@code ODataEntry}.
	 * @param oDataEntry an entry to be populated from an {@link de.hybris.platform.core.model.ItemModel}
	 * @param request a request carrying all context information needed for correct population of the entry by a specific
	 * implementation of the property populator.
	 * @throws EdmException when unexpected problems occur while accessing the EDM info.
	 */
	void processEntity(ODataEntry oDataEntry, ItemConversionRequest request) throws EdmException;
}
