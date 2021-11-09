/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;


import java.util.List;
import java.util.Set;


/**
 * This interface provides support for different types of qualifiers.
 */
public interface SnQualifierProvider
{
	/**
	 * Returns the supported qualifier classes.
	 *
	 * @return the supported qualifier classes
	 */
	Set<Class<?>> getSupportedQualifierClasses();

	/**
	 * Returns all the possible qualifiers for a given context.
	 *
	 * @param context
	 *           - the context
	 *
	 * @return the available qualifiers
	 */
	List<SnQualifier> getAvailableQualifiers(SnContext context);

	/**
	 * Returns the current qualifier for a given context.
	 *
	 * @param context
	 *           - the context
	 *
	 * @return the current qualifier
	 */
	List<SnQualifier> getCurrentQualifiers(SnContext context);
}
