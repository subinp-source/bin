/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.converter.impl;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.converters.impl.DefaultModifableConfigurablePopulator;


/**
 * Default implementation of the {@link ConfigurablePopulator} extending {@link AbstractModifiableConfigurablePopulator}
 *
 * @deprecated Since 6.0. Use {@link DefaultModifableConfigurablePopulator} instead. Will be removed in version 6.2.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultConfigurablePopulator<SOURCE, TARGET, OPTION>
		extends AbstractModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	//deprecated
}
