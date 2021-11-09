/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * A blueprint for creating EDMX schema property annotation generators.
 */
public interface PropertyAnnotationGenerator extends SchemaElementGenerator<AnnotationAttribute, TypeAttributeDescriptor>
{
	/**
	 * Determines if this annotation generator is applicable for the given property
	 * @param descriptor the TypeAttributeDescriptor to check
	 * @return true if applicable, otherwise false
	 */
	boolean isApplicable(final TypeAttributeDescriptor descriptor);
}
