/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.converter.config;

import de.hybris.platform.commercefacades.converter.ModifiableConfigurablePopulator;


/**
 * Spring Bean used to modify {@link ModifiableConfigurablePopulator} instances. Supports adding or removing a
 * populator.
 *
 * @deprecated Since 6.0. Use {@link de.hybris.platform.converters.config.ConfigurablePopulatorModification} instead.
 *             Will be removed in version 6.2
 */
@Deprecated(since = "6.0", forRemoval = true)
public class ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>
		extends de.hybris.platform.converters.config.ConfigurablePopulatorModification<SOURCE, TARGET, OPTION>
{
	//deprecated
}
