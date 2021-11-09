/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MASTER_TEMPLATE_ID;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;


/**
 * Populator that populates the masterTemplateId field for a given page.
 */
public class CMSItemPageModelToDataAttributePopulator implements Populator<ItemModel, Map<String, Object>>
{

	@Override
	public void populate(final ItemModel itemModel, final Map<String, Object> itemMap) throws ConversionException
	{
		AbstractPageModel page = (AbstractPageModel) itemModel;
		itemMap.put(FIELD_MASTER_TEMPLATE_ID, page.getMasterTemplate().getUid());
	}
}
