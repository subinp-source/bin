/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.NestedAbstractItemTypeCannotBeCreatedException;
import de.hybris.platform.odata2services.odata.UniqueCollectionNotAllowedException;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.association.AssociationGenerator;
import de.hybris.platform.odata2services.odata.schema.association.AssociationGeneratorRegistry;
import de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils;

import java.util.List;
import java.util.Optional;

import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.springframework.beans.factory.annotation.Required;

public class NavigationPropertySchemaElementGenerator implements SchemaElementGenerator<Optional<NavigationProperty>, TypeAttributeDescriptor>
{
	private AssociationGeneratorRegistry associationGeneratorRegistry;
	private SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> propertyAnnotationListGenerator;

	@Override
	public Optional<NavigationProperty> generate(final TypeAttributeDescriptor attribute)
	{
		final Optional<AssociationGenerator> associationGeneratorOptional = getAssociationGeneratorRegistry().getAssociationGenerator(attribute);
		return associationGeneratorOptional
				.map(associationGenerator -> generateInternal(associationGenerator, attribute));
	}

	private NavigationProperty generateInternal(final AssociationGenerator associationGenerator, final TypeAttributeDescriptor attribute)
	{
		return attribute != null ? createNavigationProperty(associationGenerator, attribute) : null;
	}

	private NavigationProperty createNavigationProperty(final AssociationGenerator associationGenerator, final TypeAttributeDescriptor attribute)
	{
		final Association association = associationGenerator.generate(attribute);
		validateNavigationProperty(attribute);
		return new NavigationProperty()
				.setName(attribute.getAttributeName())
				.setRelationship(new FullQualifiedName(SchemaUtils.NAMESPACE, association.getName()))
				.setFromRole(association.getEnd1().getRole())
				.setToRole(association.getEnd2().getRole())
				.setAnnotationAttributes(getPropertyAnnotationListGenerator().generate(attribute));
	}

	private void validateNavigationProperty(final TypeAttributeDescriptor attributeDescriptor)
	{
		if(attributeDescriptor.isAutoCreate() && attributeDescriptor.getAttributeType().isAbstract())
		{
			throw new NestedAbstractItemTypeCannotBeCreatedException(attributeDescriptor);
		}

		if (attributeDescriptor.isKeyAttribute() && isCollection(attributeDescriptor))
		{
			throw new UniqueCollectionNotAllowedException(attributeDescriptor);
		}
	}


	private boolean isCollection(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.isCollection() || descriptor.isMap();
	}

	protected SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> getPropertyAnnotationListGenerator()
	{
		return propertyAnnotationListGenerator;
	}

	@Required
	public void setPropertyAnnotationListGenerator(final SchemaElementGenerator<List<AnnotationAttribute>, TypeAttributeDescriptor> propertyAnnotationListGenerator)
	{
		this.propertyAnnotationListGenerator = propertyAnnotationListGenerator;
	}

	protected AssociationGeneratorRegistry getAssociationGeneratorRegistry()
	{
		return associationGeneratorRegistry;
	}

	@Required
	public void setAssociationGeneratorRegistry(final AssociationGeneratorRegistry associationGeneratorRegistry)
	{
		this.associationGeneratorRegistry = associationGeneratorRegistry;
	}
}