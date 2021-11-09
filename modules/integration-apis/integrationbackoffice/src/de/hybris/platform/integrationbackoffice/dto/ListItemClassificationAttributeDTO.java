/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.dto;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.utility.QualifierNameUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;

public class ListItemClassificationAttributeDTO extends AbstractListItemDTO
{
	private final ClassAttributeAssignmentModel classAttributeAssignmentModel;
	private final String classificationAttributeCode;
	private final String categoryCode;

	public ListItemClassificationAttributeDTO(final boolean selected, final boolean customUnique, final boolean autocreate,
	                                          final ClassAttributeAssignmentModel classAttributeAssignmentModel,
	                                          final String alias)
	{
		super(selected, customUnique, autocreate);
		this.classAttributeAssignmentModel = classAttributeAssignmentModel;
		this.classificationAttributeCode = createClassificationAttributeCode(classAttributeAssignmentModel);
		this.categoryCode = classAttributeAssignmentModel.getClassificationClass().getCode();
		this.alias = createAlias(alias);
		this.description = createDescription();
	}

	@Override
	public void setAlias(final String alias)
	{
		this.alias = createAlias(alias);
	}

	public ClassAttributeAssignmentModel getClassAttributeAssignmentModel()
	{
		return classAttributeAssignmentModel;
	}

	public String getClassificationAttributeCode()
	{
		return classificationAttributeCode;
	}

	public String getCategoryCode()
	{
		return categoryCode;
	}

	@Override
	public AbstractListItemDTO findMatch(final Map<ComposedTypeModel, List<AbstractListItemDTO>> currentAttributesMap,
	                                     final ComposedTypeModel parentComposedType)
	{
		final ListItemClassificationAttributeDTO match;
		final Optional<ListItemClassificationAttributeDTO> optionalListItemClassificationAttributeDTO = currentAttributesMap
				.get(parentComposedType)
				.stream()
				.filter(ListItemClassificationAttributeDTO.class::isInstance)
				.map(ListItemClassificationAttributeDTO.class::cast)
				.filter(listItemDTO -> listItemDTO
						.getCategoryCode()
						.equals(categoryCode)
						&& listItemDTO
						.getClassificationAttributeCode()
						.equals(classificationAttributeCode))
				.findFirst();
		match = optionalListItemClassificationAttributeDTO
				.orElseThrow(() -> new NoSuchElementException(
						String.format("No ClassificationAttribute was found for %s", classificationAttributeCode)));

		return match;
	}

	@Override
	public boolean isComplexType(final ReadService readService)
	{
		return classAttributeAssignmentModel.getReferenceType() != null;
	}

	@Override
	public String getQualifier()
	{
		return getClassificationAttributeCode();
	}

	@Override
	public TypeModel getType()
	{
		return classAttributeAssignmentModel.getReferenceType();
	}

	@Override
	public boolean isStructureType(){
		return false;
	}

	private String createClassificationAttributeCode(final ClassAttributeAssignmentModel classAttributeAssignmentModel)
	{
		final String attributeCode = classAttributeAssignmentModel.getClassificationAttribute().getCode();
		return QualifierNameUtils.removeNonAlphaNumericCharacters(attributeCode);
	}

	private String createAlias(final String alias)
	{
		return "".equals(alias) ? classificationAttributeCode : alias;
	}

	@Override
	public final String createDescription()
	{
		final boolean isLocalized = BooleanUtils.isTrue(classAttributeAssignmentModel.getLocalized());
		String classificationType = "";

		if (isLocalized)
		{
			classificationType = "localized:";
		}
		if (classAttributeAssignmentModel.getReferenceType() == null)
		{
			if (classAttributeAssignmentModel.getAttributeType() == ClassificationAttributeTypeEnum.ENUM)
			{
				classificationType += "ValueList";
			}
			else
			{
				classificationType += classAttributeAssignmentModel.getAttributeType().getCode();
			}
		}
		else
		{
			classificationType += classAttributeAssignmentModel.getReferenceType().getCode();
		}

		final String classificationDescription;
		final Boolean isMultivalued = classAttributeAssignmentModel.getMultiValued();
		if (Boolean.TRUE.equals(isMultivalued))
		{
			classificationDescription = String.format("Collection [%s]", classificationType);
		}
		else
		{
			classificationDescription = classificationType;
		}

		return classificationDescription;
	}

}
