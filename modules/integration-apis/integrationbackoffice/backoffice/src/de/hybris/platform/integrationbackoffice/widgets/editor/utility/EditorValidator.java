/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EditorValidator
{
	private EditorValidator()
	{
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Validates that each IntegrationObjectItem contains at least one defined attribute.
	 *
	 * @param definitionMap The map to be evaluated.
	 * @return The name of the IntegrationObjectItem that is missing a definition.
	 */
	public static String validateDefinitions(final Map<ComposedTypeModel, List<AbstractListItemDTO>> definitionMap)
	{
		for (final Map.Entry<ComposedTypeModel, List<AbstractListItemDTO>> entry : definitionMap.entrySet())
		{
			if (entry.getValue().isEmpty())
			{
				return entry.getKey().getCode();
			}
		}
		return "";
	}

	/**
	 * Validates that each IntegrationObjectItem contains at least one attribute marked as unique.
	 *
	 * @param definitionMap The map to be evaluated
	 * @return The name of the IntegrationObjectItem that is missing a unique attribute.
	 */
	public static String validateHasKey(final Map<ComposedTypeModel, List<AbstractListItemDTO>> definitionMap)
	{
		String validationError = "";
		for (final Map.Entry<ComposedTypeModel, List<AbstractListItemDTO>> entry : definitionMap.entrySet())
		{
			validationError = entry.getKey().getCode();
			for (final AbstractListItemDTO dto : entry.getValue())
			{
				if (dto instanceof ListItemAttributeDTO && (dto.isCustomUnique() || ((ListItemAttributeDTO) dto).getAttributeDescriptor()
				                                                                                                .getUnique()))
				{
					validationError = "";
					break;
				}
			}

			if (!("").equals(validationError))
			{
				return validationError;
			}
		}
		return validationError;
	}

	/**
	 * Validates whether there are two or more attributes under the same composed with the same name. Duplications are not allowed
	 * and must be either renamed or excluded from the definition before persistence can occur.
	 *
	 * @param duplicationMap Map containing duplicate attributes, will be empty if no duplications are present
	 * @return Name(s) of Composed types that contain duplicate attribute names
	 */
	public static String validateHasNoDuplicateAttributeNames(
			final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> duplicationMap)
	{
		if (!duplicationMap.isEmpty())
		{
			return duplicationMap.keySet().stream().map(ComposedTypeModel::getCode).collect(Collectors.joining(", "));
		}
		else
		{
			return "";
		}
	}

}
