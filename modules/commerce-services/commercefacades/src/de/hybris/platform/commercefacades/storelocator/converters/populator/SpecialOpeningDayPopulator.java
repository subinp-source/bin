/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storelocator.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.SpecialOpeningDayData;
import de.hybris.platform.storelocator.model.SpecialOpeningDayModel;

import java.text.DateFormat;


public class SpecialOpeningDayPopulator extends OpeningDayPopulator<SpecialOpeningDayModel, SpecialOpeningDayData>
{

	@Override
	public void populate(final SpecialOpeningDayModel source, final SpecialOpeningDayData target)
	{
		populateBase(source, target);
		target.setClosed(source.isClosed());
		target.setName(source.getName());
		target.setComment(source.getMessage());
		if (source.getDate() != null)
		{
			target.setDate(source.getDate());
			target.setFormattedDate(DateFormat.getDateInstance(getDateFormatStyle(), getCurrentLocale()).format(source.getDate()));
		}
	}

	protected int getDateFormatStyle()
	{
		return DateFormat.SHORT;
	}
}
