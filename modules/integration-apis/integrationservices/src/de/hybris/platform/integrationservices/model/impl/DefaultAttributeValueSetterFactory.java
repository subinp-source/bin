/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueSetter;
import de.hybris.platform.integrationservices.model.AttributeValueSetterFactory;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueConverter;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAttributeValueSetterFactory implements AttributeValueSetterFactory
{
    private static final AttributeValueSetter DEFAULT_SETTER = new NullAttributeValueSetter();
    private ModelService modelService;
    private List<ClassificationAttributeValueHandler> valueHandlers;
    private List<ClassificationAttributeValueConverter> valueConverters;

    @Override
    public AttributeValueSetter create(final TypeAttributeDescriptor descriptor)
    {
        if (descriptor instanceof ClassificationTypeAttributeDescriptor)
        {
            return new ClassificationAttributeValueSetter(
                    (ClassificationTypeAttributeDescriptor) descriptor, getValueConverters(), getValueHandlers());
        }
        if (descriptor != null)
        {
            return new StandardAttributeValueSetter(descriptor, getModelService());
        }
        return DEFAULT_SETTER;
    }

    protected ModelService getModelService()
    {
        return modelService;
    }

    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }

    public List<ClassificationAttributeValueHandler> getValueHandlers()
    {
        return valueHandlers != null ? valueHandlers : Lists.emptyList();
    }

    public void setValueHandlers(final List<ClassificationAttributeValueHandler> valueHandlers)
    {
        this.valueHandlers = valueHandlers;
    }

    protected List<ClassificationAttributeValueConverter> getValueConverters()
    {
        return valueConverters != null ? valueConverters : Lists.emptyList();
    }

    public void setValueConverters(final List<ClassificationAttributeValueConverter> valueConverters)
    {
        this.valueConverters = valueConverters;
    }
}
