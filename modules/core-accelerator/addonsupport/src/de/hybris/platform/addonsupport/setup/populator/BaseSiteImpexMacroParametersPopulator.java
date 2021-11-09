/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.populator;

import de.hybris.platform.addonsupport.setup.impl.AddOnDataImportEventContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


public class BaseSiteImpexMacroParametersPopulator implements Populator<AddOnDataImportEventContext, ImpexMacroParameterData>
{

	@Override
	public void populate(final AddOnDataImportEventContext source, final ImpexMacroParameterData target)
			throws ConversionException
	{
		target.setSiteUid(source.getBaseSite().getUid());
		target.setStoreUid(source.getBaseSite().getUid());
		target.setSolrIndexedType(source.getBaseSite().getUid() + "ProductType");

		if (source.getBaseSite().getChannel() != null)
		{
			target.setChannel(StringUtils.lowerCase(source.getBaseSite().getChannel().getCode()));
		}
	}
}
