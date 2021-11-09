/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.attributehandlers;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.accountsummaryaddon.model.B2BDocumentPaymentInfoModel;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

public class B2BPaymentMethodDynamicAttributeHandler extends AbstractDynamicAttributeHandler<String, B2BDocumentPaymentInfoModel>
{
	private static final String ACCOUNTSUMMARY_CC_PAYMENT = "accountsummary.ccpayment";
	
	 @Override
	 public String get(final B2BDocumentPaymentInfoModel ruleSet)
	 {
		 final boolean usingDocument = StringUtils.isBlank(ruleSet.getCcTransactionNumber());
		 if (usingDocument)
		 {
			 return ruleSet.getUseDocument().getDocumentType().getName();
		 }
		 else
		 {
			 return Localization.getLocalizedString(ACCOUNTSUMMARY_CC_PAYMENT);
		 }
	 }
	 
	 @Override
	 public void set(final B2BDocumentPaymentInfoModel paramMODEL, final String paramVALUE)
	 {
	  // Ignore
	 }
}
