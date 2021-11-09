/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.router.impl;

import static java.util.Collections.singletonList;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.outboundsync.dto.OutboundItem;
import de.hybris.platform.outboundsync.dto.OutboundItemDTO;
import de.hybris.platform.outboundsync.job.ItemPKPopulator;
import de.hybris.platform.outboundsync.job.OutboundItemFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RootItemPKPopulator implements ItemPKPopulator
{
	private static final Logger LOG = LoggerFactory.getLogger(RootItemPKPopulator.class);

	private OutboundItemFactory itemFactory;

	@Override
	public List<OutboundItemDTO> populatePK(final OutboundItemDTO itemDto)
	{
		final OutboundItem outboundItem = itemFactory.createItem(itemDto);
		return outboundItem.getChangedItemModel()
				.map(itemModel -> derivePkFromChangedItem(itemDto, outboundItem, itemModel))
				.orElseGet(() -> handleNoChangedItem(itemDto, outboundItem));
	}

	private List<OutboundItemDTO> handleNoChangedItem(final OutboundItemDTO itemDto, final OutboundItem outboundItem)
	{
		LOG.warn("Cannot find item model for PK {} and change DTO {} for outbound sync. Item may have been deleted already",
				outboundItem.getItem().getPK(), itemDto);
		return singletonList(itemDto);
	}

	private List<OutboundItemDTO> derivePkFromChangedItem(final OutboundItemDTO itemDto, final OutboundItem outboundItem, final ItemModel itemModel)
	{
		if (outboundItem.getIntegrationObject().getRootItemType().isEmpty())
		{
			return Collections.singletonList(populateWithOwnPK(itemDto));
		}
		return outboundItem.getTypeDescriptor()
				.map(type -> derivePkForItem(itemDto, itemModel, type))
				.orElseGet(() -> handleItemIsNotInIntegrationObject(itemDto, outboundItem, itemModel));
	}

	private List<OutboundItemDTO> derivePkForItem(final OutboundItemDTO itemDto, final ItemModel itemModel, final TypeDescriptor typeDescriptor)
	{
		final List<ReferencePath> pathsToRoot = typeDescriptor.getPathsToRoot();
		return pathsToRoot.isEmpty()
				? handleNoParentReferences(itemDto, itemModel)
				: populateWithRootPK(itemDto, itemModel, pathsToRoot);
	}

	private List<OutboundItemDTO> handleItemIsNotInIntegrationObject(final OutboundItemDTO itemDto, final OutboundItem outboundItem, final ItemModel itemModel)
	{
		LOG.warn("No IntegrationObjectItem is defined for '{}' on '{}'. Please check your Integration Object is defined correctly.",
				itemModel.getItemtype(), outboundItem.getIntegrationObject().getCode());
		return singletonList(itemDto);
	}

	private OutboundItemDTO populateWithOwnPK(final OutboundItemDTO itemDto)
	{
		LOG.debug("Populating item with own PK in {}", itemDto);
		return OutboundItemDTO.Builder.from(itemDto).withRootItemPK(itemDto.getItem().getPK()).build();
	}

	private List<OutboundItemDTO> populateWithRootPK(final OutboundItemDTO dto, final ItemModel item, final Collection<ReferencePath> paths)
	{
		final Collection<Object> parentItems = paths.stream()
				.map(p -> p.execute(item))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		return parentItems.isEmpty()
				? handleNoParentReferences(dto, item)
				: deriveRootItems(dto, item, parentItems);
	}

	private List<OutboundItemDTO> handleNoParentReferences(final OutboundItemDTO dto, final ItemModel item)
	{
		LOG.debug("No root item reference exists on: {}", item);
		return singletonList(dto);
	}

	private List<OutboundItemDTO> deriveRootItems(final OutboundItemDTO dto, final ItemModel item, final Collection<Object> parentItems)
	{
		final List<OutboundItemDTO> dtos = parentItems.stream()
				.peek(itemModel -> LOG.debug("Populated root item PK in {}", dto))
				.filter(Objects::nonNull)
				.map(ItemModel.class::cast)
				.map(itemModel -> OutboundItemDTO.Builder.from(dto).withRootItemPK(itemModel.getPk().getLong()).build())
				.collect(Collectors.toList());
		return dtos.isEmpty()
				? handleNoRootItem(dto, item)
				: dtos;
	}

	private List<OutboundItemDTO> handleNoRootItem(final OutboundItemDTO dto, final ItemModel item)
	{
		LOG.debug("Root item is null for: {}", item);
		return Collections.singletonList(OutboundItemDTO.Builder.from(dto).withRootItemPK(item.getPk().getLong()).build());
	}

	protected OutboundItemFactory getItemFactory()
	{
		return itemFactory;
	}

	@Required
	public void setItemFactory(final OutboundItemFactory factory)
	{
		itemFactory = factory;
	}
}
