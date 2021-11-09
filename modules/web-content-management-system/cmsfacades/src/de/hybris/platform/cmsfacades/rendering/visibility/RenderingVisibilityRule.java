/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.visibility;

import de.hybris.platform.core.model.ItemModel;

import java.util.function.Predicate;


/**
 * Interface responsible for verification of an item visibility.
 * @param <T> the object that extends {@link ItemModel}
 */
public interface RenderingVisibilityRule<T extends ItemModel>
{
	/**
	 * The predicate to verify that the provided itemModel is the one for which the visibility is being verified.
	 * @return the {@link Predicate}
	 */
	Predicate<ItemModel> restrictedBy();

	/**
	 * Verifies the visibility of itemModel
	 * @param itemModel
	 * @return
	 */
	boolean isVisible(T itemModel);
}
