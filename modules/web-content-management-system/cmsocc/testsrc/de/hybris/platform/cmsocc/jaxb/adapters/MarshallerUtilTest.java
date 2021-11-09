/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MarshallerUtilTest
{
	private static final String TEST_STRING = "testString";
	private static final String TEST_KEY = "testKey";
	private static final String TEST_OBJECT_KEY_1 = "keyObject1";
	private static final String TEST_OBJECT = "Object";
	private static final Map<String, Object> TEST_MAP = new HashMap<>();
	private static final Map<String, Object> TEST_OBJECT_MAP_1 = new HashMap<>();

	@Test
	public void shouldMarshalMapToListOfKeyMapAdaptedEntry()
	{
		TEST_OBJECT_MAP_1.put(TEST_KEY, TEST_STRING);

		TEST_MAP.put(TEST_OBJECT_KEY_1, TEST_OBJECT_MAP_1);
		final List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> resultList = MarshallerUtil.marshalMap(TEST_MAP);

		assertThat(Boolean.valueOf(resultList.isEmpty()), equalTo(Boolean.FALSE));
		assertThat(resultList.get(0).mapValue.get(0).strValue, equalTo(TEST_STRING));
	}

	@Test
	public void shouldMarshalPrimitiveToListOfKeyMapAdaptedEntry()
	{
		TEST_MAP.put(TEST_OBJECT_KEY_1, TEST_STRING);

		final List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> resultList = MarshallerUtil.marshalMap(TEST_MAP);

		assertThat(resultList.get(0).strValue, equalTo(TEST_STRING));
	}

	@Test
	public void shouldMarshalCollectionOfStringToListOfKeyMapAdaptedEntry()
	{
		final List<Object> testList = new ArrayList<>();
		testList.add(TEST_STRING);

		TEST_MAP.put(TEST_OBJECT_KEY_1, testList);

		final List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> resultList = MarshallerUtil.marshalMap(TEST_MAP);
		assertThat(resultList.get(0).arrayValue.get(0), equalTo(TEST_STRING));
	}

	@Test
	public void shouldMarshalDefaultCaseToListOfKeyMapAdaptedEntry()
	{
		final Map<String, Object> testMap = new HashMap<>();
		final AbstractCMSComponentData component1 = new AbstractCMSComponentData();
		component1.setUid(TEST_OBJECT);

		testMap.put(TEST_OBJECT_KEY_1, component1);

		final List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> resultList = MarshallerUtil.marshalMap(testMap);

		assertThat(resultList.get(0).mapValue.get(0).strValue, equalTo(TEST_OBJECT));
	}
}
