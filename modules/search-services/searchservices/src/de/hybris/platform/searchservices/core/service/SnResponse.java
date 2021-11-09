/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service;

import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


/**
 * Represents a response.
 */
public interface SnResponse
{
	SnIndexConfiguration getIndexConfiguration();

	SnIndexType getIndexType();
}
