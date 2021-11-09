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
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessorTestUtils.typeAttributeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor;
import de.hybris.platform.integrationservices.service.AttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingNavigationPropertyException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collection;
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
import org.apache.olingo.odata2.api.edm.EdmType;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.core.edm.provider.EdmNavigationPropertyImplProv;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * @deprecated still tests deprecated methods, but this test file should be deleted together with the deprecated
 * methods.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
@Deprecated(since = "1905.07-CEP", forRemoval = true)
public class EntityCollectionPropertyProcessorDeprecatedUnitTest
{
	private static final Locale LOCALE = Locale.ENGLISH;
	private static final String IS_PART_OF = "s:IsPartOf";
	private static final String IS_AUTO_CREATE = "s:IsAutoCreate";
	private static final ItemModel ITEM = mock(ItemModel.class);
	private static final String INTEGRATION_OBJECT_CODE = "IntegrationObjectType";
	private static final String ITEM_TYPE = "TypeA";
	private static final String TEST_ATTRIBUTE = "attributeName";

	private Map<String, Object> entryProperties;

	@Mock
	private ModelService modelService;
	@Mock
	private ModelEntityService modelEntityService;
	@Mock
	private IntegrationObjectService integrationObjectService;
	@Mock
	private TypeService typeService;
	@Mock
	private ItemTypeDescriptorService itemTypeDescriptorService;
	@Mock
	private EdmNavigationPropertyImplProv edmTyped;
	@Mock
	private EdmEntitySet entitySet;
	@Mock
	private EdmEntityType entityType;
	@Mock
	private ODataEntry oDataEntry;
	@Mock
	private AttributeDescriptorModel attributeDescriptor;
	@Spy
	@InjectMocks
	private EntityCollectionPropertyProcessor collectionProcessor;
	@Captor
	private ArgumentCaptor<Collection> collectionCaptor;

	@Before
	public void setUp() throws EdmException
	{
		entryProperties = Maps.newHashMap();
		when(entitySet.getEntityType()).thenReturn(entityType);
		when(oDataEntry.getProperties()).thenReturn(entryProperties);
		when(typeService.getAttributeDescriptor(anyString(), anyString())).thenReturn(attributeDescriptor);
	}

