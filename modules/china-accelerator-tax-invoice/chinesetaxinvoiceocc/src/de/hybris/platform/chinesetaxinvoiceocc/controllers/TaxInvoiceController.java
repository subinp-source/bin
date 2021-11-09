/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceocc.controllers;

import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoicefacades.facades.TaxInvoiceCheckoutFacade;
import de.hybris.platform.chinesetaxinvoiceservices.enums.InvoiceRecipientType;
import de.hybris.platform.chinesecommercewebservicescommons.dto.TaxInvoiceWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Controller for tax invoice, provide RESTful API for tax invoice
 */
@Controller
@RequestMapping("/{baseSiteId}")
@Api(tags = "Tax Invoice")
public class TaxInvoiceController
{

	@Resource(name = "chineseTaxInvoiceCheckoutFacade")
	private TaxInvoiceCheckoutFacade taxInvoiceFacade;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "taxInvoiceDTOValidator")
	private Validator taxInvoiceDTOValidator;

	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	protected static final String TAX_INVOICE_OBJECT = "taxInvoice";

	@RequestMapping(value = "/taxinvoice/recipienttypes", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Gets tax invoice recipient types.", notes = "Returns recipient types for tax invoice.")
	@ApiBaseSiteIdParam
	public List<InvoiceRecipientType> getTaxInvoiceRecipientTypes()
	{
		return getTaxInvoiceFacade().getTaxInvoiceRecipientTypes();

	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/users/{userId}/carts/{cartId}/taxinvoice", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Deletes tax invoice from the cart.", notes = "Deletes tax invoice from the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeCartTaxInvoice()
	{
		removeTaxInvoice();
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_GUEST" })
	@RequestMapping(value = "/users/{userId}/carts/{cartId}/taxinvoice", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Updates tax invoice for the cart.", notes = "Updates tax invoice for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void replaceCartTaxInvoice(
			@ApiParam(value = "Request body parameter that contains tax invoice detail.\n\nThe DTO is in XML or .json format.", required = true) @RequestBody final TaxInvoiceWsDTO taxInvoice)
	{
		validate(taxInvoice, TAX_INVOICE_OBJECT, getTaxInvoiceDTOValidator());
		final TaxInvoiceData invoiceData = getDataMapper().map(taxInvoice, TaxInvoiceData.class);
		removeTaxInvoice();
		getTaxInvoiceFacade().setTaxInvoice(invoiceData);
	}


	public TaxInvoiceCheckoutFacade getTaxInvoiceFacade()
	{
		return taxInvoiceFacade;
	}


	public DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public Validator getTaxInvoiceDTOValidator()
	{
		return taxInvoiceDTOValidator;
	}

	protected void removeTaxInvoice()
	{
		if (getTaxInvoiceFacade().hasTaxInvoice())
		{
			final TaxInvoiceData invoice = getTaxInvoiceFacade().getCheckoutCart().getTaxInvoice();
			getTaxInvoiceFacade().removeTaxInvoice(invoice.getId());
		}
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

}

