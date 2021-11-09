/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.converter.attribute.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.version.converter.attribute.data.VersionPayloadDescriptor;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
public class DateDataToModelConverterTest
{
	private final Long DATE = 1516892842930L;

	private final DateDataToModelConverter converter = new DateDataToModelConverter();

	private VersionPayloadDescriptor payloadDescriptor;

	@Test
	public void whenConvertNullValueReturnsNull()
	{
		assertThat(converter.convert(payloadDescriptor), nullValue());
	}

	@Test
	public void whenConvertValidTimeStampWillReturnMatchingDate()
	{

		payloadDescriptor = new VersionPayloadDescriptor(Date.class.getCanonicalName(), DATE.toString());

		final Date date = converter.convert(payloadDescriptor);

		assertThat(date.getTime(), is(DATE));
	}

}
