/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.service.impl;

import de.hybris.platform.accountsummaryaddon.document.dao.B2BDocumentTypeDao;
import de.hybris.platform.accountsummaryaddon.document.service.B2BDocumentTypeService;
import de.hybris.platform.accountsummaryaddon.model.B2BDocumentTypeModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import org.springframework.beans.factory.annotation.Required;


/**
 * Provides services for B2B document type.
 *
 */
public class DefaultB2BDocumentTypeService implements B2BDocumentTypeService
{
	private B2BDocumentTypeDao b2bDocumentTypeDao;

	@Override
	public SearchResult<B2BDocumentTypeModel> getAllDocumentTypes()
	{
		return getB2bDocumentTypeDao().getAllDocumentTypes();
	}

	@Required
	public void setB2bDocumentTypeDao(final B2BDocumentTypeDao b2bDocumentTypeDao)
	{
		this.b2bDocumentTypeDao = b2bDocumentTypeDao;
	}

	protected B2BDocumentTypeDao getB2bDocumentTypeDao()
	{
		return b2bDocumentTypeDao;
	}
}
