/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping.converters;

import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.cmsocc.data.NavigationNodeWsDTO;

import ma.glasnost.orika.MapperFactory;

/**
 * The converter to convert {@link NavigationNodeData} data object to {@link NavigationNodeWsDTO} ws object.
 */
public class NavigationNodeDataToWsConverter extends AbstractDataToWsConverter<NavigationNodeData, NavigationNodeWsDTO>
{
	protected static final String LOCALIZED_TITLE = "localizedTitle";
	protected static final String TITLE = "title";

	@Override
	public Class getDataClass()
	{
		return NavigationNodeData.class;
	}

	@Override
	public Class getWsClass()
	{
		return NavigationNodeWsDTO.class;
	}

	@Override
	public void customize(final MapperFactory factory)
	{
		factory.classMap(NavigationNodeData.class, NavigationNodeWsDTO.class)
				.field(LOCALIZED_TITLE, TITLE).byDefault().register();
	}
}