	@Test
	public void testItemWithNoProperties() throws EdmException
	{
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelService, never()).setAttributeValue(eq(ITEM), anyString(), any());
		verify(modelEntityService, never()).createOrUpdateItem(any(), any());
	}

	@Test
	public void testEntityNoSettableProperties() throws EdmException
	{
		givenProperty(TEST_ATTRIBUTE);
		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		when(attributeDescriptor.getWritable()).thenReturn(false);

		when(modelService.isNew(ITEM)).thenReturn(false);

		assertThat(collectionProcessor.isItemPropertySettable(ITEM, TEST_ATTRIBUTE, storageRequest())).isEqualTo(false);
	}

	@Test
	public void testEntitySettableProperties() throws EdmException
	{
		givenProperty(TEST_ATTRIBUTE);
		propertyOfType(TypeOfCollectionEnum.COLLECTION);

		when(modelService.isNew(ITEM)).thenReturn(true);

		assertThat(collectionProcessor.isItemPropertySettable(ITEM, TEST_ATTRIBUTE, storageRequest())).isEqualTo(true);
	}

	@Test
	public void testProcessItemWithIntegrationKey() throws EdmException
	{
		givenProperty(INTEGRATION_KEY_PROPERTY_NAME, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, never()).createOrUpdateItem(any(), any());
		verify(modelService, never()).setAttributeValue(any(), anyString(), any());
	}

	@Test
	public void testProcessSuccessWithMergeOfCollectionEntries() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(3);
	}

	@Test
	public void testAddNewItemToCollectionWhenAutoCreateIsTrue() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_AUTO_CREATE);
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(any())).thenReturn(true);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(3);
	}

	@Test
	public void testProcessSuccessForTwoPartOfCollectionEntries() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(2);
	}

	@Test
	public void testProcessSuccessForTwoPartOfCollectionEntriesWithSet() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);

		propertyOfType(TypeOfCollectionEnum.SET);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		when(modelEntityService.createOrUpdateItem(any(), any())).thenReturn(mock(ItemModel.class));
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(1);  // it's a set.
	}

	@Test
	public void testProcessSuccessForTwoCollectionEntries() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations("SOME_OTHER_ANNOTATION");
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(2);
	}

	@Test
	public void testProcessSuccessForPropertyOfMapType() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);

		propertyOfMapType();
		prepareAnnotations("SOME_OTHER_ANNOTATION");
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(2);
	}

	@Test
	public void testProcessSuccessForTwoCollectionEntriesWhenExistingItemOnAttributeIsNotDuplicatedInCollection() throws EdmException
	{
		givenIsPropertySupported();
		final ItemModel existingItem = mock(ItemModel.class);
		givenProperty(TEST_ATTRIBUTE, existingItem);

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations("SOME_OTHER_ANNOTATION");
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(ITEM)).thenReturn(false);
		when(modelEntityService.createOrUpdateItem(any(), any())).thenReturn(existingItem, mock(ItemModel.class));
		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelEntityService, times(2)).createOrUpdateItem(any(), any());
		verify(modelService).setAttributeValue(eq(ITEM), anyString(), collectionCaptor.capture());
		assertThat(collectionCaptor.getValue()).hasSize(2);
	}

	@Test
	public void testExceptionWhileProcess() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);
		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareGetEntitySetReferencedByProperty();
		doThrow(EdmException.class).when(entityType).getProperty(any());

		assertThatThrownBy(() -> collectionProcessor.processItem(ITEM, storageRequest()))
				.isInstanceOf(EdmException.class);
	}

	@Test
	public void testPartOfRelationToSetOwnerAsForeignKey() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();

		final ItemModel modelToCreate = mock(ItemModel.class);
		when(modelEntityService.createOrUpdateItem(any(), any())).thenReturn(modelToCreate);
		when(modelService.isNew(modelToCreate)).thenReturn(true);

		collectionProcessor.processItem(ITEM, storageRequest());

		// it called twice because it has two collection entries.
		verify(modelToCreate, times(2)).setOwner(eq(ITEM));
	}

	@Test
	public void testAutoCreateAttributeDoesNotSetOwner() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations(IS_AUTO_CREATE);
		prepareGetEntitySetReferencedByProperty();

		final ItemModel modelToCreate = mock(ItemModel.class);
		when(modelEntityService.createOrUpdateItem(any(), any())).thenReturn(modelToCreate);
		when(modelService.isNew(modelToCreate)).thenReturn(true);

		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelToCreate, never()).setOwner(eq(ITEM));
	}

	@Test
	public void testInnerStorageRequestsCorrectlyBuilt() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE);
		prepareAnnotations(IS_PART_OF);
		prepareGetEntitySetReferencedByProperty();
		givenODataEntryContainsNestedEntries(TEST_ATTRIBUTE, oDataEntry("abc"), oDataEntry("123"));

		final StorageRequest request = storageRequestBuilder()
				.withIntegrationItem(integrationItem(TEST_ATTRIBUTE, integrationItem("123"), integrationItem("abc")))
				.build();

		collectionProcessor.processItem(ITEM, request);

		final List<StorageRequest> capturedRequests = captureRequestsToCreateOrUpdateItem();
		assertThat(capturedRequests).hasSize(2);
		assertRequestForIntegrationKey(capturedRequests, "123");
		assertRequestForIntegrationKey(capturedRequests, "abc");
	}

	private void assertRequestForIntegrationKey(final List<StorageRequest> capturedRequests, final String key)
	{
		final StorageRequest request = capturedRequests.stream()
				.filter(r -> key.equals(r.getIntegrationKey()))
				.findAny()
				.orElse(null);
		assertThat(request)
				.isNotNull()
				.hasFieldOrPropertyWithValue("integrationItem.integrationKey", key);
		assertThat(request.getODataEntry().getProperties())
				.containsEntry(INTEGRATION_KEY_PROPERTY_NAME, key);
	}

	private List<StorageRequest> captureRequestsToCreateOrUpdateItem() throws EdmException
	{
		final ArgumentCaptor<StorageRequest> requestCaptor = ArgumentCaptor.forClass(StorageRequest.class);
		verify(modelEntityService, atLeast(0)).createOrUpdateItem(requestCaptor.capture(), any());
		return requestCaptor.getAllValues();
	}

	@Test
	public void testPartOfRelationNotToOverrideOwnerForExistingItem() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));

		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations("FOOBAR");
		prepareGetEntitySetReferencedByProperty();

		final ItemModel modelToCreate = mock(ItemModel.class);
		when(modelEntityService.createOrUpdateItem(any(), any())).thenReturn(modelToCreate);
		when(modelService.isNew(modelToCreate)).thenReturn(false);

		collectionProcessor.processItem(ITEM, storageRequest());

		verify(modelToCreate, times(0)).setOwner(eq(ITEM));
	}

	@Test
	public void testNotPartOfAndNewCollectionItemThrowsException() throws EdmException
	{
		givenIsPropertySupported();
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class));
		propertyOfType(TypeOfCollectionEnum.COLLECTION);
		prepareAnnotations("BARFOO");
		prepareGetEntitySetReferencedByProperty();

		when(modelService.isNew(any())).thenReturn(true);

		assertThatThrownBy(() -> collectionProcessor.processItem(ITEM, storageRequest()))
				.isInstanceOf(MissingNavigationPropertyException.class);
	}

	@Test
	public void testProcessItemRethrowsAttributeDescriptorNotFoundException() throws EdmException
	{
		givenProperty(TEST_ATTRIBUTE, mock(ItemModel.class), mock(ItemModel.class));
		propertyOfType(TypeOfCollectionEnum.SET);
		prepareGetEntitySetReferencedByProperty();

		doThrow(AttributeDescriptorNotFoundException.class)
				.when(integrationObjectService).findItemAttributeName(anyString(), anyString(), anyString());

		assertThatThrownBy(() -> collectionProcessor.processItem(ITEM, storageRequest()))
				.isInstanceOf(AttributeDescriptorNotFoundException.class);
	}

	private void givenIsPropertySupported()
	{
		final TypeAttributeDescriptor typeAttributeDescriptor = typeAttributeDescriptor(true,false);
		final TypeDescriptor itemTypeDescriptor = mock(ItemTypeDescriptor.class);
		when(itemTypeDescriptorService.getTypeDescriptorByTypeCode(any(String.class), any(String.class))).thenReturn(Optional.of(itemTypeDescriptor));
		doReturn(Optional.of(typeAttributeDescriptor)).when(itemTypeDescriptor).getAttribute( any(String.class));
	}

	private void givenProperty(final String name, final Object... values) throws EdmException
	{
		givenODataEntryContainsNestedEntries(name, oDataEntry("1"), oDataEntry("2"));

		final EdmType edmType = mock(EdmType.class);
		when(edmType.getName()).thenReturn(ITEM_TYPE);
		when(edmTyped.getType()).thenReturn(edmType);
		when(entityType.getProperty(name)).thenReturn(edmTyped);

		when(modelService.getAttributeValue(ITEM, name)).thenReturn(Lists.newArrayList(values));

		when(attributeDescriptor.getWritable()).thenReturn(true);
		when(attributeDescriptor.getQualifier()).thenReturn(name);
	}

	private void givenODataEntryContainsNestedEntries(final String property, final ODataEntry... entries)
	{
		final ODataFeed feed = mock(ODataFeed.class);
		doReturn(Arrays.asList(entries)).when(feed).getEntries();
		entryProperties.put(property, feed);
	}

	private ODataEntry oDataEntry(final String key)
	{
		final Map<String, Object> properties = ImmutableMap.of(INTEGRATION_KEY_PROPERTY_NAME, key);
		final ODataEntry entry = mock(ODataEntry.class, key);
		doReturn(properties).when(entry).getProperties();
		return entry;
	}

	private void prepareAnnotations(final String... annotations) throws EdmException
	{
		final EdmAnnotations edmAnnotations = mock(EdmAnnotations.class);

		final List<EdmAnnotationAttribute> attributes = Arrays.stream(annotations).map(a -> {
			final EdmAnnotationAttribute annotationAttribute = mock(EdmAnnotationAttribute.class);
			when(annotationAttribute.getText()).thenReturn("true");
			when(annotationAttribute.getName()).thenReturn(a);
			return annotationAttribute;
		}).collect(Collectors.toList());

		when(edmAnnotations.getAnnotationAttributes()).thenReturn((attributes));
		when((edmTyped).getAnnotations()).thenReturn(edmAnnotations);
	}

	private void propertyOfType(final TypeOfCollectionEnum collectionType)
	{
		final CollectionTypeModel collectionModel = mock(CollectionTypeModel.class);
		when(attributeDescriptor.getAttributeType()).thenReturn(collectionModel);
		when(collectionModel.getTypeOfCollection()).thenReturn(collectionType);
		doReturn(attributeDescriptor).when(typeService).getAttributeDescriptor(anyString(), anyString());
	}

	private void propertyOfMapType()
	{
		final MapTypeModel collectionModel = mock(MapTypeModel.class);
		when(attributeDescriptor.getAttributeType()).thenReturn(collectionModel);
		doReturn(attributeDescriptor).when(typeService).getAttributeDescriptor(anyString(), anyString());
	}

	private void prepareGetEntitySetReferencedByProperty() throws EdmException
	{
		final EdmEntityContainer entityContainer = mock(EdmEntityContainer.class);
		when(entitySet.getEntityContainer()).thenReturn(entityContainer);
		when(entityContainer.getEntitySet(anyString())).thenReturn(entitySet);
	}

	private StorageRequest storageRequest() throws EdmException
	{
		return storageRequestBuilder().build();
	}

	private StorageRequest.StorageRequestBuilder storageRequestBuilder()
	{
		return StorageRequest.storageRequestBuilder()
				.withContentLocale(LOCALE)
				.withAcceptLocale(LOCALE)
				.withODataEntry(oDataEntry)
				.withIntegrationObject(INTEGRATION_OBJECT_CODE)
				.withIntegrationItem(integrationItem(TEST_ATTRIBUTE, integrationItem("1"), integrationItem("2")))
				.withEntitySet(entitySet);
	}

	private IntegrationItem integrationItem(final String navProperty, final IntegrationItem... items)
	{
		final Collection<IntegrationItem> nestedItems = Arrays.asList(items);
		final IntegrationItem item = mock(IntegrationItem.class);
		doReturn(nestedItems).when(item).getAttribute(navProperty);
		doReturn(nestedItems).when(item).getReferencedItems(navProperty);
		return item;
	}

	private IntegrationItem integrationItem(final String key)
	{
		final IntegrationItem item = mock(IntegrationItem.class);
		doReturn(key).when(item).getIntegrationKey();
		return item;
	}
}
