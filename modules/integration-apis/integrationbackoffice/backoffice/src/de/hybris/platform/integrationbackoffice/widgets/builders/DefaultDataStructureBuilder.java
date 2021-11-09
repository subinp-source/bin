/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.builders;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.getListItemStructureType;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.updateDTOs;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Default implementation of DataStructure builder
 * Utilizes the EditorAttributeFilterService and ReadService beans
 */
public class DefaultDataStructureBuilder implements DataStructureBuilder
{
	@WireVariable
	private transient EditorAttributesFilteringService editorAttrFilterService;
	@WireVariable
	private transient ReadService readService;

	public DefaultDataStructureBuilder(final ReadService r, final EditorAttributesFilteringService e)
	{
		this.readService = r;
		this.editorAttrFilterService = e;

	}

	public Map<ComposedTypeModel, List<AbstractListItemDTO>> populateAttributesMap(final ComposedTypeModel typeModel,
	                      final Map<ComposedTypeModel, List<AbstractListItemDTO>> currAttrMap)
	{
		if (currAttrMap.get(typeModel) == null)
		{
			final List<AbstractListItemDTO> dtoList = new ArrayList<>();
			final Set<AttributeDescriptorModel> filteredAttributes = editorAttrFilterService.filterAttributesForAttributesMap(
					typeModel);
			filteredAttributes.forEach(attribute -> {
				final boolean selected = Optional.ofNullable(attribute.getUnique()).orElse(false)
						&& Optional.ofNullable(!attribute.getOptional()).orElse(false);
				final ListItemStructureType structureType = getListItemStructureType(readService, attribute);
				dtoList.add(new ListItemAttributeDTO(selected, false, false, attribute, structureType, "", null));
			});
			currAttrMap.put(typeModel, dtoList);
		}

		return currAttrMap;
	}

	public Map<ComposedTypeModel, List<AbstractListItemDTO>> loadExistingDefinitions(final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions,
	                                                                                 final Map<ComposedTypeModel, List<AbstractListItemDTO>> currAttrMap)
	{
		existingDefinitions.forEach((key, value) -> currAttrMap.forEach((key2, value2) -> {
			if (key2.equals(key))
			{
				currAttrMap.replace(key2, updateDTOs(value2, value));
			}
		}));
		return currAttrMap;
	}

	public Set<SubtypeData> compileSubtypeDataSet(final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions,
	                                  final Set<SubtypeData> subtypeDataSet)
	{
		existingDefinitions.forEach((key, value) -> value.stream().filter(ListItemAttributeDTO.class::isInstance)
		                                                 .map(ListItemAttributeDTO.class::cast).forEach(dto -> {
					if (!dto.getType().equals(dto.getBaseType()))
					{
						final SubtypeData data = new SubtypeData(key, dto.getType(), dto.getBaseType(), dto.getAlias(),
								dto.getAttributeDescriptor().getQualifier());
						subtypeDataSet.add(data);
					}
				}));

		return subtypeDataSet;
	}

	public ComposedTypeModel findSubtypeMatch(final ComposedTypeModel parentType, final String attributeQualifier,
	                                          final ComposedTypeModel attributeType,
	                                          final Set<SubtypeData> subtypeDataSet)
	{
		final ComposedTypeModel attributeSubtype;
		final Optional<SubtypeData> data = subtypeDataSet.stream().filter(p -> p.getParentNodeType().equals(parentType)
				&& p.getBaseType().equals(attributeType) && attributeQualifier.equals(p.getAttributeQualifier())).findFirst();
		if (data.isPresent())
		{
			attributeSubtype = readService.getComposedTypeModelFromTypeModel(data.get().getSubtype());
		}
		else
		{
			attributeSubtype = null;
		}
		return attributeSubtype;
	}
}
