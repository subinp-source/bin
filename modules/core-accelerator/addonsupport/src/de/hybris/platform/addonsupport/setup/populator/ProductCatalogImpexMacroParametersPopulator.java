/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.populator;

import de.hybris.platform.addonsupport.setup.impl.AddOnDataImportEventContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


public class ProductCatalogImpexMacroParametersPopulator implements
		Populator<AddOnDataImportEventContext, ImpexMacroParameterData>
{

	@Override
	public void populate(final AddOnDataImportEventContext source, final ImpexMacroParameterData target)
			throws ConversionException
	{
		target.setProductCatalog(source.getProductCatalog().getId());

	}
}
