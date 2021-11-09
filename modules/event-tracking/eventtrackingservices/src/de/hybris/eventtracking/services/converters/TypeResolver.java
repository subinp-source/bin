/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.services.converters;

/**
 * @author stevo.slavic
 *
 */
public interface TypeResolver<SOURCE, TARGET>
{
	Class<? extends TARGET> resolveType(SOURCE source);
}
