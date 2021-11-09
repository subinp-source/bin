/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.generator;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.MetadataPrimitiveData;
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration;
import de.hybris.platform.integrationservices.model.AbstractIntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DefaultIntegrationObjectJsonGenerator implements IntegrationObjectJsonGenerator
{
	private static final String ENDL = " \r\n";
	private static final String DOUBLE_QUOTE = "\"";
	private static final String CURLY_BRACKET_OPEN = "{";
	private static final String CURLY_BRACKET_CLOSE = "}";
	private static final String SQUARE_BRACKET_OPEN = "[";
	private static final String SQUARE_BRACKET_CLOSE = "]";
	private static final String COLON = ": ";
	private static final String COMMA = ",";
	private static final String KEY = "Key";
	private static final String VALUE = "Value";
	private static final String DUMMY_DATE = "2019-01-01T00:00:00";

	private final ReadService readService;
	private final ReadOnlyAttributesConfiguration configuration;

	private IntegrationObjectModel integrationObjectModel;
	private Deque<ComposedTypeModel> ancestors;

	public DefaultIntegrationObjectJsonGenerator(final ReadService readService,
	                                             final ReadOnlyAttributesConfiguration configuration)
	{
		this.readService = readService;
		this.configuration = configuration;
	}

	@Override
	public String generateJson(final IntegrationObjectModel integrationObjectModel)
	{
		final StringBuilder jsonBuilder = new StringBuilder();
		this.integrationObjectModel = integrationObjectModel;
		this.ancestors = new ArrayDeque<>();

		constructJsonForAttributes(jsonBuilder, integrationObjectModel.getRootItem());

		final JsonParser parser = new JsonParser();
		final JsonObject jsonObject = parser.parse(jsonBuilder.toString()).getAsJsonObject();
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(jsonObject);
	}

	private void constructJsonForAttributes(final StringBuilder jsonBuilder,
	                                        final IntegrationObjectItemModel integrationObjectItemModel)
	{
		jsonBuilder.append(CURLY_BRACKET_OPEN);

		final List<AbstractIntegrationObjectItemAttributeModel> attributes = integrationObjectItemModel
				.getAttributes()
				.stream()
				.filter(attribute -> !configuration.getReadOnlyAttributes().contains(attribute.getAttributeName()))
				.collect(Collectors.toList());
		attributes.addAll(integrationObjectItemModel.getClassificationAttributes());

		final List<AbstractIntegrationObjectItemAttributeModel> localizedElements = new ArrayList<>();

		for (final AbstractIntegrationObjectItemAttributeModel attribute : attributes)
		{
			localizedElements.addAll(determineJsonHybrisType(jsonBuilder, attribute));
		}

		if (!localizedElements.isEmpty())
		{
			composeJsonLocalizedSection(jsonBuilder, localizedElements);
		}

		composeJsonIntegrationKey(jsonBuilder);
		jsonBuilder.append(CURLY_BRACKET_CLOSE);
	}

	private List<AbstractIntegrationObjectItemAttributeModel> determineJsonHybrisType(final StringBuilder jsonBuilder,
	                                                                                  final AbstractIntegrationObjectItemAttributeModel attribute)
	{
		boolean isCollection = false;
		boolean isMap = false;
		boolean isLocalized = false;
		ComposedTypeModel composedTypeModel = null;

		final List<AbstractIntegrationObjectItemAttributeModel> localizedElements = new ArrayList<>();

		if (attribute instanceof IntegrationObjectItemAttributeModel)
		{
			final IntegrationObjectItemModel returnIntegrationObjectItem = attribute.getReturnIntegrationObjectItem();
			final AttributeDescriptorModel descriptor = ((IntegrationObjectItemAttributeModel) attribute).getAttributeDescriptor();
			final TypeModel attributeType = descriptor.getAttributeType();
			final String itemType = attributeType.getItemtype();
			final TypeModel typeModel = (returnIntegrationObjectItem != null) ? returnIntegrationObjectItem.getType() : attributeType;
			composedTypeModel = readService.getComposedTypeModelFromTypeModel(typeModel);
			isCollection = readService.isCollectionType(itemType);
			isMap = readService.isMapType(itemType);
			isLocalized = descriptor.getLocalized();
		}
		else if (attribute instanceof IntegrationObjectItemClassificationAttributeModel)
		{
			final ClassAttributeAssignmentModel classAttributeAssignment = ((IntegrationObjectItemClassificationAttributeModel) attribute)
					.getClassAttributeAssignment();
			composedTypeModel = classAttributeAssignment.getReferenceType();
			isCollection = classAttributeAssignment.getMultiValued();
		}

		boolean isAdded = false;
		if (isLocalized)
		{
			localizedElements.add(attribute);
		}
		else if (composedTypeModel != null && !isMap)
		{
			if (!ancestors.contains(composedTypeModel))
			{
				ancestors.addFirst(composedTypeModel);
				composeJsonComposedTypesAndCollections(jsonBuilder, attribute, composedTypeModel, isCollection);
				isAdded = true;
				ancestors.pollFirst();
			}
		}
		else
		{
			if (!isMap)
			{
				composeJsonPrimitive(jsonBuilder, attribute, false);
			}
			else
			{
				constructJsonMapElement(jsonBuilder, attribute);
			}
			isAdded = true;
		}

		if (isAdded)
		{
			jsonBuilder.append(COMMA);
		}

		return localizedElements;
	}

	private void composeJsonComposedTypesAndCollections(final StringBuilder jsonBuilder,
	                                                    final AbstractIntegrationObjectItemAttributeModel attribute,
	                                                    final ComposedTypeModel composedTypeModel,
	                                                    final boolean isCollection)
	{
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(attribute.getAttributeName());
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(COLON);

		if (isCollection)
		{
			jsonBuilder.append(SQUARE_BRACKET_OPEN);
		}

		final IntegrationObjectItemModel matchingIOI = findItemInIntegrationObject(composedTypeModel.getCode());
		constructJsonForAttributes(jsonBuilder, matchingIOI);

		if (isCollection)
		{
			jsonBuilder.append(SQUARE_BRACKET_CLOSE);
		}
	}

	private void composeJsonLocalizedSection(final StringBuilder jsonBuilder,
	                                         final List<AbstractIntegrationObjectItemAttributeModel> localizedElements)
	{
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append("localizedAttributes");
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(COLON);
		jsonBuilder.append(SQUARE_BRACKET_OPEN);
		jsonBuilder.append(CURLY_BRACKET_OPEN);
		composeJsonLocal(jsonBuilder);
		for (final AbstractIntegrationObjectItemAttributeModel element : localizedElements)
		{
			constructJsonMapElement(jsonBuilder, element);
			if (localizedElements.indexOf(element) < localizedElements.size() - 1)
			{
				jsonBuilder.append(COMMA);
			}
		}
		jsonBuilder.append(CURLY_BRACKET_CLOSE);
		jsonBuilder.append(SQUARE_BRACKET_CLOSE);
		jsonBuilder.append(COMMA);
	}

	private void constructJsonMapElement(final StringBuilder jsonBuilder,
	                                     final AbstractIntegrationObjectItemAttributeModel attribute)
	{
		ComposedTypeModel composedTypeModel = null;
		boolean isCollection = false;
		boolean isMapOfMap = false;
		boolean isLocalizedMap = false;
		if (attribute instanceof IntegrationObjectItemAttributeModel)
		{
			final AttributeDescriptorModel attributeDescriptorModel = ((IntegrationObjectItemAttributeModel) attribute).getAttributeDescriptor();
			final TypeModel typeModel = ((MapTypeModel) attributeDescriptorModel.getAttributeType()).getReturntype();
			final String itemType = typeModel.getItemtype();
			isLocalizedMap = attributeDescriptorModel.getLocalized();
			composedTypeModel = readService.getComposedTypeModelFromTypeModel(typeModel);
			isCollection = readService.isCollectionType(itemType);
			isMapOfMap = readService.isMapType(itemType);
		}
		else if (attribute instanceof IntegrationObjectItemClassificationAttributeModel)
		{
			final ClassAttributeAssignmentModel classAttributeAssignment = ((IntegrationObjectItemClassificationAttributeModel) attribute)
					.getClassAttributeAssignment();
			composedTypeModel = classAttributeAssignment.getReferenceType();
			isCollection = classAttributeAssignment.getMultiValued();
		}

		if (composedTypeModel != null)
		{
			composeJsonComposedTypesAndCollections(jsonBuilder, attribute, composedTypeModel, isCollection);
		}
		else
		{
			jsonBuilder.append(DOUBLE_QUOTE);
			jsonBuilder.append(attribute.getAttributeName());
			jsonBuilder.append(DOUBLE_QUOTE);
			jsonBuilder.append(COLON);
			if (!isMapOfMap)
			{
				if (isLocalizedMap)
				{
					composeJsonPrimitive(jsonBuilder, attribute, true);
				}
				else
				{
					jsonBuilder.append(SQUARE_BRACKET_OPEN);
					jsonBuilder.append(CURLY_BRACKET_OPEN);
					composeJsonPrimitive(jsonBuilder, attribute, true);
					jsonBuilder.append(CURLY_BRACKET_CLOSE);
					jsonBuilder.append(SQUARE_BRACKET_CLOSE);
				}
			}
			else
			{
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append("Map of map type value is not supported by JSON generation.");
				jsonBuilder.append(DOUBLE_QUOTE);
			}
		}
	}

	private void composeJsonPrimitive(final StringBuilder jsonBuilder,
	                                  final AbstractIntegrationObjectItemAttributeModel attribute,
	                                  final boolean isFromMap)
	{
		final MetadataPrimitiveData primitiveData;
		if (attribute instanceof IntegrationObjectItemAttributeModel)
		{
			primitiveData = determinePrimitiveType((IntegrationObjectItemAttributeModel) attribute, isFromMap, true);
		}
		else
		{
			primitiveData = determineClassificationType((IntegrationObjectItemClassificationAttributeModel) attribute);
		}

		if (attribute instanceof IntegrationObjectItemAttributeModel && isFromMap)
		{
			final IntegrationObjectItemAttributeModel attributeModel = (IntegrationObjectItemAttributeModel) attribute;
			final AttributeDescriptorModel attributeDescriptorModel = attributeModel.getAttributeDescriptor();
			final MapTypeModel mapTypeModel = (MapTypeModel) attributeDescriptorModel.getAttributeType();
			final boolean isMapKeyComposedType = readService.isComposedType(mapTypeModel.getArgumentType().getItemtype());
			final boolean isLocalizedMap = attributeDescriptorModel.getLocalized();

			if (!(isLocalizedMap && isMapKeyComposedType))
			{
				final MetadataPrimitiveData primitiveKeyData = determinePrimitiveType(attributeModel, true, false);
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(KEY);
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(COLON);
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(primitiveKeyData.getValue()).append("_1");
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(COMMA);
				jsonBuilder.append(ENDL);
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(VALUE);
				jsonBuilder.append(DOUBLE_QUOTE);
				jsonBuilder.append(COLON);
			}
		}
		else
		{
			jsonBuilder.append(DOUBLE_QUOTE);
			jsonBuilder.append(attribute.getAttributeName());
			jsonBuilder.append(DOUBLE_QUOTE);
			jsonBuilder.append(COLON);
		}

		if (primitiveData.isQuoted())
		{
			jsonBuilder.append(DOUBLE_QUOTE);
		}
		jsonBuilder.append(primitiveData.getValue());
		if (primitiveData.isQuoted())
		{
			jsonBuilder.append(DOUBLE_QUOTE);
		}
	}

	private MetadataPrimitiveData determineClassificationType(final IntegrationObjectItemClassificationAttributeModel attribute)
	{
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = attribute.getClassAttributeAssignment();
		final String classificationType = classAttributeAssignmentModel.getAttributeType().getCode();
		final boolean isCollection = classAttributeAssignmentModel.getMultiValued();
		String payloadData = "";
		if (!isCollection)
		{
			return getClassificationPrimitivePayload(attribute);
		}
		else
		{
			if ("string".equals(classificationType))
			{
				payloadData = getCollectionDummyValueForPrimitiveClassificationTypes("abc", "def", true);
			}
			else if ("number".equals(classificationType))
			{
				payloadData = getCollectionDummyValueForPrimitiveClassificationTypes("123.0", "34", true);
			}
			else if ("boolean".equals(classificationType))
			{
				payloadData = getCollectionDummyValueForPrimitiveClassificationTypes("True", "False", false);
			}
			else if ("date".equals(classificationType))
			{
				payloadData = getCollectionDummyValueForPrimitiveClassificationTypes(DUMMY_DATE, DUMMY_DATE, true);
			}
			else if ("enum".equals(classificationType))
			{
				payloadData = getCollectionDummyValueForPrimitiveClassificationTypes("enum_abc", "enum_def", true);
			}
		}
		return new MetadataPrimitiveData(payloadData, false);
	}

	private MetadataPrimitiveData getClassificationPrimitivePayload(final IntegrationObjectItemClassificationAttributeModel attr)
	{
		final String classificationType = attr.getClassAttributeAssignment().getAttributeType().getCode();
		boolean hasTobeQuoted = true;
		String payloadData = "";
		if ("string".equals(classificationType))
		{
			payloadData = "Test_" + attr.getAttributeName();
		}
		else if ("number".equals(classificationType))
		{
			payloadData = "123.0";
		}
		else if ("boolean".equals(classificationType))
		{
			hasTobeQuoted = false;
			payloadData = "True";
		}
		else if ("date".equals(classificationType))
		{
			payloadData = DUMMY_DATE;
		}
		else if ("enum".equals(classificationType))
		{
			payloadData = classificationType + "_TEST";
		}
		return new MetadataPrimitiveData(payloadData, hasTobeQuoted);
	}


	public String getCollectionDummyValueForPrimitiveClassificationTypes(final String value1,
	                                                                     final String value2,
	                                                                     final boolean isValueQuoted)
	{
		final String quoteOrSpace = isValueQuoted ? DOUBLE_QUOTE : "";
		return SQUARE_BRACKET_OPEN + CURLY_BRACKET_OPEN + DOUBLE_QUOTE + "value" + DOUBLE_QUOTE + COLON + quoteOrSpace + value1 + quoteOrSpace + CURLY_BRACKET_CLOSE + COMMA +
				CURLY_BRACKET_OPEN + DOUBLE_QUOTE + "value" + DOUBLE_QUOTE + COLON + quoteOrSpace + value2 + quoteOrSpace + CURLY_BRACKET_CLOSE + SQUARE_BRACKET_CLOSE;
	}

	private MetadataPrimitiveData determinePrimitiveType(final IntegrationObjectItemAttributeModel attribute,
	                                                     final boolean isFromMap,
	                                                     final boolean isForValue)
	{
		final Class valueClassType;
		final Class keyClassType;
		if (!isFromMap)
		{
			valueClassType = ((AtomicTypeModel) attribute.getAttributeDescriptor().getAttributeType()).getJavaClass();
			keyClassType = Object.class;
		}
		else
		{
			final AttributeDescriptorModel attributeDescriptorModel = attribute.getAttributeDescriptor();
			final MapTypeModel mapTypeModel = (MapTypeModel) attributeDescriptorModel.getAttributeType();
			final TypeModel mapKeyType = mapTypeModel.getArgumentType();
			final TypeModel mapValueType = mapTypeModel.getReturntype();
			valueClassType = ((AtomicTypeModel) mapValueType).getJavaClass();
			if (attributeDescriptorModel.getLocalized() && readService.isComposedType(mapKeyType.getItemtype()))
			{
				keyClassType = Object.class;
			}
			else
			{
				keyClassType = ((AtomicTypeModel) mapKeyType).getJavaClass();
			}
		}
		if (isForValue)
		{
			return buildMetadataPrimitiveData(attribute, valueClassType);
		}
		else
		{
			return buildMetadataPrimitiveData(attribute, keyClassType);
		}
	}

	private MetadataPrimitiveData buildMetadataPrimitiveData(final IntegrationObjectItemAttributeModel attribute,
	                                                         final Class classType)
	{
		if (classType.isAssignableFrom(String.class))
		{
			return new MetadataPrimitiveData("Test_" + attribute.getAttributeName(), true);
		}
		else if (classType.isAssignableFrom(Integer.class))
		{
			return new MetadataPrimitiveData("123", false);
		}
		else if (classType.isAssignableFrom(Character.class))
		{
			return new MetadataPrimitiveData("a", true);
		}
		else if (classType.isAssignableFrom(Double.class))
		{
			return new MetadataPrimitiveData("123.0", true);
		}
		else if (classType.isAssignableFrom(Boolean.class))
		{
			return new MetadataPrimitiveData("True", false);
		}
		else if (classType.isAssignableFrom(Long.class))
		{
			return new MetadataPrimitiveData("12345", true);
		}
		else if (classType.isAssignableFrom(Short.class))
		{
			return new MetadataPrimitiveData("123", true);
		}
		else if (classType.isAssignableFrom(BigInteger.class))
		{
			return new MetadataPrimitiveData("123456", true);
		}
		else if (classType.isAssignableFrom(BigDecimal.class))
		{
			return new MetadataPrimitiveData("123456.00", true);
		}
		else if (classType.isAssignableFrom(Byte.class))
		{
			return new MetadataPrimitiveData("255", false);
		}
		else if (classType.isAssignableFrom(Date.class))
		{
			return new MetadataPrimitiveData(DUMMY_DATE, true);
		}
		else if (classType.isAssignableFrom(Float.class))
		{
			return new MetadataPrimitiveData("123.0f", true);
		}
		else
		{
			return new MetadataPrimitiveData(classType.getName() + "_TEST", true);
		}
	}

	private void composeJsonIntegrationKey(final StringBuilder jsonBuilder)
	{
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append("integrationKey");
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(COLON);
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append("TEST_KEY");
		jsonBuilder.append(DOUBLE_QUOTE);
	}

	private void composeJsonLocal(final StringBuilder jsonBuilder)
	{
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append("language");
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(COLON);
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append("en");
		jsonBuilder.append(DOUBLE_QUOTE);
		jsonBuilder.append(COMMA);
	}

	private IntegrationObjectItemModel findItemInIntegrationObject(final String code)
	{
		final Optional<IntegrationObjectItemModel> optionalIntegrationObjectItemModel = integrationObjectModel.getItems().stream()
		                                                                                                      .filter(item -> item.getCode().equals(code))
		                                                                                                      .findFirst();

		final IntegrationObjectItemModel integrationObjectItemModel = optionalIntegrationObjectItemModel
				.orElseThrow(() -> new NoSuchElementException(String.format("No IntegrationObjectItem found with code %s", code)));

		return integrationObjectItemModel;
	}
}
