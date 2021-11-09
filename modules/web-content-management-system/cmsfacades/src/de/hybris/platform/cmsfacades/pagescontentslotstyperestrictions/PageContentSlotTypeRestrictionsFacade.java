/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.data.ContentSlotTypeRestrictionsData;

import java.util.List;


/**
 * CMS Content slot facade used to fetch content slot details (ie: positions), and reposition components from slot to
 * slot.
 */
public interface PageContentSlotTypeRestrictionsFacade
{
	/**
	 * Fetches type restrictions for the given content slot on a given page. The content slot is searched on the current catalog
	 * version and all the active versions of each of the parent catalogs.
	 *
	 * @param pageUid
	 *           Page UID
	 * @param contentSlotUid
	 *           Content slot UID
	 * @return Type restrictions for the given content slot on the page; never <tt>null</tt>
	 * @throws CMSItemNotFoundException
	 *            Thrown in case not find type restrictions passing pageUID
	 */
	ContentSlotTypeRestrictionsData getTypeRestrictionsForContentSlotUID(String pageUid, String contentSlotUid)
			throws CMSItemNotFoundException;

	/**
	 * Fetches type restrictions for each content slot. The content slots are searched on the current catalog
	 * version and all the active versions of each of the parent catalogs.
	 *
	 * Note: Can retrieve type restrictions for page or template slots.
	 *
	 * @param contentSlotListData
	 * 			- Object containing the information about the slots for which to retrieve their type restrictions
	 * @return Type restrictions for every content slots; never <tt>null</tt>
	 * @throws CMSItemNotFoundException
	 *            Thrown in case not find type restrictions passing pageUID
	 */
	default List<ContentSlotTypeRestrictionsData> getTypeRestrictionsForContentSlots(CMSPageContentSlotListData contentSlotListData)
			throws CMSItemNotFoundException
	{
		throw new UnsupportedOperationException("PageContentSlotTypeRestrictionsFacade.getTypeRestrictionsForContentSlots is not implemented.");
	}

}
