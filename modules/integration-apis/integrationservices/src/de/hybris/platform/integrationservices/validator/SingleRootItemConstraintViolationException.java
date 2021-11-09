/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.validator;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SingleRootItemConstraintViolationException extends RuntimeException
{
	private static final String MSG = "Only one Integration Object Item is allowed to be marked as root item on an Integration Object. " +
			"'%s' has %s: %s";

	public SingleRootItemConstraintViolationException(final String integrationObject, final IntegrationObjectItemModel... rootItems)
	{
		this(integrationObject, Arrays.asList(rootItems));
	}

	public SingleRootItemConstraintViolationException(final String integrationObject, final List<IntegrationObjectItemModel> rootItems)
	{
		super(formatMessageWithOffendingRootItems(integrationObject, rootItems));
	}

	private static String formatMessageWithOffendingRootItems(final String integrationObject, final List<IntegrationObjectItemModel> items)
	{
		return String.format(MSG, integrationObject, items.size(), getOffendingRootItemNames(items));
	}

	private static List<String> getOffendingRootItemNames(final List<IntegrationObjectItemModel> items)
	{
		return items.stream()
				.map(IntegrationObjectItemModel::getCode)
				.collect(Collectors.toList());
	}
}
