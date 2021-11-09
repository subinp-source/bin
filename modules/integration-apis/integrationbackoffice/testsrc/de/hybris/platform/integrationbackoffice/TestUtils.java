/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;

public class TestUtils
{
	public static ListItemClassificationAttributeDTO createClassificationAttributeDTO(final String attributeName, final String qualifier,
	                                                                                  final ClassificationAttributeTypeEnum type,
	                                                                                  final String category)
	{
		final ClassificationAttributeModel classificationAttributeModel = new ClassificationAttributeModel();
		classificationAttributeModel.setCode(qualifier);

		final ClassificationClassModel classificationClassModel = new ClassificationClassModel();
		classificationClassModel.setCode(category);

		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		classAttributeAssignmentModel.setClassificationAttribute(classificationAttributeModel);
		classAttributeAssignmentModel.setAttributeType(type);
		classAttributeAssignmentModel.setClassificationClass(classificationClassModel);
		classAttributeAssignmentModel.setMultiValued(false);

		final IntegrationObjectItemClassificationAttributeModel integrationObjectItemClassificationAttribute = new IntegrationObjectItemClassificationAttributeModel();
		integrationObjectItemClassificationAttribute.setAttributeName(attributeName);
		integrationObjectItemClassificationAttribute.setClassAttributeAssignment(classAttributeAssignmentModel);

		return new ListItemClassificationAttributeDTO(true, false, false, classAttributeAssignmentModel, attributeName);
	}

	public static ListItemAttributeDTO createListItemAttributeDTO(final String qualifier,
	                                                              final boolean customUnique,
	                                                              final boolean unique,
	                                                              final boolean optional,
	                                                              final boolean autocreate,
	                                                              final ListItemStructureType structureType,
	                                                              final TypeModel typeModel)
	{

		final AttributeDescriptorModel attributeDescriptorModel = new AttributeDescriptorModel();
		attributeDescriptorModel.setAttributeType(typeModel);
		attributeDescriptorModel.setQualifier(qualifier);
		attributeDescriptorModel.setOptional(optional);
		attributeDescriptorModel.setUnique(unique);

		return new ListItemAttributeDTO(true, customUnique, autocreate, attributeDescriptorModel,
				structureType, "", typeModel);
	}
}
