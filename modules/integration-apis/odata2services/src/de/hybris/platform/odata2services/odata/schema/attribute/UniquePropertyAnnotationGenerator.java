/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * Generates an annotation for EDMX property when the property is unique.
 * example:
 * {@code <Property s:IsUnique="true" ... />}
 */
public class UniquePropertyAnnotationGenerator implements PropertyAnnotationGenerator
{
	private static final String IS_UNIQUE = "s:IsUnique";
	private static final AnnotationAttribute ANNOTATION_ATTRIBUTE = new ImmutableAnnotationAttribute()
			.setName(IS_UNIQUE)
			.setText("true");

	@Override
	public boolean isApplicable(final TypeAttributeDescriptor descriptor)
	{
		return descriptor != null && descriptor.isKeyAttribute();
	}

	@Override
	public AnnotationAttribute generate(final TypeAttributeDescriptor descriptor)
	{
		return ANNOTATION_ATTRIBUTE;
	}
}
