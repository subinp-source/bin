/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.populator;

import de.hybris.platform.addonsupport.setup.impl.AddOnDataImportEventContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


public class AddOnExtensionNameImpexMacroParametersPopulator implements
		Populator<AddOnDataImportEventContext, ImpexMacroParameterData>
{


	@Override
	public void populate(final AddOnDataImportEventContext source, final ImpexMacroParameterData target)
			throws ConversionException
	{
		if (source.getAddonExtensionMetadata().isSuffixChannel())
		{
			target.setAddonExtensionName(source.getAddonExtensionMetadata().getBaseExtensionName()
					+ StringUtils.lowerCase(source.getBaseSite().getChannel().getCode()));
		}
		else
		{
			target.setAddonExtensionName(source.getAddonExtensionMetadata().getBaseExtensionName());
		}

	}

}
