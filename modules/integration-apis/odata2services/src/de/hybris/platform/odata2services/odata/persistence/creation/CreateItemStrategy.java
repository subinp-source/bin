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

import org.apache.olingo.odata2.api.edm.EdmException;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;

/**
 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.ContextItemModelService}, which eliminates the need for
 * a {@code CreateItemStrategy} or {@link de.hybris.platform.inboundservices.persistence.ItemModelFactory} for item model creations
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public interface CreateItemStrategy
{
	ItemModel createItem(PersistenceContext ctx) throws EdmException;
}
