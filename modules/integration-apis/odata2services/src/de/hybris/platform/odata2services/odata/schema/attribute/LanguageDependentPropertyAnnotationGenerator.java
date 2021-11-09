/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * Generate an annotation for EDMX property when localized is true
 * example:
 * <code><Property s:IsLanguageDependent="true" .../></code>
 */
public class LanguageDependentPropertyAnnotationGenerator implements PropertyAnnotationGenerator
{
    private static final String IS_LANGUAGE_DEPENDENT = "s:IsLanguageDependent";
    private static final AnnotationAttribute ANNOTATION_ATTRIBUTE = new ImmutableAnnotationAttribute()
                                                                            .setName(IS_LANGUAGE_DEPENDENT)
                                                                            .setText("true");

    @Override
    public boolean isApplicable(final TypeAttributeDescriptor descriptor)
    {
        return descriptor != null && descriptor.isLocalized();
    }

    @Override
    public AnnotationAttribute generate(final TypeAttributeDescriptor object)
    {
        return ANNOTATION_ATTRIBUTE;
    }
}
