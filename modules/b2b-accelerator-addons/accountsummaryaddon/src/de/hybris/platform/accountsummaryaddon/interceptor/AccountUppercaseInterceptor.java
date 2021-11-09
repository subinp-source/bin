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
public class AccountUppercaseInterceptor implements PrepareInterceptor
{


	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{

		final B2BDocumentModel doc = (B2BDocumentModel) model;


		if (doc.getDocumentNumber() != null)
		{
			doc.setDocumentNumber(doc.getDocumentNumber().toUpperCase());
		}


	}

}
