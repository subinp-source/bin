/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.converter;



/**
 * Interface for a configurable populator. A populator sets values in a target instance based on values in the source
 * instance. Populators are similar to converters except that unlike converters the target instance must already exist.
 * The collection of options is used to control what data is populated.
 *
 * @param <SOURCE>
 *           the type of the source object
 * @param <TARGET>
 *           the type of the destination object
 * @param <OPTION>
 *           the type of the options list
 *
 * @deprecated Since 6.0. Use {@link de.hybris.platform.converters.ConfigurablePopulator} instead. Will be removed in version 6.2
 */
@Deprecated(since = "6.0", forRemoval= true)
public interface ConfigurablePopulator<SOURCE, TARGET, OPTION> extends
		de.hybris.platform.converters.ConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	//deprecated
}
