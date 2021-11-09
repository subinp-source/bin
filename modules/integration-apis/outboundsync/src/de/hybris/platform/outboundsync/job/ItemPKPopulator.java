/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.job;

import de.hybris.platform.outboundsync.dto.OutboundItemDTO;

import java.util.List;

/**
 * A populator that finds a corresponding root item for any given item in the integration object and populates the root item primary
 * key in the item.
 */
public interface ItemPKPopulator
{
	/**
	 * Populates the pk with the root item pk for the integrationObject under concern
	 *
	 * @param itemDTO A DTO with the information about the changes in an item.
	 * @return a list of DTOs where root item PK is populated. Each item in the returned list represents a different root item
	 * derived. This is possible only in case of a one-to-many or many-to-many relation of the item presented by the {@code itemDTO}
	 * to the root item of the integration object.
	 */
	List<OutboundItemDTO> populatePK(final OutboundItemDTO itemDTO);
}
