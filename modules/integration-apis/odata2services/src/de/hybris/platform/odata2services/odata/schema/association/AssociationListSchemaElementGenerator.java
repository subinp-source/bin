/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.association;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.springframework.beans.factory.annotation.Required;

public class AssociationListSchemaElementGenerator implements SchemaElementGenerator<List<Association>, Collection<TypeDescriptor>>
{
	private AssociationGeneratorRegistry associationGeneratorRegistry;

	@Override
	public List<Association> generate(final Collection<TypeDescriptor> descriptors)
	{
		if (CollectionUtils.isNotEmpty(descriptors))
		{
			return descriptors.stream()
					.filter(descriptor -> CollectionUtils.isNotEmpty(descriptor.getAttributes()))
					.flatMap(descriptor -> descriptor.getAttributes().stream())
					.map(this::generateAssociation)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private Optional<Association> generateAssociation(final TypeAttributeDescriptor attribute)
	{
		return getAssociationGeneratorRegistry()
				.getAssociationGenerator(attribute)
				.map(generator -> generator.generate(attribute));
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
