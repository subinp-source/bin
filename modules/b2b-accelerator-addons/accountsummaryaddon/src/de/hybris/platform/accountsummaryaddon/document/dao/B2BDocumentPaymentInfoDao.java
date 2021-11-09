/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.dao;

import de.hybris.platform.servicelayer.search.SearchResult;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentPaymentInfoModel;


public interface B2BDocumentPaymentInfoDao
{

	/**
	 * Gets a list of document payments associated to a Document.
	 * 
	 * @param documentNumber
	 *           the document number identification.
	 * @return list of documentPaymentInfos
	 */
	public SearchResult<B2BDocumentPaymentInfoModel> getDocumentPaymentInfo(final String documentNumber);

}
