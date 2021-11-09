/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesetaxinvoiceservices.services.impl;

import de.hybris.platform.chinesetaxinvoicefacades.data.TaxInvoiceData;
import de.hybris.platform.chinesetaxinvoiceservices.daos.TaxInvoiceDao;
import de.hybris.platform.chinesetaxinvoiceservices.model.TaxInvoiceModel;
import de.hybris.platform.chinesetaxinvoiceservices.services.TaxInvoiceService;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Implementation for {@link TaxInvoiceService}. Delivers main functionality for chinese tax invoice.
 */
@Component("chineseTaxInvoiceService")
public class ChineseTaxInvoiceService implements TaxInvoiceService
{

	@Resource(name = "taxInvoiceDao")
	private TaxInvoiceDao taxInvoiceDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public TaxInvoiceModel getTaxInvoiceForCode(final String code)
	{

		return taxInvoiceDao.findInvoiceByCode(code);
	}

	@Override
	public TaxInvoiceModel createTaxInvoice(TaxInvoiceData data)
	{

		return StringUtils.isBlank(data.getId()) ? modelService.create(TaxInvoiceModel.class) : getTaxInvoiceForCode(data.getId());
	}

	protected TaxInvoiceDao getTaxInvoiceDao()
	{
		return taxInvoiceDao;
	}

	public void setTaxInvoiceDao(TaxInvoiceDao taxInvoiceDao)
	{
		this.taxInvoiceDao = taxInvoiceDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}


}
