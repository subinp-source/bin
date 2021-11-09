/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.populators;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated Since 6.0. Use
 *             {@link de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUnitReversePopulator}
 *             instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class B2BUnitReversePopulator implements Populator<B2BUnitData, B2BUnitModel>
{
	private CompanyB2BCommerceService companyB2BCommerceService;
	private B2BCommerceUnitService b2bCommerceUnitService;

	@Override
	public void populate(final B2BUnitData source, final B2BUnitModel target) throws ConversionException
	{
		target.setName(source.getName());
		target.setLocName(source.getName());
		target.setUid(source.getUid());
		target.setActive(Boolean.TRUE);
		target.setApprovalProcessCode(source.getApprovalProcessCode());
		if (source.getUnit() != null)
		{
			final B2BUnitModel parentUnit = this.getCompanyB2BCommerceService().getUnitForUid(source.getUnit().getUid());
			getB2bCommerceUnitService().setParentUnit(target, parentUnit);
		}

	}

	protected <T extends CompanyB2BCommerceService> T getCompanyB2BCommerceService()
	{
		return (T) companyB2BCommerceService;
	}

	@Required
	public void setCompanyB2BCommerceService(final CompanyB2BCommerceService companyB2BCommerceService)
	{
		this.companyB2BCommerceService = companyB2BCommerceService;
	}

	protected B2BCommerceUnitService getB2bCommerceUnitService()
	{
		return b2bCommerceUnitService;
	}

	@Required
	public void setB2bCommerceUnitService(final B2BCommerceUnitService b2bUnitService)
	{
		this.b2bCommerceUnitService = b2bUnitService;
	}

}
