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

import de.hybris.platform.odata2services.odata.processor.handler.persistence.PersistenceParam;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * A factory for creating {@link StorageRequest} instances.
 */
public interface StorageRequestFactory
{
	/**
	 * Creates new storage request
	 * @param param parameters for the storage request
	 * @param entry payload entry associated with the request
	 * @return new instance of the storage request
	 */
	default StorageRequest create(final PersistenceParam param, final ODataEntry entry) {
		return create(param.getContext(), param.getResponseContentType(), param.getEntitySet(), entry);
	}

	/**
	 * Get the StorageRequest for persistence processing
	 * @param oDataContext The ODataContext to be used.
	 * @param responseContentType contentType to use.
	 * @param entitySet EdmEntitySet to use.
	 * @param entry ODataEntry to use.
	 * @return The StorageRequest used for persistence
	 */
	StorageRequest create(ODataContext oDataContext, String responseContentType, EdmEntitySet entitySet, ODataEntry entry);
}
