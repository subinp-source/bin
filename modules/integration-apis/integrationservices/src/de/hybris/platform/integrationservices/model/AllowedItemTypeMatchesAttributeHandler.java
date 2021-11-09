/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

/**
 * Provides calculation of the dynamic {@code ItemTypeMatch} attribute on the {@link IntegrationObjectItemModel}
 */
public class AllowedItemTypeMatchesAttributeHandler
		extends AbstractDynamicAttributeHandler<Collection<ItemTypeMatchEnum>, IntegrationObjectItemModel>
{
	private static final ImmutableList<ItemTypeMatchEnum> COMPOSED_TYPE_ENUMS = ImmutableList.of(
			ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES,
			ItemTypeMatchEnum.ALL_SUBTYPES,
			ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE);
	private static final List<ItemTypeMatchEnum> ENUM_AND_ABSTRACT_TYPE_ENUMS = Collections.singletonList(
			ItemTypeMatchEnum.ALL_SUBTYPES);

	/**
	 * Gets a collection of allowed ItemTypeMatchEnums based on the {@link IntegrationObjectItemModel} type.
	 *
	 * @param model a model object to read the value from
	 * @return a collection of the allowed ItemTypeMatchEnums for the given {@link IntegrationObjectItemModel}
	 */
	@Override
	@Nonnull
	public Collection<ItemTypeMatchEnum> get(@Nonnull final IntegrationObjectItemModel model)
	{
		final ComposedTypeModel modelType = model.getType();
		return modelType instanceof EnumerationMetaTypeModel || Boolean.TRUE.equals(modelType.getAbstract())
				? ENUM_AND_ABSTRACT_TYPE_ENUMS
				: COMPOSED_TYPE_ENUMS;
	}
}
