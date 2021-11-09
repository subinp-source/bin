/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.populators;

import de.hybris.platform.converters.Populator;


/**
 * @author stevo.slavic
 *
 */
public interface GenericPopulator<SOURCE, TARGET> extends Populator<SOURCE, TARGET>
{
	boolean supports(Class<?> clazz);
}
