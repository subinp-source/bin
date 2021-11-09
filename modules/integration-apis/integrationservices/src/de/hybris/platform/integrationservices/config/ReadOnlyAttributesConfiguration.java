/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.config;

import java.util.Set;

public interface ReadOnlyAttributesConfiguration
{
	/**
	 * Provides the read-only attributes
	 *
	 * @return set of strings of read-only attribute qualifiers
	 */
	Set<String> getReadOnlyAttributes();
}
