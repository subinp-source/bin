/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.validator.SingleRootItemConstraintViolationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

public final class IntegrationObjectRootUtils
{
	private static final Logger LOG = Log.getLogger(IntegrationObjectRootUtils.class);

	private IntegrationObjectRootUtils()
	{
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Checks if an IntegrationObject has a root IntegrationObjectItem and returns it. Otherwise returns null
	 *
	 * @param integrationObject an IntegrationObjectModel
	 * @return the root IntegrationObjectItemModel
	 */
	public static IntegrationObjectItemModel getRootIntegrationObjectItem(final IntegrationObjectModel integrationObject)
	{
		IntegrationObjectItemModel root;
		try
		{
			root = integrationObject.getRootItem();
			if (root != null)
			{
				LOG.info("Detected root of type: {}", root.getCode());
			}
			else
			{
				LOG.info("No root initially detected.");
			}
		}
		catch (final SingleRootItemConstraintViolationException e)
		{
			root = null;
			LOG.error("", e);
		}
		return root;
	}

	/**
	 * Old algorithm to determine the root ComposedType from a IntegrationObject's IntegrationObjectItems. The root is
	 * the IntegrationObjectItem which has no parent IntegrationObjectItem. Algorithm: every IntegrationObjectItem is
	 * also an IntegrationObjectItemAttribute with ReturnIntegrationObjectItem != null, except for the root.
	 *
	 * @param integrationObject an IntegrationObjectModel
	 * @return the root IntegrationObjectItemModel
	 */
	public static IntegrationObjectItemModel getRootFromReturnIntegrationObjectItem(
			final IntegrationObjectModel integrationObject)
	{
		final Set<IntegrationObjectItemModel> allItems = new HashSet<>(integrationObject.getItems());
		final Set<IntegrationObjectItemModel> allItemsExcludingRoots = new HashSet<>();

		for (final IntegrationObjectItemModel item : allItems)
		{
			for (final IntegrationObjectItemAttributeModel attribute : item.getAttributes())
			{
				if (attribute.getReturnIntegrationObjectItem() != null)
				{
					allItemsExcludingRoots.add(attribute.getReturnIntegrationObjectItem());
				}
			}
		}

		allItems.removeAll(allItemsExcludingRoots);

		if (allItems.size() != 1)
		{
			LOG.error("Error in finding root composed type.");
			return null;
		}
		else
		{
			return new ArrayList<>(allItems).get(0);
		}
	}

	/**
	 * Corrects an IntegrationObject's root property which is located at the IntegrationObjectItemModel level. This method
	 * will set all other item's root boolean to false, except for the actual root.
	 *
	 * @param integrationObject IntegrationObjectModel to fix
	 * @param rootCode          Code of the root item
	 * @return The same IntegrationObjectModel with root type property corrected to contain a single valid root
	 */
	public static IntegrationObjectModel correctRoot(final IntegrationObjectModel integrationObject, final String rootCode)
	{
		integrationObject.getItems().forEach(item -> item.setRoot(item.getCode().equals(rootCode)));
		return integrationObject;
	}

	/**
	 * Uses both the boolean attribute root detector and legacy algorithm in the case of boolean method failure to
	 * determine the root of the IntegrationObject.
	 *
	 * @param integrationObject an IntegrationObjectModel
	 * @return the IntegrationObjectModel with its root fixed if necessary
	 */
	public static IntegrationObjectModel resolveIntegrationObjectRoot(final IntegrationObjectModel integrationObject)
	{
		IntegrationObjectItemModel root = getRootIntegrationObjectItem(integrationObject);
		if (root == null)
		{
			root = getRootFromReturnIntegrationObjectItem(integrationObject);
			if (root != null)
			{
				LOG.info("Correcting root type to: {} (this correction is only local until changes are persisted)",
						root.getCode());
				return correctRoot(integrationObject, root.getCode());
			}
			else
			{
				LOG.warn("Root could not be determined.");
			}
		}
		return integrationObject;
	}

}
