/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class AsGroupReplaceMergeStrategyTest extends AbstractAsMergeStrategyTest
{
	private AsGroupReplaceMergeStrategy mergeStrategy;

	@Before
	public void createMergeStrategy()
	{
		mergeStrategy = new AsGroupReplaceMergeStrategy();
		mergeStrategy.setAsSearchProfileResultFactory(getAsSearchProfileResultFactory());
	}

	@Test
	public void mergeGroup()
	{
		// given
		final AsGroup group1 = new AsGroup();
		group1.setExpression(INDEX_PROPERTY_1);
		group1.setUid(UID_1);

		final AsGroup group2 = new AsGroup();
		group2.setExpression(INDEX_PROPERTY_2);
		group2.setUid(UID_2);

		getTarget().setGroup(createConfigurationHolder(group1));
		getSource().setGroup(createConfigurationHolder(group2));

		// when
		mergeStrategy.mergeGroup(getSource(), getTarget());

		// then
		assertEquals(AsGroupMergeMode.REPLACE, getTarget().getGroupMergeMode());

		final AsConfigurationHolder<AsGroup, AsGroup> groupHolder = getTarget().getGroup();
		assertSame(group2, groupHolder.getConfiguration());
		assertThat(groupHolder.getReplacedConfigurations()).hasSize(1).containsExactly(group1);
	}

	@Test
	public void mergeGroupWithNullSource()
	{
		// given
		final AsGroup group = new AsGroup();
		group.setExpression(INDEX_PROPERTY_1);
		group.setUid(UID_1);

		getTarget().setGroup(createConfigurationHolder(group));

		// when
		mergeStrategy.mergeGroup(getSource(), getTarget());

		// then
		assertEquals(AsGroupMergeMode.REPLACE, getTarget().getGroupMergeMode());

		final AsConfigurationHolder<AsGroup, AsGroup> groupHolder = getTarget().getGroup();
		assertNull(groupHolder);
	}

	@Test
	public void mergeGroupWithNullTarget()
	{
		// given
		final AsGroup group = new AsGroup();
		group.setExpression(INDEX_PROPERTY_1);
		group.setUid(UID_1);

		getSource().setGroup(createConfigurationHolder(group));

		// when
		mergeStrategy.mergeGroup(getSource(), getTarget());

		// then
		assertEquals(AsGroupMergeMode.REPLACE, getTarget().getGroupMergeMode());

		final AsConfigurationHolder<AsGroup, AsGroup> groupHolder = getTarget().getGroup();
		assertSame(group, groupHolder.getConfiguration());
		assertThat(groupHolder.getReplacedConfigurations()).isEmpty();
	}
}
