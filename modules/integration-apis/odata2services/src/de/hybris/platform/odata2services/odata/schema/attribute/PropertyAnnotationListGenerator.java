/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.springframework.beans.factory.annotation.Required;

/**
 * Generates the various annotations for a property
 */
public class PropertyAnnotationListGenerator implements SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor>
{
    private List<PropertyAnnotationGenerator> annotationGenerators;

    @Override
    public List<AnnotationAttribute> generate(final TypeAttributeDescriptor descriptor)
    {
        return annotationGenerators.stream()
                .filter(g -> g.isApplicable(descriptor))
                .map(g -> g.generate(descriptor))
                .collect(Collectors.toList());
    }

    @Required
    public void setAnnotationGenerators(final List<PropertyAnnotationGenerator> annotationGenerators)
    {
        this.annotationGenerators = annotationGenerators;
    }
}
