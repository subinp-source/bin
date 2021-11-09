/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.service;

import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import de.hybris.platform.accountsummaryaddon.document.data.B2BDragAndDropData;
import de.hybris.platform.accountsummaryaddon.model.B2BDocumentPaymentInfoModel;


/**
 * Provides services for B2BDocument payment info.
 *
 */
public interface B2BDocumentPaymentInfoService
{

	/**
	 * Gets a list of document payment info associated to a Document.
	 *
	 * @param documentNumber
	 *           the document number
	 * @return list of documentPaymentInfos
	 */
	public SearchResult<B2BDocumentPaymentInfoModel> getDocumentPaymentInfo(final String documentNumber);

	/**
	 *
	 * Applies a list of drag & drop actions
	 *
	 * @param lstActions
	 *           the list of actions to be applied.
	 */
	public void applyPayment(List<B2BDragAndDropData> lstActions);


}
