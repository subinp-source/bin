/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CLONE_COMPONENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_PAGE_UUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relatedpages.service.RelatedPageRejectionService;
import de.hybris.platform.cmsfacades.pages.service.PageInitializer;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Required;


/**
 * performs the necessary initialization that a newly created {@code AbstractPageModel} may require before saving.
 */
public class PageInitializerPopulator implements Populator<Map<String, Object>, ItemModel>
{
	private PageInitializer pageInitializer;
	private ModelService modelService;
	private RelatedPageRejectionService relatedPageRejectionService;

	@Override
	public void populate(final Map<String, Object> source, final ItemModel itemModel) throws ConversionException
	{
		if (itemModel == null)
		{
			throw new ConversionException("Item Model used in the populator should not be null.");
		}
		if (source == null)
		{
			throw new ConversionException("Source map used in the populator should not be null.");
		}

		final AbstractPageModel page = ((AbstractPageModel) itemModel);

		// when the approval status is manually updated, do not automatically reject it
		// when the page is being cloned, the original page should not be updated
		if (!getRelatedPageRejectionService().hasUserChangedApprovalStatus(page) && !isPageBeingCloned(source))
		{
			getRelatedPageRejectionService().rejectPage(page);
		}

		if (getModelService().isNew(page))
		{
			getModelService().save(page);
			getPageInitializer().initialize(page);
		}
	}

	/**
	 * Verifies if the page is being cloned or not.
	 * 
	 * @param source
	 *           the map containing page cloning context information
	 * @return <tt>TRUE</tt> if the page is being clone, <tt>FALSE</tt> otherwise
	 */
	protected boolean isPageBeingCloned(final Map<String, Object> source)
	{
		final String sourcePageUUID = (String) source.get(FIELD_PAGE_UUID);
		final Object cloneComponents = source.get(FIELD_CLONE_COMPONENTS);
		return isNotBlank(sourcePageUUID) && Objects.nonNull(cloneComponents);
	}

	@Required
	public void setPageInitializer(final PageInitializer pageInitializer)
	{
		this.pageInitializer = pageInitializer;
	}

	protected PageInitializer getPageInitializer()
	{
		return pageInitializer;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected RelatedPageRejectionService getRelatedPageRejectionService()
	{
		return relatedPageRejectionService;
	}

	@Required
	public void setRelatedPageRejectionService(final RelatedPageRejectionService relatedPageRejectionService)
	{
		this.relatedPageRejectionService = relatedPageRejectionService;
	}
}
