/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.schema.property;

import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

public abstract class AbstractPropertyListGenerator implements SchemaElementGenerator<List<Property>, IntegrationObjectItemModel>
{
	private SchemaElementGenerator<Property, IntegrationObjectItemAttributeModel> propertyGenerator;
	private SchemaElementGenerator<SimpleProperty, TypeAttributeDescriptor> simplePropertyGenerator;
	private DescriptorFactory descriptorFactory;

	/**
	 * {@inheritDoc}
	 * <p>Converts {@code itemMode} to a {@link TypeDescriptor} and then delegates to {@link #generate(TypeDescriptor)}</p>
	 * @param itemModel integration object item to generate simple EDM properties for.
	 * @return a list of simple EDM properties or an empty list, if the integration object item does not have properties or has
	 * only navigation properties referring other integration object items.
	 * @see #generate(TypeDescriptor)
	 */
	@Override
	public List<Property> generate(final IntegrationObjectItemModel itemModel) {
		final var typeDescriptor = getDescriptorFactory().createItemTypeDescriptor(itemModel);
		return generate(typeDescriptor);
	}

	/**
	 * Generates simple properties for simple (not navigation) attributes declared in the type descriptor
	 * @param typeDescriptor describes integration object item, for which simple EDM properties need to be generated.
	 * @return a list of simple EDM properties or an empty list, if the integration object item does not have properties or has
	 * only navigation properties referring other integration object items.
	 */
	public List<Property> generate(final TypeDescriptor typeDescriptor)
	{
		return typeDescriptor.getAttributes().stream()
				.filter(this::isSimpleAttribute)
				.filter(this::isApplicable)
				.map(getSimplePropertyGenerator()::generate)
				.collect(Collectors.toList());
	}

	private boolean isSimpleAttribute(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.isPrimitive() && !descriptor.isCollection();
	}

	/**
	 * Allows subclasses to have a hook into the decision of whether an attribute should be presented as a simple property. This
	 * class already checks whether the attribute is simple, i.e. it's a primitive and not a collection, before
	 * checking this method.
	 * @param descriptor an attribute descriptor to decided about whether it should be presented as EDM property or not.
	 * @return {@code true}, if the attribute should be converted to a property; {@code false}, otherwise. This default
	 * implementation always returns {@code true}.
	 */
	protected abstract boolean isApplicable(TypeAttributeDescriptor descriptor);

	/**
	 * @deprecated not used because {@code itemModel} is not dereferenced anymore by this generator. It is simply passed to
	 * {@link DescriptorFactory}. Therefore it's up to the factory implementation to allow or deny {@code null} values.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected void validatePreconditions(final IntegrationObjectItemModel itemModel)
	{
		Preconditions.checkArgument(itemModel != null, "A Property list cannot be generated from a null parameter");
	}

	/**
	 * @deprecated not used because properties are generated off {@link TypeAttributeDescriptor}s now.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected List<Property> generateProperties(final Set<IntegrationObjectItemAttributeModel> attributeModels)
	{
		return CollectionUtils.isEmpty(attributeModels)
				? Collections.emptyList()
				: generate(attributeModels.iterator().next().getIntegrationObjectItem());
	}

	/**
	 * @deprecated not used because generation is done by {@link #generate(TypeDescriptor)} method. There is no need to convert
	 * {@link IntegrationObjectItemAttributeModel}s to {@code TypeAttributeDescriptor}s because attribute descriptors can be
	 * retrieved by calling {@link TypeDescriptor#getAttributes()}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected TypeAttributeDescriptor asDescriptor(final IntegrationObjectItemAttributeModel attributeModel)
	{
		return getDescriptorFactory().createTypeAttributeDescriptor(attributeModel);
	}

	protected DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	@Required
	public void setDescriptorFactory(final DescriptorFactory factory)
	{
		descriptorFactory = factory;
	}

	protected SchemaElementGenerator<SimpleProperty, TypeAttributeDescriptor> getSimplePropertyGenerator()
	{
		return simplePropertyGenerator;
	}

	@Required
	public void setSimplePropertyGenerator(final SchemaElementGenerator<SimpleProperty, TypeAttributeDescriptor> generator)
	{
		simplePropertyGenerator = generator;
	}

	/**
	 * @deprecated use {@link #getSimplePropertyGenerator()}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected SchemaElementGenerator<Property, IntegrationObjectItemAttributeModel> getPropertyGenerator()
	{
		return propertyGenerator;
	}

	/**
	 * @deprecated use {@link #setSimplePropertyGenerator(SchemaElementGenerator)}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public void setPropertyGenerator(final SchemaElementGenerator<Property, IntegrationObjectItemAttributeModel> propertyGenerator)
	{
		this.propertyGenerator = propertyGenerator;
	}
}
