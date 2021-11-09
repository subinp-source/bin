/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.dao;


import de.hybris.platform.servicelayer.search.SearchResult;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentTypeModel;


public interface B2BDocumentTypeDao
{

	/**
	 * Gets all document types.
	 * 
	 * @return a SearchResult<B2BDocumentTypeModel> containing a list of all document types.
	 */
	SearchResult<B2BDocumentTypeModel> getAllDocumentTypes();
}
