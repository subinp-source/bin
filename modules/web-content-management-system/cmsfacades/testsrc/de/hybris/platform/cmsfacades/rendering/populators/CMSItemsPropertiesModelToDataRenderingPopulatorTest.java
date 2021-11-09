/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static de.hybris.platform.cmsfacades.rendering.populators.CMSItemsPropertiesModelToDataRenderingPopulator.PROPERTIES_ATTRIBUTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.common.service.impl.DefaultCollectionHelper;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplier;
import de.hybris.platform.cmsfacades.cmsitems.properties.CMSItemPropertiesSupplierProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSItemsPropertiesModelToDataRenderingPopulatorTest
{
	private final String SUPPLIER1_GROUP_NAME = "groupName1";
	private final String SUPPLIER1_PROP_PRIMITIVE_KEY1 = "supplier1_key1";
	private final String SUPPLIER1_PROP_LIST_KEY2 = "supplier1_key2";
	private final String SUPPLIER1_PROP_PRIMITIVE_VALUE1 = "supplier1_value1";
	private final String SUPPLIER1_PROP_PRIMITIVE_VALUE2 = "supplier1_value2";
	private Map<String, Object> supplier1Properties = null;

	private final String SUPPLIER2_GROUP_NAME = "groupName2";
	private final String SUPPLIER2_PROP_PRIMITIVE_KEY1 = "supplier2_key1";
	private final String SUPPLIER2_PROP_LIST_KEY2 = "supplier2_key2";
	private final String SUPPLIER2_PROP_PRIMITIVE_VALUE1 = "supplier2_value1";
	private final String SUPPLIER2_PROP_PRIMITIVE_VALUE2 = "supplier2_value2";
	private Map<String, Object> supplier2properties = null;


	@InjectMocks
	private CMSItemsPropertiesModelToDataRenderingPopulator populator;

	@Mock
	private CMSItemPropertiesSupplierProvider cmsItemTagPropertiesSupplierProvider;

	@InjectMocks
	private DefaultCollectionHelper collectionHelper;

	@Mock
	private CMSItemModel itemModel;
	@Mock
	private CMSItemPropertiesSupplier supplier1;
	@Mock
	private CMSItemPropertiesSupplier supplier2;

	@Before
	public void setUp()
	{
		populator.setCollectionHelper(collectionHelper);

		when(cmsItemTagPropertiesSupplierProvider.getSuppliers(itemModel)).thenReturn(Arrays.asList(supplier1, supplier2));

		// supplier1
		supplier1Properties = new HashMap<>();
		supplier1Properties.put(SUPPLIER1_PROP_PRIMITIVE_KEY1, SUPPLIER1_PROP_PRIMITIVE_VALUE1);
		supplier1Properties.put(SUPPLIER1_PROP_LIST_KEY2, Arrays.asList(SUPPLIER1_PROP_PRIMITIVE_VALUE2));
		when(supplier1.getProperties(itemModel)).thenReturn(supplier1Properties);
		when(supplier1.isEnabled(itemModel)).thenReturn(Boolean.TRUE);
		when(supplier1.groupName()).thenReturn(SUPPLIER1_GROUP_NAME);

		// supplier2
		supplier2properties = new HashMap<>();
		supplier2properties.put(SUPPLIER2_PROP_PRIMITIVE_KEY1, SUPPLIER2_PROP_PRIMITIVE_VALUE1);
		supplier2properties.put(SUPPLIER2_PROP_LIST_KEY2, Arrays.asList(SUPPLIER2_PROP_PRIMITIVE_VALUE2));

		when(supplier2.getProperties(itemModel)).thenReturn(supplier2properties);
		when(supplier2.isEnabled(itemModel)).thenReturn(Boolean.TRUE);
		when(supplier2.groupName()).thenReturn(SUPPLIER2_GROUP_NAME);
	}

	@Test
	public void shouldUseOnlyEnabledSuppliers()
	{
		// GIVEN
		when(supplier2.isEnabled(itemModel)).thenReturn(Boolean.FALSE);
		final Map<String, Object> resultData = new HashMap<>();

		// WHEN
		populator.populate(itemModel, resultData);

		// THEN
		verify(supplier1, atLeastOnce()).getProperties(itemModel);
		verify(supplier2, never()).getProperties(itemModel);
	}

	@Test
	public void shouldNotGroupPropertiesFromDifferentSuppliersUnderDifferentGroupNames()
	{
		// GIVEN
		final Map<String, Object> resultData = new HashMap<>();

		// WHEN
		populator.populate(itemModel, resultData);

		// THEN
		final Map<String, Object> propertiesObject = getProperties(resultData);
		assertThat(propertiesObject.entrySet(), hasSize(2));
	}

	@Test
	public void shouldGroupPropertiesFromDifferentSuppliersUnderSameGroup()
	{
		// GIVEN
		final Map<String, Object> resultData = new HashMap<>();
		when(supplier2.groupName()).thenReturn(SUPPLIER1_GROUP_NAME);

		// WHEN
		populator.populate(itemModel, resultData);

		// THEN
		final Map<String, Object> propertiesObject = getProperties(resultData);
		assertThat(propertiesObject.entrySet(), hasSize(1));
	}

	@Test
	public void shouldOverridePrimitivePropertyWithSameNameInSameGroup()
	{
		// GIVEN
		final Map<String, Object> resultData = new HashMap<>();
		final String OVERRIDE_VALUE = "Override";
		when(supplier2.groupName()).thenReturn(SUPPLIER1_GROUP_NAME);
		supplier2properties.put(SUPPLIER1_PROP_PRIMITIVE_KEY1, OVERRIDE_VALUE);

		// WHEN
		populator.populate(itemModel, resultData);

		// THEN
		final Map<String, Object> groupPropertiesObject = getPropertiesByGroup(resultData, SUPPLIER1_GROUP_NAME);
		assertThat(groupPropertiesObject.get(SUPPLIER1_PROP_PRIMITIVE_KEY1), is(OVERRIDE_VALUE));
	}

	@Test
	public void shouldConcatenateListPropertyWithSameNameInSameGroup()
	{
		// GIVEN
		final Map<String, Object> resultData = new HashMap<>();
		when(supplier2.groupName()).thenReturn(SUPPLIER1_GROUP_NAME);
		supplier2properties.put(SUPPLIER1_PROP_LIST_KEY2, Arrays.asList(SUPPLIER2_PROP_PRIMITIVE_VALUE2));

		// WHEN
		populator.populate(itemModel, resultData);

		// THEN
		final Map<String, Object> groupPropertiesObject = getPropertiesByGroup(resultData, SUPPLIER1_GROUP_NAME);
		assertThat((Collection<?>) groupPropertiesObject.get(SUPPLIER1_PROP_LIST_KEY2), hasSize(2));
		assertThat((Collection<?>) groupPropertiesObject.get(SUPPLIER1_PROP_LIST_KEY2),
				contains(SUPPLIER1_PROP_PRIMITIVE_VALUE2, SUPPLIER2_PROP_PRIMITIVE_VALUE2));
	}

	protected Map<String, Object> getProperties(final Map<String, Object> resultData)
	{
		return (Map<String, Object>) resultData.get(PROPERTIES_ATTRIBUTE);
	}

	protected Map<String, Object> getPropertiesByGroup(final Map<String, Object> resultData, final String groupName)
	{
		final Map<String, Object> properties = getProperties(resultData);
		return (Map<String, Object>) properties.get(groupName);
	}
}
