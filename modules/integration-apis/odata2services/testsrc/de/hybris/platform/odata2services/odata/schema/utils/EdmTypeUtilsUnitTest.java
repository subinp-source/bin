/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.junit.Test;

@UnitTest
public class EdmTypeUtilsUnitTest
{
	@Test
	public void testConvertNull()
	{
		assertThatThrownBy(() -> assertPlatformTypeConvertedTo(null, null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testConvertBigDecimal()
	{
		assertPlatformTypeConvertedTo("java.math.BigDecimal", EdmSimpleTypeKind.Decimal);
	}

	@Test
	public void testConvertBigInteger()
	{
		assertPlatformTypeConvertedTo("java.math.BigInteger", EdmSimpleTypeKind.String);
	}

	@Test
	public void testConvertString()
	{
		assertPlatformTypeConvertedTo("java.lang.String", EdmSimpleTypeKind.String);
	}

	@Test
	public void testConvertDouble()
	{
		assertPlatformTypeConvertedTo("java.lang.Double", EdmSimpleTypeKind.Double);
	}

	@Test
	public void testConvertInteger()
	{
		assertPlatformTypeConvertedTo("java.lang.Integer", EdmSimpleTypeKind.Int32);
	}

	@Test
	public void testConvertLong()
	{
		assertPlatformTypeConvertedTo("java.lang.Long", EdmSimpleTypeKind.Int64);
	}

	@Test
	public void testConvertBoolean()
	{
		assertPlatformTypeConvertedTo("java.lang.Boolean", EdmSimpleTypeKind.Boolean);
	}

	@Test
	public void testConvertDate()
	{
		assertPlatformTypeConvertedTo("java.util.Date", EdmSimpleTypeKind.DateTime);
	}

	@Test
	public void testConvertByte()
	{
		assertPlatformTypeConvertedTo("java.lang.Byte", EdmSimpleTypeKind.SByte);
	}

	@Test
	public void testConvertFloat()
	{
		assertPlatformTypeConvertedTo("java.lang.Float", EdmSimpleTypeKind.Single);
	}

	@Test
	public void testConvertShort()
	{
		assertPlatformTypeConvertedTo("java.lang.Short", EdmSimpleTypeKind.Int16);
	}

	@Test
	public void testConvertObject()
	{
		assertPlatformTypeConvertedTo("java.lang.Object", EdmSimpleTypeKind.String);
	}

	@Test
	public void testConvertCharacter()
	{
		assertPlatformTypeConvertedTo("java.lang.Character", EdmSimpleTypeKind.String);
	}

	private void assertPlatformTypeConvertedTo(final String platformType, final EdmSimpleTypeKind expectedEdmType)
	{
		final EdmSimpleTypeKind edmType = EdmTypeUtils.convert(platformType);
		assertThat(edmType).isEqualTo(expectedEdmType);
	}
}