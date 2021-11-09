/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.version.populator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.DATE_TIME_FORMAT;

import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;

import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * Populates a Map with custom attributes from the {@link CMSVersionModel} source data model.
 */
public class CMSVersionItemCustomAttributesPopulator implements Populator<CMSVersionModel, Map<String, Object>>
{
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
	private static final String TYPE_CODE = "typeCode";

	@Override
	public void populate(final CMSVersionModel source, final Map<String, Object> itemMap)
	{
		itemMap.put(ItemModel.CREATIONTIME, simpleDateFormat.format(source.getCreationtime()));
		itemMap.put(ItemModel.MODIFIEDTIME, simpleDateFormat.format(source.getModifiedtime()));
		itemMap.put(TYPE_CODE, source.getItemTypeCode());
	}

}
