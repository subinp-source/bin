/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslots.service;

import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;


/**
 * Represents meta-information about a <code>CMSRelationModel</code> class and the converter required to convert the
 * information to a <code>PageContentSlotData</code>
 */
public interface PageContentSlotConverterType
{
	Class<? extends CMSRelationModel> getClassType();

	void setClassType(Class<? extends CMSRelationModel> classType);

	AbstractPopulatingConverter<CMSRelationModel, PageContentSlotData> getConverter();

	void setConverter(AbstractPopulatingConverter<CMSRelationModel, PageContentSlotData> converter);
}
