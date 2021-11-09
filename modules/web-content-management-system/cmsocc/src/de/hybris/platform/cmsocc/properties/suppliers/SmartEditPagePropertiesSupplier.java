/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link CMSItemPropertiesSupplier} to handle {@link AbstractPageModel}.
 */
public class SmartEditPagePropertiesSupplier extends AbstractSmarteditItemPropertiesSupplier
{
	private static final String CSS_CODE_PREFIX_UID = "smartedit-page-uid-";
	private static final String CSS_CODE_PREFIX_UUID = "smartedit-page-uuid-";
	private static final String CSS_CODE_PREFIX_CATALOG_VERSION_UUID = "smartedit-catalog-version-uuid-";
	private static final String PAGE_UID_CHARACTER_EXCLUSION_REGEXP = "[^a-zA-Z0-9-_]";

	@Override
	public Map<String, Object> getProperties(final CMSItemModel itemModel)
	{
		final ItemData pageData = getUniqueItemIdentifierService().getItemData(itemModel).orElseThrow(
				() -> new UnknownIdentifierException("Cannot generate uuid for page in SmartEditPagePropertiesSupplier"));

		final ItemData catalogVersionData = getUniqueItemIdentifierService().getItemData(itemModel.getCatalogVersion()).orElseThrow(
				() -> new UnknownIdentifierException("Cannot generate uuid for component in SmartEditPagePropertiesSupplier"));

		final Map<String, Object> properties = new HashMap<>();

		final List<String> classes = new ArrayList<>();
		classes.add(CSS_CODE_PREFIX_UID + itemModel.getUid().replaceAll(PAGE_UID_CHARACTER_EXCLUSION_REGEXP, "-"));
		classes.add(CSS_CODE_PREFIX_UUID + pageData.getItemId());
		classes.add(CSS_CODE_PREFIX_CATALOG_VERSION_UUID + catalogVersionData.getItemId());

		properties.put(CLASSES, classes);

		return properties;
	}
}
