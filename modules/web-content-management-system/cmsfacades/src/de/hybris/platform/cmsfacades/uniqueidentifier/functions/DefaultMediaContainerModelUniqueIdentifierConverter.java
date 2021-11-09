/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.uniqueidentifier.functions;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.media.service.CMSMediaContainerService;
import de.hybris.platform.cmsfacades.uniqueidentifier.EncodedItemComposedKey;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueIdentifierConverter;
import de.hybris.platform.core.model.media.MediaContainerModel;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for consuming the item Id, name and itemType from reading {@link MediaContainerModel} class
 */
public class DefaultMediaContainerModelUniqueIdentifierConverter implements UniqueIdentifierConverter<MediaContainerModel>
{
	private ObjectFactory<ItemData> itemDataDataFactory;
	private CMSMediaContainerService cmsMediaContainerService;
	private CatalogVersionService catalogVersionService;

	@Override
	public String getItemType()
	{
		return MediaContainerModel._TYPECODE;
	}

	@Override
	public ItemData convert(final MediaContainerModel mediaContainerModel)
	{
		final ItemData itemData = getItemDataDataFactory().getObject();
		itemData.setItemId(getUniqueIdentifier(mediaContainerModel));
		itemData.setItemType(MediaContainerModel._TYPECODE);
		itemData.setName(mediaContainerModel.getQualifier());
		return itemData;
	}

	@Override
	public MediaContainerModel convert(final ItemData itemData)
	{
		final EncodedItemComposedKey itemComposedKey = new EncodedItemComposedKey.Builder(itemData.getItemId()).encoded().build();
		final CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(itemComposedKey.getCatalogId(),
				itemComposedKey.getCatalogVersion());
		return getCmsMediaContainerService().getMediaContainerForQualifier(itemComposedKey.getItemId(), catalogVersion);
	}

	/**
	 * Returns the unique identifier using the encoded compose key class. See more details here
	 * {@link EncodedItemComposedKey}.
	 *
	 * @param mediaContainerModel
	 *           the MediaContainer model we want to extract the unique identifier.
	 * @return the encoded unique identifier.
	 * @see EncodedItemComposedKey
	 */
	protected String getUniqueIdentifier(final MediaContainerModel mediaContainerModel)
	{
		final EncodedItemComposedKey itemComposedKey = new EncodedItemComposedKey();
		itemComposedKey.setCatalogId(mediaContainerModel.getCatalogVersion().getCatalog().getId());
		itemComposedKey.setCatalogVersion(mediaContainerModel.getCatalogVersion().getVersion());
		itemComposedKey.setItemId(mediaContainerModel.getQualifier());

		return itemComposedKey.toEncoded();
	}

	protected ObjectFactory<ItemData> getItemDataDataFactory()
	{
		return itemDataDataFactory;
	}

	@Required
	public void setItemDataDataFactory(final ObjectFactory<ItemData> itemDataDataFactory)
	{
		this.itemDataDataFactory = itemDataDataFactory;
	}

	protected CMSMediaContainerService getCmsMediaContainerService()
	{
		return cmsMediaContainerService;
	}

	@Required
	public void setCmsMediaContainerService(final CMSMediaContainerService cmsMediaContainerService)
	{
		this.cmsMediaContainerService = cmsMediaContainerService;
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

}
