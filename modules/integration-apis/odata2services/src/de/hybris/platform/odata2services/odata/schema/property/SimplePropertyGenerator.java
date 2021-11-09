/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.property;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.utils.EdmTypeUtils;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;

/**
 * A generator for {@code <Property Name=".." Type="..."/>} elements in the EDMX.
 */
public class SimplePropertyGenerator implements SchemaElementGenerator<SimpleProperty, TypeAttributeDescriptor>
{
	private SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> annotationsGenerator;

	/**
	 * Generates EDM Property based on the attribute description in the Integration Object model.
	 * @param descriptor descriptor of the attribute in an Integration Object. Attribute descriptor cannot be {@code null}.
	 * @return EDM property corresponding to the specified integration object item attribute.
	 */
	@Override
	public SimpleProperty generate(@NotNull final TypeAttributeDescriptor descriptor)
	{
		return new SimpleProperty()
				.setName(descriptor.getAttributeName())
				.setType(EdmTypeUtils.convert(descriptor.getAttributeType().getTypeCode()))
				.setAnnotationAttributes(generateAnnotations(descriptor));
	}

	private List<AnnotationAttribute> generateAnnotations(final TypeAttributeDescriptor descriptor)
	{
		return annotationsGenerator != null
				? annotationsGenerator.generate(descriptor)
				: Collections.emptyList();
	}

	protected SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> getAnnotationsGenerator()
	{
		return annotationsGenerator;
	}

	public void setAnnotationsGenerator(final SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> generator)
	{
		annotationsGenerator = generator;
	}
}
