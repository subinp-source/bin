/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.converter.attribute.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EnumDataToModelConverterTest
{

	private final String ENUM_VALUE = "CHECK";
	private final String ENUM_TYPE = "de.hybris.platform.cms2.enums.CmsApprovalStatus";

	@InjectMocks
	private EnumDataToModelConverter converter;

	@Mock
	private EnumerationService enumerationService;

	@Mock
	private HybrisEnumValue enumValue;

	private final VersionPayloadDescriptor payloadDescriptor = new VersionPayloadDescriptor(ENUM_TYPE, ENUM_VALUE);

	@Test
	public void shouldConvertHybrisEnumValueRepresentedByStringToHybrisEnumValue()
	{

		// GIVEN
		doReturn(enumValue).when(enumerationService).getEnumerationValue((Class) CmsApprovalStatus.class, ENUM_VALUE.toLowerCase());

		// WHEN
		final Object value = converter.convert(payloadDescriptor);

		// THEN
		assertThat(value, is(enumValue));

	}

}
