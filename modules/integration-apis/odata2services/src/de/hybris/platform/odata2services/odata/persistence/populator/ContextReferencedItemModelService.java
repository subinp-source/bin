/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;

/**
 * @deprecated use {@link de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService}
 * A service for deriving an {@link ItemModel} referenced in the payload of the {@link PersistenceContext}
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public interface ContextReferencedItemModelService extends de.hybris.platform.inboundservices.persistence.populator.ContextReferencedItemModelService
{
}
