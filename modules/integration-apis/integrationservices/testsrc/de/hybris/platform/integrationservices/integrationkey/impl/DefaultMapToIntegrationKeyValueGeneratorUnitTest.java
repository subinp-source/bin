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
package de.hybris.platform.integrationservices.integrationkey.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.integrationkey.KeyValue;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.KeyAttribute;
import de.hybris.platform.integrationservices.model.KeyDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DefaultMapToIntegrationKeyValueGeneratorUnitTest
{
	private static final String PRODUCT_ENTITY_NAME = "Product";
	private static final String PRODUCT_CODE_PROPERTY = "code";
	private static final String PRODUCT_CODE_VALUE = "product|code|value";

	private static final String CATALOGVERSION_ENTITY_NAME = "CatalogVersion";
	private static final String CATALOGVERSION_VERSION_PROPERTY = "version";

	private static final TypeDescriptor ITEM_TYPE = mock(TypeDescriptor.class);
	private static final Map<String, Object> ENTRY = new HashMap<>();

	private final DefaultMapToIntegrationKeyValueGenerator integrationKeyGenerator = new DefaultMapToIntegrationKeyValueGenerator();

	@Before
	public void setUp()
	{
		integrationKeyGenerator.setEncoding("UTF-8");
	}

	@Test
	public void testCalculateWhenTypeDescriptorIsNull()
	{
		assertThat(integrationKeyGenerator.generate(null, ENTRY))
				.isEqualTo(StringUtils.EMPTY);
	}

	@Test
	public void testCalculateWhenMapIsNull()
	{
		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, null))
				.isEqualTo(StringUtils.EMPTY);
	}

	@Test
	public void testCalculateIntegrationKeyValueKeyWhenGivenEntityWithSimpleKey()
	{
		final KeyAttribute attribute = keyAttribute(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY);
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder().withValue(attribute, PRODUCT_CODE_VALUE));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo(encode(PRODUCT_CODE_VALUE));
	}

	@Test
	public void testCalculateIntegrationKeyWhenGivenEntityWithSimpleKey_date()
	{
		final Date now = new Date();
		final KeyAttribute attribute = keyAttribute(PRODUCT_ENTITY_NAME, "date");
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder().withValue(attribute, "/Date(" + now.getTime() + ")/"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo(String.valueOf(now.getTime()));
	}

	@Test
	public void testCalculateIntegrationKeyWhenGivenEntityWithSimpleKey_Calendar()
	{
		final GregorianCalendar now = new GregorianCalendar();
		final KeyAttribute attribute = keyAttribute(PRODUCT_ENTITY_NAME, "creationtime");
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder().withValue(attribute, now));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo(String.valueOf(now.getTimeInMillis()));
	}

	@Test
	public void testCalculateIntegrationKeyWhenGivenEntityWithSimpleKey_encodeProblem()
	{
		integrationKeyGenerator.setEncoding("SOME_WEIRD_ENCODING");

		final KeyAttribute attribute = keyAttribute(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY);
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder().withValue(attribute, PRODUCT_CODE_VALUE));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo(PRODUCT_CODE_VALUE);
	}

	@Test
	public void testCalculateIntegrationKeyWhenGivenEntityWithSimpleKeyAndNavigationKey()
	{
		final String catalogCodeValue = "the|catalog|Value";
		final String productCodeValue = "some|product|code";
		final KeyAttribute productCodeAttr = keyAttribute(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY);
		final KeyAttribute catalogCodeAttr = keyAttribute(CATALOGVERSION_ENTITY_NAME, CATALOGVERSION_VERSION_PROPERTY);
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(productCodeAttr, productCodeValue)
				.withValue(catalogCodeAttr, catalogCodeValue));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo(encode(catalogCodeValue) + "|" + encode(productCodeValue));
	}

	@Test
	public void testCalculateIntegrationKeyWhenGivenEntityWithNullValues()
	{
		final String productCodeValue = "some|product|code";
		final KeyAttribute productCodeAttr = keyAttribute(PRODUCT_ENTITY_NAME, PRODUCT_CODE_PROPERTY);
		final KeyAttribute catalogCodeAttr = keyAttribute(CATALOGVERSION_ENTITY_NAME, CATALOGVERSION_VERSION_PROPERTY);
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(catalogCodeAttr, null)
				.withValue(productCodeAttr, productCodeValue));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("null|" + encode(productCodeValue));
	}

	@Test
	public void testCalculateIntegrationKeyCombinesKeyAttributeValuesInOrderOfTheItemTypeName()
	{
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(keyAttribute("D", "code"), "D_code_value")
				.withValue(keyAttribute("C", "code"), "C_code_value")
				.withValue(keyAttribute("B", "code"), "B_code_value")
				.withValue(keyAttribute("A", "code"), "A_code_value"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("A_code_value|B_code_value|C_code_value|D_code_value");
	}

	@Test
	public void testCalculateIntegrationKeyPrefersLongerTypeNamesInOrderOfTheKeyOutput()
	{
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(keyAttribute("Item", "code"), "shortest")
				.withValue(keyAttribute("ItemType", "code"), "short")
				.withValue(keyAttribute("ItemTypeExtension", "code"), "long"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("long|short|shortest");
	}

	@Test
	public void testCalculateIntegrationKeyCombinesKeyAttributeValuesInOrderOfTheAttributeName()
	{
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(keyAttribute("A", "attrB"), "A_attrB_value")
				.withValue(keyAttribute("A", "attrA"), "A_attrA_value")
				.withValue(keyAttribute("A", "attrC"), "A_attrC_value")
				.withValue(keyAttribute("A", "attrD"), "A_attrD_value"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("A_attrA_value|A_attrB_value|A_attrC_value|A_attrD_value");
	}

	@Test
	public void testCalculateIntegrationKeyPrefersShorterAttributeNamesInOrderOfTheKeyOutput()
	{
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(keyAttribute("Item", "code"), "shortest")
				.withValue(keyAttribute("Item", "codeValue"), "short")
				.withValue(keyAttribute("Item", "codeValueAttribute"), "long"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("shortest|short|long");
	}

	@Test
	public void testCalculateIntegrationKeyPrefersItemTypeOrderOverAttributeNameOrderInTheKeyOutput()
	{
		givenCalculatedKeyForItemModelAndEntityIs(new KeyValue.Builder()
				.withValue(keyAttribute("D", "attr4"), "four")
				.withValue(keyAttribute("O", "attr2"), "two")
				.withValue(keyAttribute("I", "attr3"), "three")
				.withValue(keyAttribute("T", "attr1"), "one"));

		assertThat(integrationKeyGenerator.generate(ITEM_TYPE, ENTRY))
				.isEqualTo("four|three|two|one");
	}

	private static String encode(final String value)
	{
		try
		{
			return URLEncoder.encode(value, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	private KeyAttribute keyAttribute(final String type, final String property)
	{
		final IntegrationObjectModel object = mock(IntegrationObjectModel.class);
		doReturn("").when(object).getCode();
		final IntegrationObjectItemModel item = mock(IntegrationObjectItemModel.class, type);
		doReturn(type).when(item).getCode();
		doReturn(object).when(item).getIntegrationObject();

		final AttributeDescriptorModel attributeDescriptor = mock(AttributeDescriptorModel.class);
		doReturn(false).when(attributeDescriptor).getLocalized();
		final IntegrationObjectItemAttributeModel attribute = mock(IntegrationObjectItemAttributeModel.class,
				type + ":" + property);
		doReturn(attributeDescriptor).when(attribute).getAttributeDescriptor();
		doReturn(property).when(attribute).getAttributeName();
		doReturn(item).when(attribute).getIntegrationObjectItem();
		return new KeyAttribute(attribute);
	}

	private void givenCalculatedKeyForItemModelAndEntityIs(final KeyValue.Builder keyBuilder)
	{
		final KeyDescriptor keyDescriptor = mock(KeyDescriptor.class);
		doReturn(keyBuilder.build()).when(keyDescriptor).calculateKey(ENTRY);
		doReturn(keyDescriptor).when(ITEM_TYPE).getKeyDescriptor();
	}
}