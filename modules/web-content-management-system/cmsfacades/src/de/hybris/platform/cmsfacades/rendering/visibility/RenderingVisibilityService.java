/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.visibility;

import de.hybris.platform.core.model.ItemModel;


/**
 * Interface responsible for verifying the visibility of an {@link ItemModel}.
 */
public interface RenderingVisibilityService
{
	/**
	 * Verifies the visibility of {@link ItemModel}
	 *
	 * @param itemModel the {@link ItemModel}
	 * @return true if the {@link ItemModel} is visible and false otherwise.
	 */
	boolean isVisible(ItemModel itemModel);
}
