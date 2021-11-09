/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * Generate an annotation for EDMX property when auto create is true.
 * example:
 * <code><Property s:IsAutoCreate="true" .../></code>
 */
public class AutoCreatePropertyAnnotationGenerator implements PropertyAnnotationGenerator
{
	private static final String IS_AUTO_CREATE = "s:IsAutoCreate";
	private static final AnnotationAttribute ANNOTATION_ATTRIBUTE = new ImmutableAnnotationAttribute()
																		.setName(IS_AUTO_CREATE)
																		.setText("true");

	@Override
	public boolean isApplicable(final TypeAttributeDescriptor descriptor)
	{
		return descriptor != null && descriptor.isAutoCreate();
	}

	@Override
	public AnnotationAttribute generate(final TypeAttributeDescriptor descriptor)
	{
		return ANNOTATION_ATTRIBUTE;
	}
}
