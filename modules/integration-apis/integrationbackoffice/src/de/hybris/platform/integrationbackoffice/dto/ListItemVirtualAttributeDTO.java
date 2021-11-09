/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.dto;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ListItemVirtualAttributeDTO extends AbstractListItemDTO
{
	private final IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor;

	public ListItemVirtualAttributeDTO(final boolean selected, final boolean customUnique, final boolean autocreate,
	                                   final IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor,
	                                   final String alias)
	{
		super(selected, customUnique, autocreate);
		this.retrievalDescriptor = retrievalDescriptor;
		this.alias = createAlias(alias);
		this.description = createDescription();
	}

	@Override
	public void setAlias(final String alias)
	{
		this.alias = createAlias(alias);
	}

	public IntegrationObjectVirtualAttributeDescriptorModel getRetrievalDescriptor()
	{
		if (retrievalDescriptor != null)
		{
			return retrievalDescriptor;
		}
		else
		{
			throw new ListItemDTOMissingDescriptorModelException(alias);
		}
	}

	@Override
	public final String createDescription()
	{
		return getType().getCode();
	}

	@Override
	public AbstractListItemDTO findMatch(final Map<ComposedTypeModel, List<AbstractListItemDTO>> currentAttributesMap, final
	ComposedTypeModel parentComposedType)
	{
		final ListItemVirtualAttributeDTO match;
		final Optional<ListItemVirtualAttributeDTO> optionalListItemVirtualAttributeDTO = currentAttributesMap
				.get(parentComposedType)
				.stream()
				.filter(ListItemVirtualAttributeDTO.class::isInstance)
				.map(ListItemVirtualAttributeDTO.class::cast)
				.filter(listItemDTO -> listItemDTO
						.getAlias()
						.equals(this.alias))
				.findFirst();
		match = optionalListItemVirtualAttributeDTO
				.orElseThrow(() -> new NoSuchElementException("No matching VirtualAttribute was found."));

		return match;
	}

	@Override
	public boolean isComplexType(final ReadService readService)
	{
		// Only hybris primitives are currently supported
		return false;
	}

	@Override
	public String getQualifier()
	{
		return getRetrievalDescriptor().getCode();
	}

	@Override
	public TypeModel getType()
	{
		return getRetrievalDescriptor().getType();
	}

	@Override
	public boolean isStructureType()
	{
		return false;
	}

	private String createAlias(final String alias)
	{
		return "".equals(alias) ? getRetrievalDescriptor().getCode() : alias;
	}

}
