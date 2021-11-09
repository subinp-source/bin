/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.buildAssociationName;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.localizedEntityName;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.toFullQualifiedName;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.attribute.ImmutableAnnotationAttribute;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;

import com.google.common.base.Preconditions;

/**
 * The LocalizedAttributeNavigationPropertyListSchemaElementGenerator creates the navigation property that associates
 * the localized entity with this entity. Since there is only one localized navigation property
 * per entity, this generator returns a collection containing one navigation property.
 */
public class LocalizedAttributeNavigationPropertyListSchemaElementGenerator implements SchemaElementGenerator<List<NavigationProperty>, TypeDescriptor>
{
	private static final AnnotationAttribute NULLABLE_ATTRIBUTE = new ImmutableAnnotationAttribute().setName("Nullable").setText("true");

	@Override
	public List<NavigationProperty> generate(final TypeDescriptor typeDescriptor)
	{
		Preconditions.checkArgument(typeDescriptor != null,
				"A NavigationProperty list cannot be generated from a null parameter");

		return hasLocalizedAttributes(typeDescriptor) ?
				Collections.singletonList(createNavigationProperty(typeDescriptor)) :
				Collections.emptyList();
	}

	private boolean hasLocalizedAttributes(final TypeDescriptor typeDescriptor)
	{
		final var attributes = typeDescriptor.getAttributes();
		return CollectionUtils.isNotEmpty(attributes) && attributes.stream().anyMatch(TypeAttributeDescriptor::isLocalized);
	}

	private NavigationProperty createNavigationProperty(final TypeDescriptor descriptor)
	{
		final String typeCode = descriptor.getItemCode();
		return new NavigationProperty()
				.setName(LOCALIZED_ATTRIBUTE_NAME)
				.setRelationship(associationName(typeCode))
				.setFromRole(typeCode)
				.setToRole(localizedEntityName(typeCode))
				.setAnnotationAttributes(Collections.singletonList(NULLABLE_ATTRIBUTE));
	}

	private static FullQualifiedName associationName(final String typeCode)
	{
		return toFullQualifiedName(buildAssociationName(typeCode, LOCALIZED_ATTRIBUTE_NAME));
	}
}
