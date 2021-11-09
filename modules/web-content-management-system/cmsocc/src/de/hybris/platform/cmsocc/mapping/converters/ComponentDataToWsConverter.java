/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping.converters;

import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsocc.data.ComponentWsDTO;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;


/**
 * The converter to convert {@link AbstractCMSComponentData} data object to {@link ComponentWsDTO} ws object.
 */
public class ComponentDataToWsConverter extends AbstractDataToWsConverter<AbstractCMSComponentData, ComponentWsDTO>
{
	@Override
	public Class getDataClass()
	{
		return AbstractCMSComponentData.class;
	}

	@Override
	public Class getWsClass()
	{
		return ComponentWsDTO.class;
	}

	@Override
	public void customize(final MapperFactory factory)
	{
		factory.classMap(AbstractCMSComponentData.class, ComponentWsDTO.class).byDefault()
				.customize(new CustomMapper<AbstractCMSComponentData, ComponentWsDTO>()
				{
					@Override
					public void mapAtoB(final AbstractCMSComponentData data, final ComponentWsDTO wsData, final MappingContext mappingContext)
					{
						// this field will not be null if it's allowed through fields
						if (wsData.getOtherProperties() != null)
						{
							wsData.setOtherProperties(convertMap(data.getOtherProperties()));
						}
					}
				}).register();
	}
}
