/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.components;

import de.hybris.platform.cmsocc.data.ComponentWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * This adapter is used to convert {@link de.hybris.platform.cmsocc.data.ComponentWsDTO} into
 * {@link ComponentAdapterUtil.ComponentAdaptedData}
 */
public class ComponentWsDTOAdapter extends XmlAdapter<ComponentAdaptedData, ComponentWsDTO>
{
	@Override
	public ComponentAdaptedData marshal(final ComponentWsDTO componentDTO)
	{
		if (componentDTO == null)
		{
			return null;
		}

		return ComponentAdapterUtil.convert(componentDTO);
	}

	@Override
	public ComponentWsDTO unmarshal(final ComponentAdaptedData v) throws Exception
	{
		throw new UnsupportedOperationException();
	}
}
