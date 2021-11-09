/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chinesetaxinvoiceoccaddon.validator;

import de.hybris.platform.chinesetaxinvoicefacades.facades.TaxInvoiceCheckoutFacade;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceRecipientType;
import de.hybris.platform.chinesecommercewebservicescommons.dto.TaxInvoiceWsDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Implementation of {@link org.springframework.validation.Validator} that validate instances of
 * {@link TaxInvoiceWsDTO}.
 *
 * Validates the fields for {@link TaxInvoiceWsDTO}
 *
 */
public class DefaultTaxInvoiceDTOValidator implements Validator
{
	private final Validator taxInvoiceUnitValidator;
	private final Validator taxInvoiceTypeValidator;
	private final TaxInvoiceCheckoutFacade taxInvoiceFacade;

	public DefaultTaxInvoiceDTOValidator(final Validator taxInvoiceUnitValidator, final Validator taxInvoiceTypeValidator,
			final TaxInvoiceCheckoutFacade taxInvoiceFacade ) {
		this.taxInvoiceUnitValidator = taxInvoiceUnitValidator;
		this.taxInvoiceTypeValidator = taxInvoiceTypeValidator;
		this.taxInvoiceFacade = taxInvoiceFacade;
		
	}
	@Override
	public boolean supports(final Class clazz)
	{
		return TaxInvoiceWsDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final TaxInvoiceWsDTO taxInvoice = (TaxInvoiceWsDTO) target;
		Assert.notNull(errors, "Errors object must not be null");

		if (StringUtils.isNotBlank(taxInvoice.getRecipientType()))
		{
			getTaxInvoiceTypeValidator().validate(new String[]
			{ taxInvoice.getRecipientType() }, errors);

			if (taxInvoice.getRecipientType().equals(InvoiceRecipientType.UNIT.toString()))
			{
				getTaxInvoiceUnitValidator().validate(taxInvoice, errors);
			}
			if (taxInvoice.getRecipientType().equalsIgnoreCase(InvoiceRecipientType.INDIVIDUAL.toString())
					&& StringUtils.isNotBlank(taxInvoice.getTaxpayerID()))
			{
				errors.rejectValue("taxpayerID", "TaxpayerID is applicable to unit users only.");
			}

		}
	}
	

	public Validator getTaxInvoiceUnitValidator()
	{
		return taxInvoiceUnitValidator;
	}

	public Validator getTaxInvoiceTypeValidator()
	{
		return taxInvoiceTypeValidator;
	}

	public TaxInvoiceCheckoutFacade getTaxInvoiceCheckoutFacade()
	{
		return taxInvoiceFacade;
	}
}
