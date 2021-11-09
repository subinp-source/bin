/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;

/**
 * Default implementation of {@link CMSItemPropertiesSupplier} to handle {@link AbstractCMSComponentModel}.
 */
public class SmartEditComponentPropertiesSupplier extends SmartEditContentSlotPropertiesSupplier
{
	@Override
	public boolean isEnabled(final CMSItemModel itemModel)
	{
		return !isContainer(itemModel) && super.isEnabled(itemModel);
	}

	/**
	 * Verifies that the {@link CMSItemModel} is a container.
	 * @param itemModel the {@link CMSItemModel} to verify
	 * @return true if the {@link CMSItemModel} is a container, false otherwise.
	 */
	protected boolean isContainer(final CMSItemModel itemModel)
	{
		return AbstractCMSComponentContainerModel.class.isAssignableFrom(itemModel.getClass());
	}
}
