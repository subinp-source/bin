/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessorTestUtils.propertyMetadata;
import static de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessorTestUtils.typeAttributeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor;
import de.hybris.platform.integrationservices.service.AttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidPropertyValueException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.olingo.odata2.api.edm.EdmAnnotations;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmType;
import org.apache.olingo.odata2.api.edm.EdmTypeKind;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;

/**
 * @deprecated With using {@link AttributePopulator}s for persistence, {@code processItem()} is deprecated.
 * For process entity unit tests
 * @see PrimitivePropertyProcessorUnitTest
 */
@Deprecated(since = "1905.07-CEP", forRemoval = true)
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PrimitivePropertyProcessorDeprecatedUnitTest
{
	private static final String INTEGRATION_OBJECT_CODE = "IntegrationObjectType";

	@Mock
	private ModelService modelService;
	@Mock
	private StorageRequest storageRequest;
	@Mock
	private ItemConversionRequest conversionRequest;
	@Mock
	private IntegrationObjectService integrationObjectService;
	@Mock
	private TypeService typeService;
	@Mock
	private ItemTypeDescriptorService itemTypeDescriptorService;
	@Mock
	private EdmEntitySet entitySet;
	@Mock
	private EdmEntityType entityType;
	@Mock
	private ODataEntry oDataEntry;

	@InjectMocks
	@Spy
	private PrimitivePropertyProcessor propertyProcessor;

	private final ItemModel item = mock(ItemModel.class);
	private final Locale locale = Locale.ENGLISH;

	private Map<String, Object> properties;

	@Before
	public void setUp() throws EdmException
	{
		properties = Maps.newHashMap();
		when(storageRequest.getEntitySet()).thenReturn(entitySet);
		when(storageRequest.getEntityType()).thenReturn(entityType);
		when(storageRequest.getContentLocale()).thenReturn(locale);
		when(storageRequest.getODataEntry()).thenReturn(oDataEntry);
		when(storageRequest.getIntegrationObjectCode()).thenReturn(INTEGRATION_OBJECT_CODE);

		when(conversionRequest.getEntitySet()).thenReturn(entitySet);
		when(conversionRequest.getEntityType()).thenReturn(entityType);
		when(conversionRequest.getAcceptLocale()).thenReturn(locale);
		when(conversionRequest.getValue()).thenReturn(item);
		when(conversionRequest.getIntegrationObjectCode()).thenReturn(INTEGRATION_OBJECT_CODE);

		when(oDataEntry.getProperties()).thenReturn(properties);
		when(item.getItemtype()).thenReturn("MyType");
		when(entityType.getName()).thenReturn("entityName");

		propertyProcessor.setIntegrationObjectService(integrationObjectService);
	}

	@Test
	public void testIsPropertySupportedWithPrimitiveCollection()
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(true, true);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(attributeDescriptor, "a"))).isFalse();
	}

	@Test
	public void testIsPropertySupportedWithNonPrimitiveCollection()
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(true, false);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(attributeDescriptor, "a"))).isFalse();
	}

	@Test
	public void testIsPropertySupportedWithPrimitiveNotCollection()
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(false, true);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(attributeDescriptor, "a"))).isTrue();
	}

	@Test
	public void testIsPropertySupportedWithNotPrimitiveNotCollection()
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(false, false);

		assertThat(propertyProcessor.isPropertySupported(propertyMetadata(attributeDescriptor, "a"))).isFalse();
	}

	@Test
	public void testItemWithNoProperties() throws EdmException
	{
		propertyProcessor.processItem(item, storageRequest);

		verifyItemSetAttributeValueIsNotCalled(item);
	}

	@Test
	public void testProcessItemWithNoSupportedProperties() throws EdmException
	{
		givenProperty("a", null);
		when(entityType.getProperty("a")).thenReturn(null);
		givenProperty("b", null);
		when(entityType.getProperty("b").getType().getKind()).thenReturn(EdmTypeKind.ENTITY);
		givenIsPropertySupported(false);

		propertyProcessor.processItem(item, storageRequest);

		verifyItemSetAttributeValueIsNotCalled(item);
	}

	@Test
	public void testProcessItemWithSupportedPropertiesNoSettableProperties() throws EdmException
	{
		givenProperty("a", null);
		givenProperty("b", null);

		when(modelService.isNew(item)).thenReturn(false);
		final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor("TypeA", "a");
		when(attributeDescriptor.getWritable()).thenReturn(false);

		final AttributeDescriptorModel attributeDescriptorB = typeService.getAttributeDescriptor("TypeB", "b");
		when(attributeDescriptorB.getWritable()).thenReturn(false);

		propertyProcessor.processItem(item, storageRequest);

		verifyItemSetAttributeValueIsNotCalled(item);
	}

	@Test
	public void testProcessItemWithSupportedPropertiesWithSettableProperties() throws EdmException
	{
		final Calendar calendar = GregorianCalendar.getInstance();
		givenProperty("a", calendar);
		givenProperty("b", "some localizable string");
		givenAttributeDescriptorExists(attributeDescriptor("b", true));
		givenProperty("c", "some value");
		givenAttributeDescriptorExists(attributeDescriptor("c", false));
		givenIsPropertySupported(true);

		when(modelService.isNew(item)).thenReturn(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService).setAttributeValue(item, "a", calendar.getTime());
		verify(modelService).setAttributeValue(item, "b", Collections.singletonMap(locale, "some localizable string"));
		verify(modelService).setAttributeValue(item, "c", "some value");
	}

	@Test
	public void testProcessItemWithKeyPropertyForExistingItemNotSetAgainstItemModel() throws EdmException
	{
		givenAttributeDescriptorExists(attributeDescriptor("a", false));
		when(modelService.isNew(item)).thenReturn(false);
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verifyItemSetAttributeValueIsNotCalled(item);
	}

	@Test
	public void testProcessItemWithKeyPropertyForNewItemSetAgainstItemModel() throws EdmException
	{
		givenProperty("a", "some localizable string");
		when(modelService.isNew(item)).thenReturn(true);
		when(entityType.getKeyPropertyNames()).thenReturn(Collections.singletonList("a"));
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService, times(1)).setAttributeValue(eq(item), anyString(), anyString());
	}

	@Test
	public void testProcessItemWithNonKeyPropertyForExistingItemSetAgainstItemModel() throws EdmException
	{
		givenProperty("a", "some localizable string");

		when(modelService.isNew(item)).thenReturn(true);
		when(entityType.getKeyPropertyNames()).thenReturn(Collections.singletonList("b"));
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService, times(1)).setAttributeValue(eq(item), anyString(), anyString());
	}

	@Test
	public void testProcessItemWithIntegrationKeyPresent() throws EdmException
	{
		givenProperty("integrationKey", "abc-123");
		when(modelService.isNew(item)).thenReturn(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService, never()).setAttributeValue(eq(item), eq(INTEGRATION_KEY_PROPERTY_NAME), any());
	}

	@Test
	public void testProcessesCharValueForCharProperty() throws EdmException
	{
		givenProperty("charAttribute", Character.class, '#');
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService).setAttributeValue(item, "charAttribute", '#');
	}

	@Test
	public void testProcessesOneCharacterLongStringValueForCharProperty() throws EdmException
	{
		givenProperty("charAttribute", Character.class, "!");
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService).setAttributeValue(item, "charAttribute", '!');
	}

	@Test
	public void testProcessMoreThanOneCharacterLongStringValueForCharPropertyThrowsException() throws EdmException
	{
		givenProperty("charAttribute", Character.class, "Oops");
		givenIsPropertySupported(true);

		assertThatThrownBy(() -> propertyProcessor.processItem(item, storageRequest))
				.isInstanceOf(InvalidPropertyValueException.class)
				.hasMessageContaining("charAttribute")
				.hasMessageContaining("Oops");
	}

	@Test
	public void testProcessesNullValueForCharProperty() throws EdmException
	{
		givenProperty("charAttribute", Character.class, null);
		givenIsPropertySupported(true);

		propertyProcessor.processItem(item, storageRequest);

		verify(modelService).setAttributeValue(same(item), eq("charAttribute"), isNull());
	}

	@Test
	public void testProcessesEmptyValueForCharPropertyThrowsException() throws EdmException
	{
		givenProperty("charAttribute", Character.class, "");
		givenIsPropertySupported(true);

		assertThatThrownBy(() -> propertyProcessor.processItem(item, storageRequest))
				.isInstanceOf(InvalidPropertyValueException.class)
				.hasMessageContaining("charAttribute");
	}

	@Test
	public void testProcessItemAttributeDescriptorNotFoundExceptionIsRethrown() throws EdmException
	{
		givenProperty("c", "abc-123");

		doThrow(AttributeDescriptorNotFoundException.class)
				.when(integrationObjectService).findItemAttributeName(anyString(), anyString(), anyString());

		assertThatThrownBy(() -> propertyProcessor.processItem(item, storageRequest))
				.isInstanceOf(AttributeDescriptorNotFoundException.class);
	}

	private void givenProperty(final String name, final Object val) throws EdmException
	{
		givenProperty(name, String.class, val);
	}

	private void givenProperty(final String name, final Class<?> type, final Object val) throws EdmException
	{
		properties.put(name, val);

		final EdmAnnotations annotations = mock(EdmAnnotations.class);
		when(annotations.getAnnotationAttributes()).thenReturn(Collections.emptyList());

		final EdmProperty property = mock(EdmProperty.class);
		when(property.getType()).thenReturn(mock(EdmType.class));
		when(property.getAnnotations()).thenReturn(annotations);

		when(entityType.getProperty(name)).thenReturn(property);

		givenAttributeDescriptorExists(attributeDescriptor(name, type));
	}

	private void givenAttributeDescriptorExists(final AttributeDescriptorModel attribute)
	{
		final String attrName = attribute.getQualifier();
		when(integrationObjectService.findItemAttributeName(any(), any(), eq(attrName))).thenReturn(attrName);
		when(typeService.getAttributeDescriptor(anyString(), eq(attrName))).thenReturn(attribute);
	}

	private AttributeDescriptorModel attributeDescriptor(final String name, final boolean localizable)
	{
		return attributeDescriptor(name, "java.lang.String", localizable);
	}

	private AttributeDescriptorModel attributeDescriptor(final String name, final Class<?> type)
	{
		return attributeDescriptor(name, type.getName(), false);
	}

	private AttributeDescriptorModel attributeDescriptor(final String name, final String type, final boolean localizable)
	{
		final TypeModel typeModel = mock(TypeModel.class, "TypeModel:" + type);
		doReturn(type).when(typeModel).getCode();

		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		when(attributeDescriptor.getLocalized()).thenReturn(localizable);
		when(attributeDescriptor.getQualifier()).thenReturn(name);
		when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
		when(attributeDescriptor.getWritable()).thenReturn(true);
		return attributeDescriptor;
	}

	private void givenIsPropertySupported(final boolean propertySupported)
	{
		final TypeAttributeDescriptor attributeDescriptor = typeAttributeDescriptor(!propertySupported, propertySupported);
		
		final TypeDescriptor itemTypeDescriptor = mock(ItemTypeDescriptor.class);
		when(itemTypeDescriptorService.getTypeDescriptorByTypeCode(any(String.class), any(String.class))).thenReturn(Optional.of(itemTypeDescriptor));
		doReturn(Optional.of(attributeDescriptor)).when(itemTypeDescriptor).getAttribute( any(String.class));
	}

	private void verifyItemSetAttributeValueIsNotCalled(final ItemModel itemModel)
	{
		verify(modelService, never()).setAttributeValue(eq(itemModel), anyString(), any());
	}
}
