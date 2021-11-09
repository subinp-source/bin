/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence.creation;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingNavigationPropertyException;
import de.hybris.platform.odata2services.odata.persistence.populator.ContextReferencedItemModelService;

import org.slf4j.Logger;

/**
 * @deprecated use {@link ContextReferencedItemModelService#deriveReferencedItemModel(TypeAttributeDescriptor, PersistenceContext)}
 * method instead and then throw an exception, if it return {@code Optional.empty()}
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public class NeverCreateItemStrategy implements CreateItemStrategy
{
	private static final Logger LOG = Log.getLogger(NeverCreateItemStrategy.class);

	@Override
	public ItemModel createItem(final PersistenceContext ctx)
	{
		final IntegrationItem item = ctx.getIntegrationItem();
		final String entityName = item.getItemType().getItemCode();
		LOG.error("Item '{}' -> '{}' does not exist, no item will be created.", entityName, item);

		throw new MissingNavigationPropertyException(entityName);
	}
}
