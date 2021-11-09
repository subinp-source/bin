/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueService;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;

abstract class AbstractClassificationAttributeValueConverter
{
    private ClassificationAttributeValueService attributeValueService;

    ClassificationAttributeValueModel getAttributeValue(final ClassificationTypeAttributeDescriptor attribute,
                                                                  final String code)
    {
        return attributeValueService.find(getSystemVersion(attribute), code)
                                    .orElseGet(() -> attributeValueService.create(getSystemVersion(attribute), code));
    }

    boolean isAttributeTypeEnum(final ClassificationTypeAttributeDescriptor attribute)
    {
        return ClassificationAttributeTypeEnum.ENUM.equals(getClassAttributeAssignmentModel(attribute).getAttributeType());
    }

    void addValueToClassificationAttributeAssignmentAttributeValues(final ClassificationTypeAttributeDescriptor attribute,
                                                                            final ClassificationAttributeValueModel attributeValue)
    {
        final var attributeValues = Lists.newArrayList(getClassAttributeAssignmentModel(attribute).getAttributeValues());
        if (!attributeValues.contains(attributeValue))
        {
            attributeValues.add(attributeValue);
            getClassAttributeAssignmentModel(attribute).setAttributeValues(attributeValues);
        }
    }

    private ClassificationSystemVersionModel getSystemVersion(final ClassificationTypeAttributeDescriptor attribute)
    {
        return getClassAttributeAssignmentModel(attribute).getSystemVersion();
    }

    private ClassAttributeAssignmentModel getClassAttributeAssignmentModel(final ClassificationTypeAttributeDescriptor attribute)
    {
        return attribute.getClassAttributeAssignment();
    }

    @Required
    public void setAttributeValueService(final ClassificationAttributeValueService attributeValueService)
    {
        this.attributeValueService = attributeValueService;
    }
}
