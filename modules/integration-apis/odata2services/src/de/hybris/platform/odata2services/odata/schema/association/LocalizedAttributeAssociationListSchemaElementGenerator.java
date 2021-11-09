/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.association;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.toFullQualifiedName;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;

/**
 * The LocalizedAttributeAssociationListSchemaElementGenerator creates the association between
 * the localized entity and the entity. Since there is only one localized association
 * per entity, this generator returns a collection containing one association.
 */
public class LocalizedAttributeAssociationListSchemaElementGenerator implements SchemaElementGenerator<List<Association>, Collection<TypeDescriptor>>
{
	@Override
	public List<Association> generate(final Collection<TypeDescriptor> descriptors)
	{
		return CollectionUtils.isNotEmpty(descriptors) ?
				generateInternal(descriptors) :
				Collections.emptyList();
	}

	private List<Association> generateInternal(final Collection<TypeDescriptor> descriptors)
	{
		return descriptors.stream()
				.filter(this::hasLocalizedAttributes)
				.map(this::createAssociation)
				.collect(Collectors.toList());
	}

	private boolean hasLocalizedAttributes(final TypeDescriptor descriptor)
	{
		final var attributes = descriptor.getAttributes();
		return CollectionUtils.isNotEmpty(attributes) && attributes.stream().anyMatch(TypeAttributeDescriptor::isLocalized);
	}

	private Association createAssociation(final TypeDescriptor descriptor)
	{
		final String sourceRole = descriptor.getItemCode();
		final String targetRole = SchemaUtils.localizedEntityName(sourceRole);
		return new Association()
				.setName(SchemaUtils.buildAssociationName(sourceRole, LOCALIZED_ATTRIBUTE_NAME))
				.setEnd1(new AssociationEnd()
						.setType(toFullQualifiedName(sourceRole))
						.setRole(sourceRole)
						.setMultiplicity(EdmMultiplicity.ONE))
				.setEnd2(new AssociationEnd()
						.setType(toFullQualifiedName(targetRole))
						.setRole(targetRole)
						.setMultiplicity(EdmMultiplicity.MANY));
	}
}
