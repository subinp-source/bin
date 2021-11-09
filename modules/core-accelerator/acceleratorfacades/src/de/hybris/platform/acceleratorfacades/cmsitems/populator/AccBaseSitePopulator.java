/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.populator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;


/**
 * Populates {@link BaseSiteData} from {@link BaseSiteModel}
 */
public class AccBaseSitePopulator implements Populator<BaseSiteModel, BaseSiteData>
{
	@Override
	public void populate(final BaseSiteModel source, final BaseSiteData target) throws ConversionException
	{
		if (source instanceof CMSSiteModel)
		{
			final CMSSiteModel cmsSource = (CMSSiteModel) source;
			target.setUrlEncodingAttributes(new ArrayList<>(cmsSource.getUrlEncodingAttributes()));
		}
	}
}
