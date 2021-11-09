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
package de.hybris.platform.integrationservices.model;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MockIntegrationObjectItemModelBuilder
{
	private final Set<BaseMockItemAttributeModelBuilder> attributeBuilders = new HashSet<>();
	private final Set<BaseMockItemAttributeModelBuilder> uniqueAttributeBuilders = new HashSet<>();
	private final Set<BaseMockItemAttributeModelBuilder> keyAttributeBuilders = new HashSet<>();
	private final Set<IntegrationObjectItemAttributeModel> attributes = new HashSet<>();
	private final Set<IntegrationObjectItemAttributeModel> uniqueAttributes = new HashSet<>();
	private final Set<IntegrationObjectItemAttributeModel> keyAttributes = new HashSet<>();
	private String itemCode = "SomeItem";
	private ComposedTypeModel itemModel;
	private String integrationObjectName;

	private MockIntegrationObjectItemModelBuilder()
	{}

	public static MockIntegrationObjectItemModelBuilder itemModelBuilder()
	{
		return new MockIntegrationObjectItemModelBuilder();
	}

	public MockIntegrationObjectItemModelBuilder withCode(final String code)
	{
		itemCode = code;
		return this;
	}

	public MockIntegrationObjectItemModelBuilder withItemModelCode(final String code)
	{
		return withItemModel(composedTypeModel(code));
	}

	public MockIntegrationObjectItemModelBuilder withItemModel(final ComposedTypeModel model)
	{
		itemModel = model;
		return this;
	}

	public MockIntegrationObjectItemModelBuilder withAttribute(final BaseMockItemAttributeModelBuilder attributeBuilder)
	{
		attributeBuilders.add(attributeBuilder);
		return this;
	}

	public MockIntegrationObjectItemModelBuilder withUniqueAttribute(final BaseMockItemAttributeModelBuilder attribute)
	{
		uniqueAttributeBuilders.add(attribute);
		return this;
	}

	public MockIntegrationObjectItemModelBuilder withKeyAttribute(final BaseMockItemAttributeModelBuilder attribute)
	{
		keyAttributeBuilders.add(attribute);
		return this;
	}

	public MockIntegrationObjectItemModelBuilder withIntegrationObject(final String name)
	{
		integrationObjectName = name;
		return this;
	}

	public IntegrationObjectItemModel build()
	{
		final IntegrationObjectItemModel item = mock(IntegrationObjectItemModel.class);
		buildAttributes(item);
		return stubItem(item);
	}

	private void buildAttributes(final IntegrationObjectItemModel item)
	{
		provideItemCodeToReferenceAttributeBuilders();
		buildUniqueAttributes();
		buildKeyAttributes(item);

		final Set<IntegrationObjectItemAttributeModel> attributesFromBuilders = attributeBuilders.stream()
				.map(BaseMockItemAttributeModelBuilder::build)
				.collect(Collectors.toSet());
		attributes.addAll(attributesFromBuilders);
		attributeBuilders.clear();
	}

	private void buildUniqueAttributes()
	{
		final Set<IntegrationObjectItemAttributeModel> attributesFromBuilders = uniqueAttributeBuilders.stream()
				.map(BaseMockItemAttributeModelBuilder::build)
				.collect(Collectors.toSet());
		uniqueAttributes.addAll(attributesFromBuilders);
		uniqueAttributeBuilders.clear();
	}

	private void buildKeyAttributes(final IntegrationObjectItemModel item)
	{
		final Set<IntegrationObjectItemAttributeModel> attributesFromBuilders = keyAttributeBuilders.stream()
				.map(a -> mockIntegrationObjectItem(item, a))
				.map(BaseMockItemAttributeModelBuilder::build)
				.collect(Collectors.toSet());
		keyAttributes.addAll(attributesFromBuilders);
		keyAttributeBuilders.clear();
	}

	private void provideItemCodeToReferenceAttributeBuilders()
	{
		attributeBuilders.stream()
				.map(b -> b.withIntegrationObjectItemCode(itemCode))
				.filter(MockOneToOneRelationItemAttributeModelBuilder.class::isInstance)
				.map(MockOneToOneRelationItemAttributeModelBuilder.class::cast)
				.forEach(b -> b.withSource(itemCode));
	}

	private IntegrationObjectItemModel stubItem(final IntegrationObjectItemModel item)
	{
		final IntegrationObjectModel object = integrationObject(integrationObjectName);
		final ComposedTypeModel typeModel = itemModel == null
				? composedTypeModel(itemCode)
				: itemModel;

		when(item.getIntegrationObject()).thenReturn(object);
		when(item.getAttributes()).thenReturn(attributes);
		when(item.getCode()).thenReturn(itemCode);
		when(item.getKeyAttributes()).thenReturn(keyAttributes);
		when(item.getUniqueAttributes()).thenReturn(uniqueAttributes);
		when(item.getType()).thenReturn(typeModel);
		return item;
	}

	private BaseMockItemAttributeModelBuilder mockIntegrationObjectItem(final IntegrationObjectItemModel thisItem, final BaseMockItemAttributeModelBuilder attribute)
	{
		return attribute.withIntegrationObjectItem(thisItem);
	}

	private IntegrationObjectModel integrationObject(final String name)
	{
		final IntegrationObjectModel model = mock(IntegrationObjectModel.class);
		when(model.getCode()).thenReturn(name);
		return model;
	}

	public static ComposedTypeModel composedTypeModel(final String itemCode)
	{
		final ComposedTypeModel model = mock(ComposedTypeModel.class);
		doReturn(itemCode).when(model).getCode();
		return model;
	}

	public static EnumerationMetaTypeModel enumerationModel(final String itemCode)
	{
		final EnumerationMetaTypeModel model = mock(EnumerationMetaTypeModel.class);
		doReturn(itemCode).when(model).getCode();
		return model;
	}
}
