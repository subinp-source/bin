/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.populators;


import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.cronjob.model.TriggerModel;


public class TriggerPopulator implements Populator<TriggerModel, TriggerData>
{
    @Override
    public void populate(final TriggerModel source, final TriggerData target)
    {
        target.setActivationTime(source.getActivationTime());
        target.setCreationTime(source.getCreationtime());
        target.setRelative(source.getRelative());
        target.setWeekInterval(source.getWeekInterval());
        target.setDaysOfWeek(source.getDaysOfWeek());
        target.setDay(source.getDay());
        target.setDisplayTimeTable(source.getTimeTable());
    }
}
