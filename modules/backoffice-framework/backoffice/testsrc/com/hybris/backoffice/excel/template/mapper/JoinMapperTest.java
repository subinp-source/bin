/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JoinMapperTest
{
	private static final String INPUT = "product";

	@InjectMocks
	@Spy
	JoinMapper joinMapper;

	@Mock
	ExcelMapper mapper1;

	@Mock
	ExcelMapper mapper2;

	@Test
	public void shouldNotReturnDuplicatedObjects()
	{
		//given
		final String firstObject = "object1";
		final String secondObject = "object2";

		when(mapper1.apply(INPUT)).thenReturn(List.of(firstObject, secondObject));
		when(mapper2.apply(INPUT)).thenReturn(List.of(firstObject, secondObject));

		//when
		final Collection result = joinMapper.apply(INPUT);

		//then
		assertThat(result).containsOnlyOnce(firstObject, secondObject);
	}

	@Test
	public void shouldReturnMergedNotDuplicatedObjects()
	{
		//given
		final String firstObject = "object1";
		final String secondObject = "object2";
		final String thirdObject = "object3";

		when(mapper1.apply(INPUT)).thenReturn(List.of(firstObject, secondObject));
		when(mapper2.apply(INPUT)).thenReturn(List.of(thirdObject));

		//when
		final Collection result = joinMapper.apply(INPUT);

		//then
		assertThat(result).containsOnly(firstObject, secondObject, thirdObject);
	}

}
