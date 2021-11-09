/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.mockito.Mockito.when;

import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import de.hybris.platform.adaptivesearch.util.ConfigurationUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public abstract class AbstractAsMergeStrategyTest
{
	protected static final String UID_1 = "uid1";
	protected static final String UID_2 = "uid2";
	protected static final String UID_3 = "uid3";
	protected static final String UID_4 = "uid4";
	protected static final String UID_5 = "uid5";
	protected static final String UID_6 = "uid6";
	protected static final String UID_7 = "uid7";
	protected static final String UID_8 = "uid8";

	protected static final String INDEX_PROPERTY_1 = "property1";
	protected static final String INDEX_PROPERTY_2 = "property2";
	protected static final String INDEX_PROPERTY_3 = "property3";
	protected static final String INDEX_PROPERTY_4 = "property4";

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	private DefaultAsSearchProfileResultFactory asSearchProfileResultFactory;

	private AsSearchProfileResult source;
	private AsSearchProfileResult target;

	@Before
	public void initalize()
	{
		MockitoAnnotations.initMocks(this);

		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString(ConfigurationUtils.DEFAULT_FACETS_MERGE_MODE, AsFacetsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsFacetsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_BOOST_ITEMS_MERGE_MODE, AsBoostItemsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsBoostItemsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_BOOST_RULES_MERGE_MODE, AsBoostRulesMergeMode.ADD.name()))
				.thenReturn(AsBoostRulesMergeMode.ADD.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_SORTS_MERGE_MODE, AsSortsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsSortsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_SORTS_MERGE_MODE, AsSortsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsSortsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_GROUP_MERGE_MODE, AsGroupMergeMode.INHERIT.name()))
				.thenReturn(AsGroupMergeMode.INHERIT.name());

		asSearchProfileResultFactory = new DefaultAsSearchProfileResultFactory();
		asSearchProfileResultFactory.setConfigurationService(configurationService);

		source = createResult();
		target = createResult();
	}

	protected AsSearchProfileResultFactory getAsSearchProfileResultFactory()
	{
		return asSearchProfileResultFactory;
	}

	public AsSearchProfileResult getSource()
	{
		return source;
	}

	public AsSearchProfileResult getTarget()
	{
		return target;
	}

	protected AsSearchProfileResult createResult()
	{
		return asSearchProfileResultFactory.createResult();
	}

	protected <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(final T configuration)
	{
		return asSearchProfileResultFactory.createConfigurationHolder(configuration);
	}
}
