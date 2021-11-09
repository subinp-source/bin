/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.util;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class HtmlElementHelperTest
{
	private HtmlElementHelper elemHelper;
	private Map<String, String> properMergedMap;

	@Before
	public void setUp()
	{
		elemHelper = new HtmlElementHelper();
		properMergedMap = new HashMap<>();
		properMergedMap.put("class", "someClass someClass2");
		properMergedMap.put("1", "5");
		properMergedMap.put("3", "8");
	}

	@Test
	public void testMergeAttributeMaps()
	{
		final Map<String, String> map1 = Collections.singletonMap("class", "someClass");
		final Map<String, String> map2 = Collections.singletonMap("class", "someClass2");
		final Map<String, String> map3 = new HashMap<>();
		map3.put("1", "2");
		map3.put("2", "3");
		map3.put("3", "4");
		final Map<String, String> map4 = new HashMap<>();
		map4.put("1", "5");
		map4.put("2", null);
		map4.put("3", "8");
		map4.put("class", null);
		Map<String, String> result = elemHelper.mergeAttributeMaps(map1, map2, map3, map4);
		assertThat(result.equals(properMergedMap)).isTrue();

		final Map<String, String> map5 = Collections.singletonMap("class", "Class");
		result = elemHelper.mergeAttributeMaps(map1, map2, map5);
		assertThat(result.get("class").equals("someClass someClass2 Class")).isTrue();
	}
}
