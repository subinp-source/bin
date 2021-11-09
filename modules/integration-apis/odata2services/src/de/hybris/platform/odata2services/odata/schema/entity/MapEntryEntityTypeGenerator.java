/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.entity;

import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.MapDescriptor;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;
import de.hybris.platform.odata2services.odata.schema.attribute.ImmutableAnnotationAttribute;
import de.hybris.platform.odata2services.odata.schema.utils.EdmTypeUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;

/**
 * Generates entities for attribute that have {@link java.util.Map} as their values.
 */
public class MapEntryEntityTypeGenerator implements EntityTypeGenerator
{
	private static final AnnotationAttribute REQUIRED_ATTRIBUTE = annotation("Nullable", "false");
	private static final AnnotationAttribute NULLABLE_ATTRIBUTE = annotation("Nullable", "true");
	private static final AnnotationAttribute UNIQUE_ATTRIBUTE = annotation("s:IsUnique", "true");

	private DescriptorFactory descriptorFactory;
	private SchemaElementGenerator<Optional<Key>, List<Property>> keyGenerator;

	@Override
	public List<EntityType> generate(final IntegrationObjectItemModel item)
	{
		return descriptorFactory != null
				? generate(descriptorFactory.createItemTypeDescriptor(item))
				: Collections.emptyList();
	}

	public List<EntityType> generate(final TypeDescriptor itemType)
	{
		return itemType.getAttributes().stream()
		               .filter(TypeAttributeDescriptor::isMap)
		               .filter(d -> !d.isLocalized())
		               .map(this::generateEntity)
		               .collect(Collectors.toList());
	}

	private EntityType generateEntity(final TypeAttributeDescriptor descriptor)
	{
		final List<Property> properties = generateProperties(descriptor);
		return new EntityType()
				.setName(mapTypeCode(descriptor))
				.setProperties(properties)
				.setKey(generateKey(properties));
	}

	private List<Property> generateProperties(final TypeAttributeDescriptor descriptor)
	{
		return Arrays.asList(keyProperty(descriptor), valueProperty(descriptor));
	}

	private Property keyProperty(final TypeAttributeDescriptor descriptor)
	{
		return new SimpleProperty()
				.setName("key")
				.setType(deriveType(descriptor, MapDescriptor::getKeyType))
				.setAnnotationAttributes(Arrays.asList(REQUIRED_ATTRIBUTE, UNIQUE_ATTRIBUTE));
	}

	private Property valueProperty(final TypeAttributeDescriptor descriptor)
	{
		return new SimpleProperty()
				.setName("value")
				.setType(deriveType(descriptor, MapDescriptor::getValueType))
				.setAnnotationAttributes(Collections.singletonList(NULLABLE_ATTRIBUTE));
	}

	private EdmSimpleTypeKind deriveType(final TypeAttributeDescriptor descriptor,
	                                     final Function<MapDescriptor, TypeDescriptor> typeSupplier)
	{
		final String type = descriptor.getMapDescriptor()
		                              .map(typeSupplier)
		                              .map(TypeDescriptor::getTypeCode)
		                              // never the case: non-map attributes are filtered out in the generate() method
		                              .orElse("Unknown");
		return EdmTypeUtils.convert(type);
	}

	@NotNull
	private String mapTypeCode(final TypeAttributeDescriptor attribute)
	{
		return attribute.getAttributeType().getTypeCode();
	}

	private static AnnotationAttribute annotation(final String name, final String text)
	{
		return new ImmutableAnnotationAttribute().setName(name).setText(text);
	}

	private Key generateKey(final List<Property> properties)
	{
		return keyGenerator != null
				? keyGenerator.generate(properties).orElse(null)
				: null;
	}

	/**
	 * Injects descriptor factory to use.
	 *
	 * @param factory factory implementation to be used by this generator.
	 */
	public void setDescriptorFactory(final DescriptorFactory factory)
	{
		descriptorFactory = factory;
	}

	public void setKeyGenerator(final SchemaElementGenerator<Optional<Key>, List<Property>> generator)
	{
		keyGenerator = generator;
	}
}
