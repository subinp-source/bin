/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.searchservices.core.service.SnQualifierProvider;
import de.hybris.platform.searchservices.core.service.SnQualifierType;


/**
 * Default implementation of {@link SnQualifierType}.
 */
public class DefaultSnQualifierType implements SnQualifierType
{
	private String id;
	private SnQualifierProvider qualifierProvider;

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *           - the id
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * Returns the qualifier provider.
	 *
	 * @return the qualifier provider
	 */
	@Override
	public SnQualifierProvider getQualifierProvider()
	{
		return qualifierProvider;
	}

	/**
	 * Sets the qualifier provider.
	 *
	 * @param qualifierProvider
	 *           - the qualifier provider
	 */
	public void setQualifierProvider(final SnQualifierProvider qualifierProvider)
	{
		this.qualifierProvider = qualifierProvider;
	}
}
