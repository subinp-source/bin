/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

/**
 * Provides calculation of the dynamic {@code keyAttributes} attribute on the {@link IntegrationObjectItemModel}
 */
public class KeyAttributesAttributeHandler extends AbstractDynamicAttributeHandler<Collection<IntegrationObjectItemAttributeModel>, IntegrationObjectItemModel>
{
	/**
	 * Reads value of the {@code keyAttributes} attribute
	 *
	 * @param model a model object to read the value from.
	 * @return a collection of key attributes for the given {@link IntegrationObjectItemModel}. This collection does not include
	 * key attributes in the referenced items but only key attributes of the specified item model. If the item does not have attributes
	 * or does not have key attributes, an empty collection is returned.
	 */
	@Override
	@NotNull
	public Collection<IntegrationObjectItemAttributeModel> get(@NotNull final IntegrationObjectItemModel model)
	{
		return model.getAttributes().stream()
				.filter(IntegrationObjectItemAttributeModelUtils::isUnique)
				.collect(Collectors.toSet());
	}
}
