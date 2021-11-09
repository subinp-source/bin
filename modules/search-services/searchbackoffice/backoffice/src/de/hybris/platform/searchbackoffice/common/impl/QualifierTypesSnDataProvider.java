/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.common.impl;

import de.hybris.platform.searchbackoffice.common.SnDataProvider;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnDataProvider} that provides beans of specific types.
 */
public class QualifierTypesSnDataProvider implements SnDataProvider<String, String>
{
	private SnQualifierTypeFactory snQualifierTypeFactory;

	@Override
	public List<String> getData(final Map<String, Object> parameters)
	{
		return List.copyOf(snQualifierTypeFactory.getAllQualifierTypeIds());
	}

	@Override
	public String getValue(final String data)
	{
		return data;
	}

	@Override
	public String getLabel(final String data)
	{
		return data;
	}

	public SnQualifierTypeFactory getSnQualifierTypeFactory()
	{
		return snQualifierTypeFactory;
	}

	@Required
	public void setSnQualifierTypeFactory(final SnQualifierTypeFactory snQualifierTypeFactory)
	{
		this.snQualifierTypeFactory = snQualifierTypeFactory;
	}
}
