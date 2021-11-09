/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.classification.labels.impl;

import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.Range;
import de.hybris.bootstrap.annotations.UnitTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class RangeLabelProviderTest
{
	private static final String RANGE_FROM_LABEL = "from";
	private static final String RANGE_TO_LABEL = "to";
	private static final String RANGE_FROM_VALUE = "val_from";
	private static final String RANGE_TO_VALUE = "val_to";
	private static final String RANGE_LABEL_PATTERN = "%s %s %s %s";

	@InjectMocks
	@Spy
	private RangeLabelProvider testSubject;

	@Mock
	private LabelService labelService;

	@Mock
	private Range<Object> range;

	@Before
	public void setUp()
	{
		doReturn(RANGE_FROM_LABEL).when(testSubject).getLabelFrom();
		doReturn(RANGE_TO_LABEL).when(testSubject).getLabelTo();
		doReturn(StringUtils.EMPTY).when(testSubject).getDefaultNoValue();
	}

	@Test
	public void shouldProvideResultLabelWithEmptyStringValueWhenStartRangeIsNull()
	{
		// given
		when(range.getStart()).thenReturn(null);
		when(range.getEnd()).thenReturn(RANGE_TO_VALUE);
		when(labelService.getObjectLabel(range.getStart())).thenReturn(null);
		when(labelService.getObjectLabel(range.getEnd())).thenReturn(RANGE_TO_VALUE);

		// when
		final String resultLabel = testSubject.getLabel(range);

		// then
		assertThat(resultLabel)
				.isEqualTo(String.format(RANGE_LABEL_PATTERN, RANGE_FROM_LABEL, StringUtils.EMPTY, RANGE_TO_LABEL, RANGE_TO_VALUE));
	}

	@Test
	public void shouldProvideResultLabelWithEmptyStringValueWhenEndRangeIsNull()
	{
		// given
		when(range.getStart()).thenReturn(RANGE_FROM_VALUE);
		when(range.getEnd()).thenReturn(null);
		when(labelService.getObjectLabel(range.getStart())).thenReturn(RANGE_FROM_VALUE);
		when(labelService.getObjectLabel(range.getEnd())).thenReturn(null);

		// when
		final String resultLabel = testSubject.getLabel(range);

		// then
		assertThat(resultLabel)
				.isEqualTo(String.format(RANGE_LABEL_PATTERN, RANGE_FROM_LABEL, RANGE_FROM_VALUE, RANGE_TO_LABEL, StringUtils.EMPTY));
	}

	@Test
	public void shouldProvideFilledResultLabelWhenBothRangesAreNotNull()
	{
		// given
		when(range.getStart()).thenReturn(RANGE_FROM_VALUE);
		when(range.getEnd()).thenReturn(RANGE_TO_VALUE);
		when(labelService.getObjectLabel(range.getStart())).thenReturn(RANGE_FROM_VALUE);
		when(labelService.getObjectLabel(range.getEnd())).thenReturn(RANGE_TO_VALUE);

		// when
		final String resultLabel = testSubject.getLabel(range);

		// then
		assertThat(resultLabel)
				.isEqualTo(String.format(RANGE_LABEL_PATTERN, RANGE_FROM_LABEL, RANGE_FROM_VALUE, RANGE_TO_LABEL, RANGE_TO_VALUE));
	}

	@Test
	public void getLabelShouldUseSpecificValueForNullInRange()
	{
	    //given
		when(range.getStart()).thenReturn(null);
		when(range.getEnd()).thenReturn(null);
		doReturn("From").when(testSubject).getLabelFrom();
		doReturn("To").when(testSubject).getLabelTo();
		doReturn("Nothing").when(testSubject).getDefaultNoValue();

	    //when
		final String label = testSubject.getLabel(range);

		//then
		assertThat(label).isEqualTo("From Nothing To Nothing");
	}
}
