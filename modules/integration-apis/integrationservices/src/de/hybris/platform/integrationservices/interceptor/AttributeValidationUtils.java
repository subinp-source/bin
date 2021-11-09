/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AttributeValidationUtils
{
	private AttributeValidationUtils()
	{
	}

	/**
	 * Checks if a standard attribute name already exists as a virtual or classification attribute
	 *
	 * @param attributeName with the name of the attribute being created
	 * @param item          integration object item that we're creating the attribute in
	 * @return {@code true} if the attribute name is duplicate, {@code false} if not
	 */
	static boolean isStandardAttributeNameDuplicated(final String attributeName, final IntegrationObjectItemModel item)
	{
		return isAttributeNameDuplicated(attributeName, item.getClassificationAttributes(), item.getVirtualAttributes(), null);
	}

	/**
	 * Checks if a classification attribute name already exists as a virtual or standard attribute
	 *
	 * @param attributeName with the name of the attribute being created
	 * @param item          integration object item that we're creating the attribute in
	 * @return {@code true} if the attribute name is duplicate, {@code false} if not
	 */
	static boolean isClassificationAttributeNameDuplicated(final String attributeName, final IntegrationObjectItemModel item)
	{
		return isAttributeNameDuplicated(attributeName, null, item.getVirtualAttributes(), item.getAttributes());
	}

	/**
	 * Checks if a virtual attribute name already exists as a standard or classification attribute
	 *
	 * @param attributeName with the name of the attribute being created
	 * @param item          integration object item that we're creating the attribute in
	 * @return {@code true} if the attribute name is duplicate, {@code false} if not
	 */
	static boolean isVirtualAttributeNameDuplicated(final String attributeName, final IntegrationObjectItemModel item)
	{
		return isAttributeNameDuplicated(attributeName, item.getClassificationAttributes(), null, item.getAttributes());
	}

	private static boolean isAttributeNameDuplicated(
			final String attributeName,
			final Set<IntegrationObjectItemClassificationAttributeModel> classificationAttributes,
			final Set<IntegrationObjectItemVirtualAttributeModel> virtualAttributes,
			final Set<IntegrationObjectItemAttributeModel> standardAttributes)
	{
		final List<? extends AbstractIntegrationObjectItemAttributeModel> combinedAttributes = (List<? extends AbstractIntegrationObjectItemAttributeModel>) Stream
				.of(
						classificationAttributes != null ? classificationAttributes : Collections.emptyList(),
						virtualAttributes != null ? virtualAttributes : Collections.emptyList(),
						standardAttributes != null ? standardAttributes : Collections.emptyList())
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		return combinedAttributes.stream()
		                         .anyMatch(attr -> attr.getAttributeName().equals(attributeName));
	}
}
