/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import de.hybris.platform.accountsummaryaddon.document.dao.B2BDocumentPaymentInfoDao;
import de.hybris.platform.accountsummaryaddon.model.B2BDocumentModel;
import de.hybris.platform.accountsummaryaddon.model.B2BDocumentPaymentInfoModel;


public class DefaultB2BDocumentPaymentInfoDao extends DefaultGenericDao<B2BDocumentPaymentInfoModel> implements
		B2BDocumentPaymentInfoDao
{

	private static final String DOCUMENT_NUMBER = "documentNumber";
	private static final String FLEX_QUERY_PAYMENT_INFO = String
			.format(
			"select {b1:pk} from { %s as b1 join %s as b2 ON {b1:payDocument}={b2:pk} } WHERE {b2:documentNumber}=?documentNumber ORDER BY {b1:date} DESC",
			B2BDocumentPaymentInfoModel._TYPECODE, B2BDocumentModel._TYPECODE, B2BDocumentModel._TYPECODE);

	public DefaultB2BDocumentPaymentInfoDao()
	{
		super(B2BDocumentPaymentInfoModel._TYPECODE);
	}

	@Override
	public SearchResult<B2BDocumentPaymentInfoModel> getDocumentPaymentInfo(final String documentNumber)
	{

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FLEX_QUERY_PAYMENT_INFO);
		query.addQueryParameter(DOCUMENT_NUMBER, documentNumber);

		return getFlexibleSearchService().search(query);
	}
}
