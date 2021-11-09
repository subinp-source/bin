/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.attributehandlers;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentPaymentInfoModel;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

public class B2BUseDocumentReferenceDynamicAttributeHandler extends AbstractDynamicAttributeHandler<String, B2BDocumentPaymentInfoModel>
{
	 @Override
	 public String get(final B2BDocumentPaymentInfoModel ruleSet)
	 {
			final boolean usingDocument = StringUtils.isBlank(ruleSet.getCcTransactionNumber());

			return usingDocument ? ruleSet.getUseDocument().getDocumentNumber() : ruleSet.getCcTransactionNumber();
	 }
	 
	 @Override
	 public void set(final B2BDocumentPaymentInfoModel paramMODEL, final String paramVALUE)
	 {
	  // Ignore
	 }
}
