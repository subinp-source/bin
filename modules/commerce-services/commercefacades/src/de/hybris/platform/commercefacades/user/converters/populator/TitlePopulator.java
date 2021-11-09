/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user.converters.populator;

import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.TitleModel;
import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.user.TitleModel} as source and {@link de.hybris.platform.commercefacades.user.data.TitleData} as target type.
 */
public class TitlePopulator implements Populator<TitleModel, TitleData>
{
    @Override
    public void populate(final TitleModel source, final TitleData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setCode(source.getCode());
        target.setName(source.getName());
    }
}