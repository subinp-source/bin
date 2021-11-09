/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.converter;

import de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModification;


/**
 * Interface for modifiable configurable populators.
 *
 * @deprecated Since 6.0. Use {@link de.hybris.platform.converters.ModifiableConfigurablePopulator} instead. Will be
 *             removed in version 6.2
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION> extends ConfigurablePopulator<SOURCE, TARGET, OPTION>,
		de.hybris.platform.converters.ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	/**
	 * @deprecated Since 6.0.
	 * @param modification
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	void addModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);

	/**
	 * @deprecated Since 6.0.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	void applyModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);
}
