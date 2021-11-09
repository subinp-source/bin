/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation of {@link CMSItemPropertiesSupplier} to handle {@link ContentSlotModel}.
 */
public class SmartEditContentSlotPropertiesSupplier extends AbstractSmarteditItemPropertiesSupplier
{
	private static final String SMART_EDIT_COMPONENT_CLASS = "smartEditComponent";
	private static final String SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE = "componentType";
	private static final String SMARTEDIT_COMPONENT_ID_ATTRIBUTE = "componentId";
	private static final String SMARTEDIT_COMPONENT_UUID_ATTRIBUTE = "componentUuid";
	private static final String SMARTEDIT_CATALOG_VERSION_UUID_ATTRIBUTE = "catalogVersionUuid";

	@Override
	public Map<String, Object> getProperties(final CMSItemModel component)
	{
		final ItemData itemData = getUniqueItemIdentifierService().getItemData(component).orElseThrow(
				() -> new UnknownIdentifierException("Cannot generate uuid for item in SmartEditComponentPropertiesSupplier"));

		final ItemData catalogVersionData = getUniqueItemIdentifierService().getItemData(component.getCatalogVersion())
				.orElseThrow(() -> new UnknownIdentifierException(
						"Cannot generate uuid for item in SmartEditComponentPropertiesSupplier"));


		final Map<String, Object> properties = new HashMap<>();
		properties.put(SMARTEDIT_COMPONENT_ID_ATTRIBUTE, component.getUid());
		properties.put(SMARTEDIT_COMPONENT_UUID_ATTRIBUTE, itemData.getItemId());
		properties.put(SMARTEDIT_COMPONENT_TYPE_ATTRIBUTE, component.getItemtype());
		properties.put(SMARTEDIT_CATALOG_VERSION_UUID_ATTRIBUTE, catalogVersionData.getItemId());
		properties.put(CLASSES, Arrays.asList(SMART_EDIT_COMPONENT_CLASS));
		return properties;
	}
}