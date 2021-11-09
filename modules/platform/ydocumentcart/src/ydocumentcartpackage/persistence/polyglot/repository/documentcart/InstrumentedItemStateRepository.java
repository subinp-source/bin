/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.util.MetricUtils;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotFeature;

import java.util.Objects;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class InstrumentedItemStateRepository implements ItemStateRepository
{
	private final ItemStateRepository targetRepository;

	private final Timer getItemTimer;
	private final Timer beginCreationTimer;
	private final Timer findItemsTimer;
	private final Timer storeItemTimer;
	private final Timer removeItemTimer;

	public InstrumentedItemStateRepository(final String repositoryName, final ItemStateRepository targetRepository,
	                                       final MetricRegistry metricRegistry)
	{
		this.targetRepository = Objects.requireNonNull(targetRepository);
		Objects.requireNonNull(repositoryName);
		Objects.requireNonNull(metricRegistry);

		getItemTimer = metricRegistry.timer(metricName(repositoryName, "query.get"));
		beginCreationTimer = metricRegistry.timer(metricName(repositoryName, "query.beginCreation"));
		findItemsTimer = metricRegistry.timer(metricName(repositoryName, "query.find"));
		storeItemTimer = metricRegistry.timer(metricName(repositoryName, "query.store"));
		removeItemTimer = metricRegistry.timer(metricName(repositoryName, "query.remove"));
	}


	private static String metricName(final String repositoryName, final String timerName)
	{
		return MetricUtils.metricName(repositoryName, timerName, "persistence");
	}

	@Override
	public ItemState get(final Identity id)
	{
		try (final Timer.Context ignored = getItemTimer.time())
		{
			return targetRepository.get(id);
		}
	}

	@Override
	public ChangeSet beginCreation(final Identity id)
	{
		try (final Timer.Context ignored = beginCreationTimer.time())
		{
			return targetRepository.beginCreation(id);
		}
	}

	@Override
	public void store(final ChangeSet changeSet)
	{
		try (final Timer.Context ignored = storeItemTimer.time())
		{
			targetRepository.store(changeSet);
		}
	}

	@Override
	public void remove(final ItemState state)
	{
		try (final Timer.Context ignored = removeItemTimer.time())
		{
			targetRepository.remove(state);
		}
	}

	@Override
	public FindResult find(final Criteria criteria)
	{
		try (final Timer.Context ignored = findItemsTimer.time())
		{
			return targetRepository.find(criteria);
		}
	}

	@Override
	public boolean isSupported(final PolyglotFeature feature)
	{
		return targetRepository.isSupported(feature);
	}
}
