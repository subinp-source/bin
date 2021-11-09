/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate.attributes;

import de.hybris.platform.cms2.model.RestrictionTypeModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.function.Predicate;

/**
 * Predicate that returns true if the provided {@link AttributeDescriptorModel} is a single level nested restriction. Note that it only returns true if the
 * restriction is not nested within another restriction.
 */
public class IsSingleLevelNestedRestrictionAttributePredicate implements Predicate<AttributeDescriptorModel>
{
    private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;

    @Override
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return attributeContainsRestrictions(attributeDescriptorModel) && !isInsideOtherRestriction(attributeDescriptorModel);
    }

    protected boolean attributeContainsRestrictions(AttributeDescriptorModel attributeDescriptorModel)
    {
        Class<?> type = getAttributeDescriptorModelHelperService().getAttributeClass(attributeDescriptorModel);
        return type != null && type.equals(AbstractRestrictionModel.class);
    }

    protected boolean isInsideOtherRestriction(AttributeDescriptorModel attributeDescriptorModel)
    {
        Class<?> enclosingType = attributeDescriptorModel.getEnclosingType().getClass();
        return enclosingType != null && enclosingType.equals(RestrictionTypeModel.class);
    }

    protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
    {
        return attributeDescriptorModelHelperService;
    }

    @Required
    public void setAttributeDescriptorModelHelperService(
            AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
    {
        this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
    }
}
