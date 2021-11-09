/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslots.populator;

import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * This populator will populate the {@link PageContentSlotData} from the {@link ContentSlotForPageModel}
 */
public class ContentSlotForPageModelPopulator implements Populator<CMSRelationModel, PageContentSlotData>
{

	@Override
	public void populate(final CMSRelationModel source, final PageContentSlotData target) throws ConversionException
	{
		final ContentSlotForPageModel model = (ContentSlotForPageModel) source;

		target.setPosition(model.getPosition());
		target.setPageId(model.getPage().getUid());
		target.setSlotId(model.getContentSlot().getUid());
	}

}
