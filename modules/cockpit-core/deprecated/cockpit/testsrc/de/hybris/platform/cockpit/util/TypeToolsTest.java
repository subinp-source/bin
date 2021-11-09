/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Test;


public final class TypeToolsTest
{
	@Test(expected = NullPointerException.class)
	public void testCreateCollectionWhenClazzIsNull()
	{
		TypeTools.createCollection(null, Collections.emptyList());
	}

	@Test(expected = NullPointerException.class)
	public void testCreateCollectionWhenItemsCollectionIsNull()
	{
		TypeTools.createCollection(ArrayList.class, null);
	}

	@Test
	public void testCreateCollectionWhenClazzIsLinkedHashSet()
	{
		// given
		final Collection<String> items = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));

		// when
		final Collection<String> collection = TypeTools.createCollection(LinkedHashSet.class, items);

		// then
		Assertions.assertThat(collection).isInstanceOf(LinkedHashSet.class);
		Assertions.assertThat(collection).hasSameSizeAs(items);
		Assertions.assertThat(collection).containsAll(items);
	}

	@Test
	public void testCreateCollectionWhenClazzIsSet()
	{
		// given
		final Collection<String> items = Collections.unmodifiableList(Arrays.asList("1", "2", "3"));

		// when
		final Collection<String> collection = TypeTools.createCollection(Set.class, items);

		// then
		Assertions.assertThat(collection).isInstanceOf(HashSet.class);
		Assertions.assertThat(collection).hasSameSizeAs(items);
		Assertions.assertThat(collection).containsAll(items);
	}

	@Test
	public void testCreateCollectionWhenClazzIsEmptyList()
	{
		// given
		final Collection<String> items = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));

		// when
		final Collection<String> collection = TypeTools.createCollection(Collections.emptyList().getClass(), items);

		// then
		Assertions.assertThat(collection).isInstanceOf(ArrayList.class);
		Assertions.assertThat(collection).hasSameSizeAs(items);
		Assertions.assertThat(collection).containsAll(items);
	}
}
