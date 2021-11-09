/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentModel;


/**
 *
 * Interceptor that formats a document number to uppercase.
 *
 */
public class B2BDocumentPrepareInterceptor implements PrepareInterceptor
{


	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{

		final B2BDocumentModel doc = (B2BDocumentModel) model;


		// make document number always uppercase
		if (doc.getDocumentNumber() != null)
		{
			doc.setDocumentNumber(doc.getDocumentNumber().toUpperCase());
		}

		// if not set, open amount = amount
		if (doc.getOpenAmount() == null)
		{
			doc.setOpenAmount(doc.getAmount());
		}

	}

}
