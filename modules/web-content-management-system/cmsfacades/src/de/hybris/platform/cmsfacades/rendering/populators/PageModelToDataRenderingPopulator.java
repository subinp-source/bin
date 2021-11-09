/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator used to add all the information required to render a CMS Page.
 */
public class PageModelToDataRenderingPopulator implements Populator<AbstractPageModel, AbstractPageData>
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private CMSPageService cmsPageService;
	private Converter<ContentSlotData, PageContentSlotData> contentSlotModelToDataRenderingConverter;
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;
	private CMSPreviewService cmsPreviewService;
	private List<Populator> otherPropertiesPopulators;
	private RenderingCacheService<PageContentSlotData> renderingCacheService;

	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------
	@Override
	public void populate(final AbstractPageModel pageModel, final AbstractPageData targetData)
	{
		targetData.setUid(pageModel.getUid());
		targetData.setUuid(getUniqueIdentifierAttributeToDataContentConverter().convert(pageModel));
		targetData.setName(pageModel.getName());
		targetData.setLocalizedDescription(pageModel.getDescription());
		targetData.setTypeCode(pageModel.getItemtype());
		targetData.setLocalizedTitle(pageModel.getTitle());
		targetData.setDefaultPage(pageModel.getDefaultPage());
		targetData.setTemplate(pageModel.getMasterTemplate().getUid());
		if (Objects.nonNull(pageModel.getRobotTag()))
		{
			targetData.setRobotTag(pageModel.getRobotTag().getCode());
		}
		targetData
				.setCatalogVersionUuid(getUniqueIdentifierAttributeToDataContentConverter().convert(pageModel.getCatalogVersion()));
		populateOtherProperties(pageModel, targetData);

		// Slots
		targetData.setContentSlots( //
				getCmsPageService().getContentSlotsForPage(pageModel, getCmsPreviewService().getPagePreviewCriteria()).stream() //
						.filter(this::isSlotActive) //
						.map(slot -> getRenderingCacheService()
								.cacheOrElse(slot.getContentSlot(), () -> getContentSlotModelToDataRenderingConverter().convert(slot))) //
						.collect(Collectors.toList()) //
		);
	}

	// --------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------

	/**
	 * Populates other properties for {@link AbstractPageModel}.
	 *
	 * @param pageModel
	 * 		the {@link AbstractPageModel} to retrieve other properties
	 * @param targetData
	 * 		the target {@link AbstractPageData} object.
	 */
	protected void populateOtherProperties(final AbstractPageModel pageModel, final AbstractPageData targetData)
	{
		final Map<String, Object> otherProperties = new HashMap<>();

		// Execute all custom populators
		getOtherPropertiesPopulators().forEach(populator -> populator.populate(pageModel, otherProperties));

		if (Objects.isNull(targetData.getOtherProperties()))
		{
			targetData.setOtherProperties(otherProperties);
		}
		else
		{
			targetData.getOtherProperties().putAll(otherProperties);
		}
	}

	/**
	 * This method is used to determine whether a content slot is active or not.
	 *
	 * @param contentSlot
	 * 		the content slot whose status to check
	 * @return true if the slot is active, false otherwise.
	 */
	protected boolean isSlotActive(final ContentSlotData contentSlot)
	{
		final Date activeFrom = contentSlot.getContentSlot().getActiveFrom();
		final Date activeUntil = contentSlot.getContentSlot().getActiveUntil();
		if (activeFrom != null && activeUntil != null)
		{
			final Date date = new Date();
			if (activeFrom.after(date) || activeUntil.before(date))
			{
				return false;
			}
		}
		return contentSlot.getContentSlot().getActive();
	}

	// --------------------------------------------------------------------------
	// Getters/Setters
	// --------------------------------------------------------------------------
	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(final CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	protected Converter<ContentSlotData, PageContentSlotData> getContentSlotModelToDataRenderingConverter()
	{
		return contentSlotModelToDataRenderingConverter;
	}

	@Required
	public void setContentSlotModelToDataRenderingConverter(
			final Converter<ContentSlotData, PageContentSlotData> contentSlotModelToDataRenderingConverter)
	{
		this.contentSlotModelToDataRenderingConverter = contentSlotModelToDataRenderingConverter;
	}

	protected UniqueIdentifierAttributeToDataContentConverter<ItemModel> getUniqueIdentifierAttributeToDataContentConverter()
	{
		return uniqueIdentifierAttributeToDataContentConverter;
	}

	@Required
	public void setUniqueIdentifierAttributeToDataContentConverter(
			final UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter)
	{
		this.uniqueIdentifierAttributeToDataContentConverter = uniqueIdentifierAttributeToDataContentConverter;
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(final CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}

	protected List<Populator> getOtherPropertiesPopulators()
	{
		return otherPropertiesPopulators;
	}

	@Required
	public void setOtherPropertiesPopulators(final List<Populator> otherPropertiesPopulators)
	{
		this.otherPropertiesPopulators = otherPropertiesPopulators;
	}

	public RenderingCacheService<PageContentSlotData> getRenderingCacheService()
	{
		return renderingCacheService;
	}

	@Required
	public void setRenderingCacheService(
			final RenderingCacheService<PageContentSlotData> renderingCacheService)
	{
		this.renderingCacheService = renderingCacheService;
	}
}
