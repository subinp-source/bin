/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.odata.persistence.ConversionOptions.conversionOptionsBuilder;
import static de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessorTestUtils.propertyMetadata;
import static de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessorTestUtils.typeAttributeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor;
import de.hybris.platform.integrationservices.service.AttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.odata.InvalidDataException;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.creation.NeverCreateItemStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmAnnotationAttribute;
import org.apache.olingo.odata2.api.edm.EdmAnnotations;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.edm.EdmType;
import org.apache.olingo.odata2.api.edm.EdmTypeKind;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * @deprecated remove when {@link PropertyProcessor#processItem(ItemModel, StorageRequest)} is removed
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EntityPropertyProcessorDeprecatedUnitTest
{
	private static final String IS_PART_OF = "s:IsPartOf";
	private static final String IS_AUTO_CREATE = "s:IsAutoCreate";
	private static final Locale LOCALE = Locale.ENGLISH;
	private static final String INTEGRATION_OBJECT_CODE = "IntegrationObjectType";

	@Mock
	private ModelService modelService;
	@Mock
	private ModelEntityService entityService;
	@Mock
	private TypeService typeService;
	@Mock
	private IntegrationObjectService integrationObjectService;
	@Mock
	private EdmEntitySet entitySet;
	@Mock
	private EdmEntityType entityType;
	@Mock
	private ODataEntryImpl oDataEntry;
	@Mock
	private ItemModel item;
	@Mock
	private ItemConversionRequest conversionRequest;
	@Mock
	private ItemTypeDescriptorService itemTypeDescriptorService;
	@InjectMocks
	@Spy
	private EntityPropertyProcessor propertyProcessor;
	private Map<String, Object> properties;
	private StorageRequest storageRequest;

	@Before
	public void setUp() throws EdmException
	{
		properties = Maps.newHashMap();

		when(conversionRequest.getEntityType()).thenReturn(entityType);
		when(conversionRequest.getValue()).thenReturn(item);
		when(conversionRequest.getOptions()).thenReturn(conversionOptionsBuilder().build());

		when(oDataEntry.getProperties()).thenReturn(properties);
		when(item.getItemtype()).thenReturn("MyType");
		when(entityType.getName()).thenReturn("MyType");
		when(entityType.getPropertyNames()).thenReturn(new ArrayList<>());

		final EdmEntityContainer entityContainer = mock(EdmEntityContainer.class);
		when(entitySet.getEntityType()).thenReturn(entityType);
		when(entitySet.getEntityContainer()).thenReturn(entityContainer);
		when(entityContainer.getEntitySet(any())).thenReturn(entitySet);

		when(entityService.createOrUpdateItem(any(), any())).thenReturn(mock(ItemModel.class));
		when(entityService.getODataEntry(any())).thenReturn(mock(ODataEntry.class));

		storageRequest = storageRequestBuilder().build();
	}

	private StorageRequest.StorageRequestBuilder storageRequestBuilder()
	{
		return StorageRequest.storageRequestBuilder()
				.withEntitySet(entitySet)
				.withContentLocale(LOCALE)
				.withAcceptLocale(LOCALE)
				.withODataEntry(oDataEntry)
				.withIntegrationObject(INTEGRATION_OBJECT_CODE)
				.withIntegrationItem(mock(IntegrationItem.class));
	}

	private void givenProperty(final String name, final String... annotations) throws EdmException
	{
		final ODataEntry entry1 = mock(ODataEntryImpl.class);
		this.properties.put(name, entry1);

		final EdmNavigationProperty edmNavigationProperty = mock(EdmNavigationProperty.class);
		when(edmNavigationProperty.getName()).thenReturn(name);
		when(entityType.getProperty(name)).thenReturn(edmNavigationProperty);

		when(edmNavigationProperty.getMultiplicity()).thenReturn(EdmMultiplicity.ONE);
		final EdmType edmType = mock(EdmType.class);
		when(edmType.getKind()).thenReturn(EdmTypeKind.ENTITY);
		when(edmNavigationProperty.getType()).thenReturn(edmType);
		when(edmType.getName()).thenReturn("edmTypedName");

		final EdmAnnotations edmAnnotations = mock(EdmAnnotations.class);

		final List<EdmAnnotationAttribute> attributes = Arrays.stream(annotations).map(a -> {
			final EdmAnnotationAttribute annotationAttribute = mock(EdmAnnotationAttribute.class);
			when(annotationAttribute.getName()).thenReturn(a);
			when(annotationAttribute.getText()).thenReturn("true");
			return annotationAttribute;
		}).collect(Collectors.toList());

		when(edmAnnotations.getAnnotationAttributes()).thenReturn(attributes);
		when(edmNavigationProperty.getAnnotations()).thenReturn(edmAnnotations);

		mockAttributeDescriptor(name, entityType.getName());
	}

	private void mockAttributeDescriptor(final String name, final String parentType)
	{
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		when(attributeDescriptorModel.getLocalized()).thenReturn(false);
		when(attributeDescriptorModel.getItemtype()).thenReturn(parentType);
		when(attributeDescriptorModel.getName()).thenReturn(name);
		when(attributeDescriptorModel.getQualifier()).thenReturn(name);
		when(attributeDescriptorModel.getWritable()).thenReturn(true);
		doAnswer(i -> attributeDescriptorModel)
				.when(typeService)
				.getAttributeDescriptor(anyString(), eq(name));
		when(integrationObjectService.findItemAttributeName(anyString(), eq(parentType), eq(name))).thenReturn(name);
	}

	@Test
	public void testIsPropertySupportedWithNonPrimitiveCollection()
	{
		final TypeAttributeDescriptor descriptor = typeAttributeDescriptor(true, false);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(descriptor, "a"))).isFalse();
	}

	@Test
	public void testIsPropertySupportedWithPrimitiveCollection()
	{
		final TypeAttributeDescriptor descriptor = typeAttributeDescriptor(true, true);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(descriptor, "a"))).isFalse();
	}

	@Test
	public void testIsPropertySupportedWithPrimitiveValue()
	{
		final TypeAttributeDescriptor descriptor = typeAttributeDescriptor(false, true);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(descriptor, "a"))).isFalse();
	}

	@Test
	public void testIsPropertySupportedWithNonPrimitiveValue()
	{
		final TypeAttributeDescriptor descriptor = typeAttributeDescriptor(false, false);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(descriptor, "a"))).isTrue();
	}

	@Test
	public void testEntityWithNoProperties() throws EdmException
	{
		propertyProcessor.processItem(item, storageRequest);

		verifySetAttributeValueIsNotCalled();
	}

	@Test
	public void testEntityWithNoSupportedProperties() throws EdmException
	{
		givenProperty("a");
		when(entityType.getProperty("a")).thenReturn(null);
		givenProperty("b");
		when(entityType.getProperty("b").getMultiplicity()).thenReturn(EdmMultiplicity.MANY);
		givenProperty("c");
		when(entityType.getProperty("c").getType().getKind()).thenReturn(EdmTypeKind.COMPLEX);
		givenProperty(INTEGRATION_KEY_PROPERTY_NAME);
		givenIsPropertySupported(false);

		propertyProcessor.processItem(item, storageRequest);

		verifySetAttributeValueIsNotCalled();
	}

	@Test
	public void testMapPropertiesAreNotSupported() throws EdmException
	{
		givenProperty("a");
		givenAttributeDescriptorForTheProperty(mapAttribute());

		propertyProcessor.processItem(item, storageRequest);

		verifySetAttributeValueIsNotCalled();
	}

	@Test
	public void testEntityWithSupportedProperties_NoSettableProperties() throws EdmException
	{
		givenProperty("a");
		givenProperty("b");
		givenProperty("c");

		makeAttributesNonWritable("a", "b", "c");
		when(modelService.isNew(item)).thenReturn(false);

		propertyProcessor.processItem(item, storageRequest);

		verifySetAttributeValueIsNotCalled();
	}

	@Test
	public void testEntityWithSupportedProperties_IsPartOfTrue() throws EdmException
	{
		givenIsPropertySupported(true);
		final ItemModel relatedItem = processAnItemWithAttributeAnnotation(IS_PART_OF);

		verify(entityService).createOrUpdateItem(any(StorageRequest.class), any(CreateItemStrategy.class));
		verify(modelService).setAttributeValue(item, getName(relatedItem), relatedItem);
	}

	@Test
	public void testEntityWithSupportedPropertiesIsAutoCreateTrue() throws EdmException
	{
		givenIsPropertySupported(true);
		final ItemModel relatedItem = processAnItemWithAttributeAnnotation(IS_AUTO_CREATE);

		verify(entityService).createOrUpdateItem(any(StorageRequest.class), any(CreateItemStrategy.class));
		verify(modelService).setAttributeValue(item, getName(relatedItem), relatedItem);
	}

	@Test
	public void testEntityWithPropertyAutoCreateTrueNeverCallsSetOwner() throws EdmException
	{
		givenIsPropertySupported(true);
		processAnItemWithAttributeAnnotation(IS_AUTO_CREATE);

		verify(item, never()).setOwner(any());
	}

	@Test
	public void testInnerStorageRequestIsCorrectlyPopulated() throws EdmException
	{
		givenProperty("a");
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem("a", integrationItem("abc")))
				.build();
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, request);

		final StorageRequest capturedRequest = captureInnerStorageRequest();
		assertThat(capturedRequest)
				.hasFieldOrPropertyWithValue("integrationKey", "abc")
				.hasFieldOrPropertyWithValue("integrationItem.integrationKey", "abc");
	}

	private StorageRequest captureInnerStorageRequest() throws EdmException
	{
		final ArgumentCaptor<StorageRequest> requestCaptor = ArgumentCaptor.forClass(StorageRequest.class);
		verify(entityService).createOrUpdateItem(requestCaptor.capture(), any());
		return requestCaptor.getValue();
	}

	private ItemModel processAnItemWithAttributeAnnotation(final String annotation) throws EdmException
	{
		final String propertyName = "a";
		givenProperty(propertyName, annotation);
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(propertyName, integrationItem("123")))
				.build();

		final ItemModel relatedItem = mock(ItemModel.class);
		when(relatedItem.getProperty("name")).thenReturn(propertyName);
		when(modelService.isNew(relatedItem)).thenReturn(true);
		when(entityService.createOrUpdateItem(any(), any(CreateItemStrategy.class))).thenReturn(relatedItem);

		propertyProcessor.processItem(item, request);
		return relatedItem;
	}

	private String getName(final ItemModel item)
	{
		return item.getProperty("name");
	}

	@Test
	public void testEntityWithSupportedProperties_IsPartOfFalse() throws EdmException
	{
		final String propertyName = "a";
		givenProperty(propertyName);
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(propertyName, integrationItem("")))
				.build();

		when(modelService.isNew(item)).thenReturn(true);
		givenIsPropertySupported(true);

		doThrow(InvalidDataException.class).when(entityService)
				.createOrUpdateItem(any(), any(NeverCreateItemStrategy.class));

		assertThatThrownBy(() -> propertyProcessor.processItem(item, request))
				.isInstanceOf(InvalidDataException.class);
	}

	@Test
	public void testEntityWithSupportedProperties_WithSettableProperties_ExistingItem() throws EdmException
	{
		givenProperty("a");
		givenProperty("b");
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(ImmutableMap.of("a", integrationItem("1"), "b", integrationItem("2"))))
				.build();

		when(modelService.isNew(item)).thenReturn(true);

		final ItemModel existingItem = mock(ItemModel.class);
		when(entityService.createOrUpdateItem(any(), any())).thenReturn(existingItem);
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, request);

		verify(modelService).setAttributeValue(item, "a", existingItem);
		verify(modelService).setAttributeValue(item, "b", existingItem);
	}

	@Test
	public void testEntityWithSupportedProperties_WithSettableProperties_NewItem() throws EdmException
	{
		givenProperty("a");
		givenProperty("b");
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(ImmutableMap.of("a", integrationItem("1"), "b", integrationItem("2"))))
				.build();

		when(modelService.isNew(item)).thenReturn(false);

		final ItemModel existingItem = mock(ItemModel.class);
		when(entityService.createOrUpdateItem(any(), any())).thenReturn(existingItem);
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, request);

		verify(modelService).setAttributeValue(item, "a", existingItem);
		verify(modelService).setAttributeValue(item, "b", existingItem);
	}

	@Test
	public void testEntityWithSupportedProperties_NoWritableProperties() throws EdmException
	{
		givenProperty("a");
		givenProperty("b");
		makeAttributesNonWritable("a", "b");

		when(modelService.isNew(item)).thenReturn(false);

		propertyProcessor.processItem(item, storageRequest);

		verifySetAttributeValueIsNotCalled();
	}

	@Test
	public void testEntityWithSupportedProperties_WithSettableProperties_Exception() throws EdmException
	{
		givenProperty("a");
		givenProperty("b");
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(ImmutableMap.of("a", integrationItem("1"), "b", integrationItem("2"))))
				.build();

		when(modelService.isNew(item)).thenReturn(true);

		doThrow(EdmException.class).when(entityService).createOrUpdateItem(any(), any());
		givenIsPropertySupported(true);

		assertThatThrownBy(() -> propertyProcessor.processItem(item, request))
				.isInstanceOf(EdmException.class);

		verify(modelService, never()).setAttributeValue(eq(item), anyString(), any());
	}

	@Test
	public void testPartOfRelation_ToSetOwnerAsForeignKey() throws EdmException
	{
		givenIsPropertySupported(true);
		final ItemModel relatedItem = processAnItemWithAttributeAnnotation(IS_PART_OF);

		verify(entityService).createOrUpdateItem(any(StorageRequest.class), any(CreateItemStrategy.class));
		verify(relatedItem).setOwner(item);
	}

	@Test
	public void testPartOfRelation_NotToOverrideOwnerForExistingItem() throws EdmException
	{
		givenIsPropertySupported(true);
		givenProperty("a", IS_PART_OF);
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(ImmutableMap.of("a", integrationItem("1"))))
				.build();

		final ItemModel existingItem = mock(ItemModel.class);
		when(entityService.createOrUpdateItem(any(), any())).thenReturn(existingItem);
		when(modelService.isNew(existingItem)).thenReturn(false);

		propertyProcessor.processItem(item, request);

		verify(existingItem, times(0)).setOwner(item);
	}

	@Test
	public void testEnumerationValue() throws EdmException
	{
		final ItemModel enumerationValueModel = mock(EnumerationValueModel.class);
		final PK mockPK = PK.BIG_PK;

		when(enumerationValueModel.getPk()).thenReturn(mockPK);
		when(modelService.get(mockPK)).thenReturn(enumerationValueModel);

		final String propertyName = "a";
		givenProperty(propertyName, IS_PART_OF);
		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(propertyName, integrationItem("1")))
				.build();

		when(modelService.isNew(enumerationValueModel)).thenReturn(false);

		when(entityService.createOrUpdateItem(any(), any())).thenReturn(enumerationValueModel);
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, request);

		verify(modelService).get(mockPK);
		verify(modelService).setAttributeValue(item, propertyName, enumerationValueModel);
	}

	private void makeAttributesNonWritable(final String... attributeNames)
	{
		for(final String name : attributeNames)
		{
			final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor("itemModel", name);
			when(attributeDescriptor.getWritable()).thenReturn(false);
		}
	}

	@Test
	public void testAttributeDescriptorNotFoundExceptionIsRethrown_processItem() throws EdmException
	{
		givenProperty("a");
		when(modelService.isNew(item)).thenReturn(true);

		doThrow(AttributeDescriptorNotFoundException.class)
				.when(integrationObjectService).findItemAttributeName(anyString(), anyString(), anyString());

		assertThatThrownBy(() -> propertyProcessor.processItem(item, storageRequest))
				.isInstanceOf(AttributeDescriptorNotFoundException.class);
	}

	private void givenIsPropertySupported(final boolean propertySupported)
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(false, false);
		when(attributeDescriptor.isCollection()).thenReturn(!propertySupported);

		givenAttributeDescriptorForTheProperty(attributeDescriptor);
	}

	private void givenAttributeDescriptorForTheProperty(final TypeAttributeDescriptor attributeDescriptor)
	{
		final TypeDescriptor itemTypeDescriptor = mock(TypeDescriptor.class);
		when(itemTypeDescriptorService.getTypeDescriptorByTypeCode(any(String.class), any(String.class))).thenReturn(Optional.of(itemTypeDescriptor));
		doReturn(Optional.of(attributeDescriptor)).when(itemTypeDescriptor).getAttribute( any(String.class));
	}

	private void verifySetAttributeValueIsNotCalled()
	{
		verify(modelService, never()).setAttributeValue(eq(item), anyString(), anyMap());
		verify(modelService, never()).setAttributeValue(eq(item), anyString(), anyObject());
	}

	private IntegrationItem integrationItem(final String navProperty, final IntegrationItem nested)
	{
		return integrationItem(ImmutableMap.of(navProperty, nested));
	}

	private IntegrationItem integrationItem(final Map<String, IntegrationItem> properties)
	{
		final IntegrationItem item = integrationItem("");
		properties.forEach((prop, nested) -> {
			doReturn(nested).when(item).getAttribute(prop);
			doReturn(nested).when(item).getReferencedItem(prop);
		});
		return item;
	}

	private IntegrationItem integrationItem(final String key)
	{
		final IntegrationItem item = mock(IntegrationItem.class);
		doReturn(key).when(item).getIntegrationKey();
		return item;
	}

	private TypeAttributeDescriptor mapAttribute()
	{
		final TypeAttributeDescriptor attr = mock(TypeAttributeDescriptor.class);
		doReturn(true).when(attr).isMap();
		return attr;
	}
}
