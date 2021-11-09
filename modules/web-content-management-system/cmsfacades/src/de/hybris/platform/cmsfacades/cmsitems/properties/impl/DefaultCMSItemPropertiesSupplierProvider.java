/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.properties.impl;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplierProvider;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link CMSItemPropertiesSupplierProvider}.
 */
public class DefaultCMSItemPropertiesSupplierProvider implements CMSItemPropertiesSupplierProvider
{
	private List<CMSItemPropertiesSupplier> cmsItemPropertiesSuppliers;

	@Override
	public List<CMSItemPropertiesSupplier> getSuppliers(final CMSItemModel itemModel)
	{
		return getCmsItemPropertiesSuppliers().stream() //
			.filter(service -> service.getConstrainedBy().test(itemModel)) //
		   .collect(Collectors.toList());
	}

	protected List<CMSItemPropertiesSupplier> getCmsItemPropertiesSuppliers()
	{
		return cmsItemPropertiesSuppliers;
	}

	@Required
	public void setCmsItemPropertiesSuppliers(
			final List<CMSItemPropertiesSupplier> cmsItemPropertiesSuppliers)
	{
		this.cmsItemPropertiesSuppliers = cmsItemPropertiesSuppliers;
	}
}