/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.navigation;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.springframework.beans.factory.annotation.Required;

public class NavigationPropertyListSchemaElementGenerator implements SchemaElementGenerator<List<NavigationProperty>, TypeDescriptor>
{
	private SchemaElementGenerator<Optional<NavigationProperty>, TypeAttributeDescriptor> navigationPropertyGenerator;

	@Override
	public List<NavigationProperty> generate(final TypeDescriptor typeDescriptor)
	{
		return typeDescriptor != null ? generateInternal(typeDescriptor) : Collections.emptyList();
	}

	private List<NavigationProperty> generateInternal(final TypeDescriptor typeDescriptor)
	{
		return typeDescriptor.getAttributes().stream()
				.map(getNavigationPropertyGenerator()::generate)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	protected SchemaElementGenerator<Optional<NavigationProperty>, TypeAttributeDescriptor> getNavigationPropertyGenerator()
	{
		return navigationPropertyGenerator;
	}

	@Required
	public void setNavigationPropertyGenerator(final SchemaElementGenerator<Optional<NavigationProperty>, TypeAttributeDescriptor> navigationPropertyGenerator)
	{
		this.navigationPropertyGenerator = navigationPropertyGenerator;
	}
}