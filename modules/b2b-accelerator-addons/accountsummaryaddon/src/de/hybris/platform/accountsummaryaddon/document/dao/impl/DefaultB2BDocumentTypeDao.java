/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.dao.impl;

import de.hybris.platform.accountsummaryaddon.document.dao.B2BDocumentTypeDao;
import de.hybris.platform.accountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;


public class DefaultB2BDocumentTypeDao extends DefaultGenericDao<B2BDocumentTypeModel> implements B2BDocumentTypeDao
{

	private static final String FIND_ALL_DOCUMENT_TYPE = "SELECT {" + B2BDocumentTypeModel._TYPECODE + ":pk}  FROM { "
			+ B2BDocumentTypeModel._TYPECODE + " } ";

	public DefaultB2BDocumentTypeDao()
	{
		super(B2BDocumentTypeModel._TYPECODE);
	}

	@Override
	public SearchResult<B2BDocumentTypeModel> getAllDocumentTypes()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_DOCUMENT_TYPE);
		return getFlexibleSearchService().search(query);
	}

}

