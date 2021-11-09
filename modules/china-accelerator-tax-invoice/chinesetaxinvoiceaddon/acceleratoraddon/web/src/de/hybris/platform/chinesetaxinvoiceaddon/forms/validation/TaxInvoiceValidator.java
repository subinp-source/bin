/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceaddon.forms.validation;

import de.hybris.platform.chinesetaxinvoiceaddon.forms.TaxInvoiceForm;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceRecipientType;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component("taxInvoiceValidator")
public class TaxInvoiceValidator implements Validator
{
	private static final int MAX_FIELD_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return TaxInvoiceForm.class == clazz;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final TaxInvoiceForm invoiceForm = (TaxInvoiceForm) object;
		validateNotNullField(invoiceForm.getCategory(), InvoiceField.CATEGORY, errors);
		validateNotNullField(invoiceForm.getRecipientType(), InvoiceField.RECIPIENT_TYPE, errors);
		validateInvoiceName(invoiceForm, MAX_FIELD_LENGTH, errors);
	}

	protected static void validateInvoiceName(final TaxInvoiceForm invoiceForm, final int maxFieldLength, final Errors errors)
	{

		if (StringUtils.isNotBlank(invoiceForm.getRecipientType())
				&& invoiceForm.getRecipientType().equals(InvoiceRecipientType.UNIT.getCode()))
		{
			final String recipient = invoiceForm.getRecipient();
			if (recipient == null || StringUtils.isEmpty(recipient) || (StringUtils.length(recipient) > maxFieldLength))
			{
				errors.rejectValue(InvoiceField.RECIPIENT.getFieldKey(), InvoiceField.RECIPIENT.getErrorKey());
			}
		}
	}

	protected static void validateNotNullField(final String invoiceField, final InvoiceField fieldType, final Errors errors)
	{
		if (invoiceField == null || StringUtils.isEmpty(invoiceField))
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	private enum InvoiceField
	{

		RECIPIENT("recipient", "invoice.recipient.invalid"), CATEGORY("category", "invoice.category.invalid"), RECIPIENT_TYPE(
				"recipientType", "invoice.recipientType.invalid");

		private final String fieldKey;
		private final String errorKey;

		private InvoiceField(final String fieldKey, final String errorKey)
		{
			this.fieldKey = fieldKey;
			this.errorKey = errorKey;
		}

		public String getFieldKey()
		{
			return fieldKey;
		}

		public String getErrorKey()
		{
			return errorKey;
		}
	}
}
