/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslots.service.impl;

import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsfacades.pagescontentslots.service.PageContentSlotConverterType;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;


/**
 * Default implementation of <code>PageContentSlotConverterType</code>.
 */
public class DefaultPageContentSlotConverterType implements PageContentSlotConverterType
{
	private Class<? extends CMSRelationModel> classType;
	private AbstractPopulatingConverter<CMSRelationModel, PageContentSlotData> converter;

	@Override
	public Class<? extends CMSRelationModel> getClassType()
	{
		return classType;
	}

	@Override
	public void setClassType(final Class<? extends CMSRelationModel> classType)
	{
		this.classType = classType;
	}

	@Override
	public AbstractPopulatingConverter<CMSRelationModel, PageContentSlotData> getConverter()
	{
		return converter;
	}

	@Override
	public void setConverter(final AbstractPopulatingConverter<CMSRelationModel, PageContentSlotData> converter)
	{
		this.converter = converter;
	}

}
