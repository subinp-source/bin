/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.common.service.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCollectionHelperTest
{
	private final String FIRST_KEY = "firstKey";
	private final String FIRST_VALUE = "firstValue";

	private final String SECOND_KEY = "secondKey";
	private final String SECOND_VALUE = "secondValue";

	private final String LIST_VALUE1 = "listValue1";
	private final String LIST_VALUE2 = "listValue2";
	private final String LIST_VALUE3 = "listValue3";

	private final int PRIMITIVE1 = 1;
	private final int PRIMITIVE2 = 2;

	@InjectMocks
	@Spy
	private DefaultCollectionHelper helper;

	@Test
	public void shouldMergeTwoMapsWithoutMergingValuesIfNoDuplicateKeys()
	{
		// GIVEN
		final Map<String, Object> first = new HashMap<>();
		first.put(FIRST_KEY, FIRST_VALUE);
		final Map<String, Object> second = new HashMap<>();
		second.put(SECOND_KEY, SECOND_VALUE);

		// WHEN
		final Map<String, Object> result = helper.mergeMaps(first, second, (v1, v2) -> v2);

		// THEN
		assertThat(result.entrySet(), hasSize(2));
		assertThat(result.get(FIRST_KEY), is(FIRST_VALUE));
		assertThat(result.get(SECOND_KEY), is(SECOND_VALUE));
	}

	@Test
	public void shouldMergeTwoMapsAndMergeDuplicateKeyValues()
	{
		// GIVEN
		final Map<String, Object> first = new HashMap<>();
		first.put(FIRST_KEY, FIRST_VALUE);
		final Map<String, Object> second = new HashMap<>();
		second.put(FIRST_KEY, SECOND_VALUE);

		// WHEN
		final Map<String, Object> result = helper.mergeMaps(first, second, (v1, v2) -> v2);

		// THEN
		assertThat(result.entrySet(), hasSize(1));
		assertThat(result.get(FIRST_KEY), is(SECOND_VALUE));
	}

	@Test
	public void mergeFunctionShouldConcatenateIfArgumentsAreCollections()
	{
		// GIVEN
		final List<String> first = Arrays.asList(LIST_VALUE1, LIST_VALUE2);
		final List<String> second = Arrays.asList(LIST_VALUE3);

		// WHEN
		final BinaryOperator<Object> objectBinaryOperator = helper.defaultDeepMergeFunction();
		final Object result = objectBinaryOperator.apply(first, second);

		// THEN
		assertThat((Collection<Object>) result, contains(LIST_VALUE1, LIST_VALUE2, LIST_VALUE3));
	}

	@Test
	public void mergeFunctionReturnSecondArgumentIfArgumentsArePrimitives()
	{
		// GIVEN

		// WHEN
		final BinaryOperator<Object> objectBinaryOperator = helper.defaultDeepMergeFunction();
		final Object result = objectBinaryOperator.apply(PRIMITIVE1, PRIMITIVE2);

		// THEN
		assertThat(result, is(PRIMITIVE2));
	}

	@Test
	public void mergeFunctionShouldMergeMapsIfArgumentsAreMaps()
	{
		// GIVEN
		final Map<String, Object> first = new HashMap<>();
		first.put(FIRST_KEY, FIRST_VALUE);
		final Map<String, Object> second = new HashMap<>();
		second.put(SECOND_KEY, SECOND_VALUE);
		// WHEN
		final BinaryOperator<Object> objectBinaryOperator = helper.defaultDeepMergeFunction();
		final Object result = objectBinaryOperator.apply(first, second);

		// THEN
		verify(helper).mergeMaps(any(), any());
	}
}
