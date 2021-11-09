/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.service.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTimeDiffServiceTest
{
	private static Long CURRENT_TIME = 100l;
	private static Long PREVIOUS_TIME = 50l;


	@InjectMocks
	private DefaultTimeDiffService timeDiffService;

	@Mock
	private TimeService timeService;

	@Mock
	private Date currentDate;

	@Mock
	private Date checkDate;

	@Test
	public void shouldCalculateDifferenceBetweenCurrentDateAndProvidedDate()
	{
		// GIVEN
		when(timeService.getCurrentTime()).thenReturn(currentDate);
		when(currentDate.getTime()).thenReturn(CURRENT_TIME);
		when(checkDate.getTime()).thenReturn(PREVIOUS_TIME);

		// WHEN
		final Long difference = timeDiffService.difference(checkDate);

		// THEN
		assertThat(difference, is(50l));
	}
}
