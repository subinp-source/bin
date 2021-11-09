/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storelocator.converters.populator;

import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import de.hybris.platform.storelocator.model.WeekdayOpeningDayModel;


public class WeekdayOpeningDayPopulator extends OpeningDayPopulator<WeekdayOpeningDayModel, WeekdayOpeningDayData>
{

	@Override
	public void populate(final WeekdayOpeningDayModel source, final WeekdayOpeningDayData target)
	{
		populateBase(source, target);
		target.setWeekDay(getWeekDaySymbols().get(source.getDayOfWeek().ordinal()));
	}
}
