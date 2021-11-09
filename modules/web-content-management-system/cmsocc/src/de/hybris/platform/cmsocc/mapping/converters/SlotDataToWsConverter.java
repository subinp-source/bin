/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping.converters;

import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsocc.data.ComponentListWsDTO;
import de.hybris.platform.cmsocc.data.ComponentWsDTO;
import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;

import java.util.List;
import java.util.stream.Collectors;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

/**
 * The converter to convert {@link PageContentSlotData} data object to {@link ContentSlotWsDTO} ws object.
 */
public class SlotDataToWsConverter extends AbstractDataToWsConverter<PageContentSlotData, ContentSlotWsDTO>
{
	@Override
	public Class getDataClass()
	{
		return PageContentSlotData.class;
	}

	@Override
	public Class getWsClass()
	{
		return ContentSlotWsDTO.class;
	}

	@Override
	public void customize(final MapperFactory factory)
	{
		factory.classMap(PageContentSlotData.class, ContentSlotWsDTO.class).byDefault()
				.customize(new CustomMapper<PageContentSlotData, ContentSlotWsDTO>()
				{
					@Override
					public void mapAtoB(final PageContentSlotData data, final ContentSlotWsDTO wsData,
							final MappingContext mappingContext)
					{
						final List<ComponentWsDTO> componentList = data.getComponents() //
								.stream() //
								.map(component -> (ComponentWsDTO) getMapper().map(component, fields)) //
								.collect(Collectors.toList());

						final ComponentListWsDTO componentListDTO = new ComponentListWsDTO();
						componentListDTO.setComponent(componentList);
						wsData.setComponents(componentListDTO);

						if (isOtherPropertiesFieldVisible(wsData))
						{
							wsData.setOtherProperties(convertMap(data.getOtherProperties()));
						}
					}
				}).register();
	}

	/**
	 * Method to check whether the otherProperties should be populated.
	 * @param wsData the {@link ContentSlotWsDTO} that is used to determine whether the otherProperties field is visible or not.
	 * @return true if otherProperties field should be populated in ws data object.
	 */
	protected boolean isOtherPropertiesFieldVisible(final ContentSlotWsDTO wsData)
	{
		// IMPORTANT: this field will not be null if it's allowed through fields attribute of map method.
		return wsData.getOtherProperties() != null;
	}
}
