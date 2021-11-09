/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.merchandisingservicesbackoffice.editor.facade;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.hybris.merchandising.exceptions.MerchandisingMetricRollupException;
import com.hybris.merchandising.metric.rollup.strategies.MerchandisingMetricRollupStrategy;

import de.hybris.platform.merchandisingservicesbackoffice.editor.DefaultProductDirectoryRollupStrategyEditor;

/**
 * Facade used to retrieve the list of 'MerchandisingMetricRollupStrategy's in the
 * {@link DefaultProductDirectoryRollupStrategyEditor}
 */
public class MerchandisingRollupStrategySearchFacade
{

	private List<String> rollupStrategies = Collections.emptyList();

	public List<String> getRollupStrategies()
	{
		return rollupStrategies;
	}

	public void setRollupStrategies(final List<MerchandisingMetricRollupStrategy> rollupStrategies)
		throws MerchandisingMetricRollupException
	{
		final List<String> rollupStrategyNames = Optional.ofNullable(rollupStrategies)
			.filter(CollectionUtils::isNotEmpty)
			.map(Collection::stream)
			.map(rollupStrategyStream -> rollupStrategyStream
				.sorted(Comparator.comparingInt(MerchandisingMetricRollupStrategy::getOrder))
				.map(MerchandisingMetricRollupStrategy::getName)
				.collect(Collectors.toList()))
			.orElseThrow(() -> new MerchandisingMetricRollupException(
				"No beans of type MerchandisingMetricRollupStrategy found to generate rollup strategy list"));

		/*
		 * As we are using the strategy name for uniqueness rather than bean id we need to ensure that there are not
		 * multiple beans defined with the same name.
		 */
		this.rollupStrategies = Optional.of(rollupStrategyNames)
			.filter(strategyNames -> strategyNames.stream()
				.allMatch(strategyName -> Collections.frequency(rollupStrategyNames, strategyName) == 1))
			.orElseThrow(() -> new MerchandisingMetricRollupException(
				"Multiple MerchandisingMetricRollupStrategy beans found with same value for getName"));
	}

}
