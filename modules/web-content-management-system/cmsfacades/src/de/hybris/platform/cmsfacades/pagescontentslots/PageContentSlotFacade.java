/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslots;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;


/**
 * Facade for managing content slot relations in pages.
 */
public interface PageContentSlotFacade
{
	/**
	 * Get a list of content slots with their slot and their position in the page for a given page.
	 *
	 * @param pageId
	 *           - the page containing the content slots
	 * @return list of page ids with the position for a given slot; never <tt>null</tt>
	 * @throws CMSItemNotFoundException
	 *            when the page cannot be found
	 * @throws ConversionException
	 *            when the converter for the CMS Relation Model type is not found
	 */
	List<PageContentSlotData> getContentSlotsByPage(String pageId) throws CMSItemNotFoundException, ConversionException;

}
