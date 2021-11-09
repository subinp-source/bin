/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemConverter;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator used to add all the information required to render a CMS Component.
 */
public class CMSComponentModelToDataRenderingPopulator implements Populator<AbstractCMSComponentModel, AbstractCMSComponentData>
{

	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private CMSItemConverter cmsItemConverter;
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------
	@Override
	public void populate(final AbstractCMSComponentModel componentModel, final AbstractCMSComponentData componentData)
	{
		componentData.setUid(componentModel.getUid());
		componentData.setTypeCode(componentModel.getItemtype());
		componentData.setModifiedtime(componentModel.getModifiedtime());
		componentData.setName(componentModel.getName());

		componentData.setCatalogVersion( //
				getUniqueIdentifierAttributeToDataContentConverter().convert(componentModel.getCatalogVersion()));
		componentData.setUuid( //
				getUniqueIdentifierAttributeToDataContentConverter().convert(componentModel)
		);
		componentData.setOtherProperties(getCmsItemConverter().convert(componentModel));
	}

	// --------------------------------------------------------------------------
	// Getters/Setters
	// --------------------------------------------------------------------------
	protected CMSItemConverter getCmsItemConverter()
	{
		return cmsItemConverter;
	}

	@Required
	public void setCmsItemConverter(final CMSItemConverter cmsItemConverter)
	{
		this.cmsItemConverter = cmsItemConverter;
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
}
