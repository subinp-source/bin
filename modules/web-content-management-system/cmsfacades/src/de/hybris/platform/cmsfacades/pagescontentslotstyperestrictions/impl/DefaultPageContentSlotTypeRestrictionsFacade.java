/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.catalogversion.service.CMSCatalogVersionService;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.ContentSlotTypeRestrictionsData;
import de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.PageContentSlotTypeRestrictionsFacade;
import de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.validator.ContentSlotTypeRestrictionsGetValidator;
import de.hybris.platform.core.model.type.TypeModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PageContentSlotTypeRestrictionsFacade}.
 */
public class DefaultPageContentSlotTypeRestrictionsFacade implements PageContentSlotTypeRestrictionsFacade
{
	private CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	private CMSAdminPageService cmsAdminPageService;
	private CMSAdminSiteService cmsAdminSiteService;
	private CatalogLevelService catalogLevelService;
	private SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler;
	private CMSCatalogVersionService cmsCatalogVersionService;
	private CatalogVersionService catalogVersionService;
	private FacadeValidationService facadeValidationService;
	private ContentSlotTypeRestrictionsGetValidator cmsContentSlotTypeRestrictionsGetValidator;

	@Override
	public ContentSlotTypeRestrictionsData getTypeRestrictionsForContentSlotUID(final String pageUid, final String contentSlotUid)
			throws CMSItemNotFoundException
	{
		final Set<CMSComponentTypeModel> typeRestrictionsForContentSlot = getSessionSearchRestrictionsDisabler().execute(() ->
		{
			try
			{
				final AbstractPageModel page = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(pageUid);
				final ContentSlotModel contentSlot = getCmsAdminContentSlotService()
						.getContentSlotForIdAndCatalogVersions(contentSlotUid, getCatalogVersionService().getSessionCatalogVersions());
				return getCmsAdminTypeRestrictionsService().getTypeRestrictionsForContentSlot(page, contentSlot);
			}
			catch (final CMSItemNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		});

		final ContentSlotTypeRestrictionsData contentSlotTypeRestrictions = new ContentSlotTypeRestrictionsData();
		contentSlotTypeRestrictions.setContentSlotUid(contentSlotUid);

		final List<String> collect = typeRestrictionsForContentSlot.stream().map(TypeModel::getCode).collect(Collectors.toList());
		contentSlotTypeRestrictions.setValidComponentTypes(collect);

		return contentSlotTypeRestrictions;
	}

	@Override
	public List<ContentSlotTypeRestrictionsData> getTypeRestrictionsForContentSlots(final CMSPageContentSlotListData contentSlotListData) throws CMSItemNotFoundException
	{
		getFacadeValidationService().validate(getCmsContentSlotTypeRestrictionsGetValidator(), contentSlotListData);

		try
		{
			return getSessionSearchRestrictionsDisabler().execute(() -> {
				final AbstractPageModel pageModel = getCmsAdminPageService().getPageForIdFromActiveCatalogVersion(contentSlotListData.getPageId());
				final List<String> uniqueSlotIds = contentSlotListData.getSlotIds().stream().distinct().collect(Collectors.toList());

				return getCmsAdminContentSlotService().getContentSlots(uniqueSlotIds).stream()
						.map(slot -> getTypeRestrictionsForSlot(pageModel, slot))
						.collect(Collectors.toList());
			});
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException ex)
		{
			throw new CMSItemNotFoundException("Cannot find type restrictions for the given slotIds.", ex);
		}
	}

	/**
	 * Get the type restrictions for the given slot.
	 *
	 * @param page
	 * 			- The page where to look for the given slot
	 * @param slot
	 * 			- The slot whose type restrictions to retrieve
	 * @return An object containing the list of type restrictions applicable to the given slot.
	 */
	protected ContentSlotTypeRestrictionsData getTypeRestrictionsForSlot(final AbstractPageModel page, final ContentSlotModel slot)
	{
		try {
			Set<CMSComponentTypeModel> restrictions = getCmsAdminTypeRestrictionsService().getTypeRestrictionsForContentSlot(page, slot);

			final ContentSlotTypeRestrictionsData restrictionsData = new ContentSlotTypeRestrictionsData();
			restrictionsData.setContentSlotUid(slot.getUid());
			restrictionsData.setValidComponentTypes(restrictions.stream()
					.map(TypeModel::getCode)
					.distinct()
					.collect(Collectors.toList()));

			return restrictionsData;
		}
		catch (final CMSItemNotFoundException ex)
		{
			throw new UnknownIdentifierException(ex);
		}
	}

	protected CMSAdminTypeRestrictionsService getCmsAdminTypeRestrictionsService()
	{
		return cmsAdminTypeRestrictionsService;
	}

	@Required
	public void setCmsAdminTypeRestrictionsService(final CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService)
	{
		this.cmsAdminTypeRestrictionsService = cmsAdminTypeRestrictionsService;
	}

	protected CMSAdminContentSlotService getCmsAdminContentSlotService()
	{
		return cmsAdminContentSlotService;
	}

	@Required
	public void setCmsAdminContentSlotService(final CMSAdminContentSlotService cmsAdminContentSlotService)
	{
		this.cmsAdminContentSlotService = cmsAdminContentSlotService;
	}

	protected CMSAdminPageService getCmsAdminPageService()
	{
		return cmsAdminPageService;
	}

	@Required
	public void setCmsAdminPageService(final CMSAdminPageService cmsAdminPageService)
	{
		this.cmsAdminPageService = cmsAdminPageService;
	}

	protected CatalogLevelService getCatalogLevelService()
	{
		return catalogLevelService;
	}

	@Required
	public void setCatalogLevelService(final CatalogLevelService catalogLevelService)
	{
		this.catalogLevelService = catalogLevelService;
	}

	protected SessionSearchRestrictionsDisabler getSessionSearchRestrictionsDisabler()
	{
		return sessionSearchRestrictionsDisabler;
	}

	@Required
	public void setSessionSearchRestrictionsDisabler(final SessionSearchRestrictionsDisabler sessionSearchRestrictionsDisabler)
	{
		this.sessionSearchRestrictionsDisabler = sessionSearchRestrictionsDisabler;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected CMSCatalogVersionService getCmsCatalogVersionService()
	{
		return cmsCatalogVersionService;
	}

	@Required
	public void setCmsCatalogVersionService(final CMSCatalogVersionService cmsCatalogVersionService)
	{
		this.cmsCatalogVersionService = cmsCatalogVersionService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	public ContentSlotTypeRestrictionsGetValidator getCmsContentSlotTypeRestrictionsGetValidator()
	{
		return cmsContentSlotTypeRestrictionsGetValidator;
	}

	@Required
	public void setCmsContentSlotTypeRestrictionsGetValidator(final ContentSlotTypeRestrictionsGetValidator cmsContentSlotTypeRestrictionsGetValidator)
	{
		this.cmsContentSlotTypeRestrictionsGetValidator = cmsContentSlotTypeRestrictionsGetValidator;
	}
}
