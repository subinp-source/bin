/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * Generate an annotation for EDMX property based on whether the property is nullable or not.
 * example:
 * <code><Property Nullable="true" .../></code>
 */
public class NullablePropertyAnnotationGenerator implements PropertyAnnotationGenerator
{
	private static final String NULLABLE = "Nullable";

	@Override
	public boolean isApplicable(final TypeAttributeDescriptor descriptor)
	{
		return descriptor != null;
	}

	@Override
	public AnnotationAttribute generate(final TypeAttributeDescriptor descriptor)
	{
		return nullableAttribute(descriptor.isNullable());
	}

	private static AnnotationAttribute nullableAttribute(final boolean isNullable)
	{
		return new AnnotationAttribute()
				.setName(NULLABLE)
				.setText(String.valueOf(isNullable));
	}
}
