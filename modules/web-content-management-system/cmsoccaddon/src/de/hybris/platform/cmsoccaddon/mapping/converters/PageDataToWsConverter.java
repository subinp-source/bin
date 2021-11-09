/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.mapping.converters;

import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsoccaddon.data.CMSPageWsDTO;
import de.hybris.platform.cmsoccaddon.data.ContentSlotListWsDTO;
import de.hybris.platform.cmsoccaddon.data.ContentSlotWsDTO;

import java.util.List;
import java.util.stream.Collectors;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

/**
 * The converter to convert {@link AbstractPageData} data object to {@link CMSPageWsDTO} ws object.
 */
public class PageDataToWsConverter extends AbstractDataToWsConverter<AbstractPageData, CMSPageWsDTO>
{
	protected static final String LOCALIZED_TITLE = "localizedTitle";
	protected static final String TITLE = "title";

	protected static final String LOCALIZED_DESCRIPTION = "localizedDescription";
	protected static final String DESCRIPTION = "description";

	@Override
	public Class getDataClass()
	{
		return AbstractPageData.class;
	}

	@Override
	public Class getWsClass()
	{
		return CMSPageWsDTO.class;
	}

	@Override
	public void customize(final MapperFactory factory)
	{
		factory.classMap(AbstractPageData.class, CMSPageWsDTO.class)
				.byDefault() //
				.field(LOCALIZED_TITLE, TITLE) //
				.field(LOCALIZED_DESCRIPTION, DESCRIPTION) //
				.customize(new CustomMapper<AbstractPageData, CMSPageWsDTO>()
				{
					@Override
					public void mapAtoB(final AbstractPageData data, final CMSPageWsDTO wsData, final MappingContext mappingContext)
					{
						final List<ContentSlotWsDTO> mappedSlots = data.getContentSlots() //
								.stream() //
								.map(slot -> (ContentSlotWsDTO) getMapper().map(slot, fields)) //
								.collect(Collectors.toList());

						final ContentSlotListWsDTO slotsWsData = new ContentSlotListWsDTO();
						slotsWsData.setContentSlot(mappedSlots);
						wsData.setContentSlots(slotsWsData);

						if (isOtherPropertiesFieldVisible(wsData))
						{
							wsData.setOtherProperties(convertMap(data.getOtherProperties()));
						}
					}
				}).register();
	}

	/**
	 * Method to check whether the otherProperties should be populated.
	 * @param wsData the {@link CMSPageWsDTO} that is used to determine whether the otherProperties field is visible or not.
	 * @return true if otherProperties field should be populated in ws data object, false otherwise
	 */
	protected boolean isOtherPropertiesFieldVisible(final CMSPageWsDTO wsData)
	{
		// IMPORTANT: this field will not be null if it's allowed through fields attribute of map method.
		return wsData.getOtherProperties() != null;
	}
}
