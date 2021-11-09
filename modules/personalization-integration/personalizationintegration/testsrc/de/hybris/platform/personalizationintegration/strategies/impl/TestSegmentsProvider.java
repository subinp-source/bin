/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.personalizationintegration.segment.SegmentsProvider;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Implementation of {@link SegmentsProvider} for strategy tests
 */
public class TestSegmentsProvider implements SegmentsProvider
{
	private final Map<BaseSiteModel, List<BaseSegmentData>> baseSitesSegments;
	private String providerId;
	private final BaseSiteService baseSiteService;


	public TestSegmentsProvider(final String providerId, final BaseSiteService baseSiteService)
	{
		this.providerId = providerId;
		this.baseSiteService = baseSiteService;
		this.baseSitesSegments = new HashMap<>();
	}


	@Override
	public Optional<List<BaseSegmentData>> getSegments()
	{
		return Optional.ofNullable(baseSitesSegments.get(baseSiteService.getCurrentBaseSite()));
	}

	public void setSegments(final List<BaseSegmentData> segments)
	{
		setSegments(null, segments);
	}

	public void setSegments(final BaseSiteModel baseSite, final List<BaseSegmentData> segments)
	{
		baseSitesSegments.put(baseSite, segments);
	}


	public void addSegment(final BaseSegmentData segment)
	{
		addSegment(null, segment);
	}

	public void addSegment(final BaseSiteModel baseSiteModel, final BaseSegmentData segment)
	{
		final List<BaseSegmentData> segments = baseSitesSegments.computeIfAbsent(baseSiteModel, k -> new ArrayList<>());
		segments.add(segment);
	}

	public void clearSegments()
	{
		baseSitesSegments.clear();
	}


	@Override
	public String getProviderId()
	{
		return providerId;
	}

	public void setProviderId(final String providerId)
	{
		this.providerId = providerId;
	}

}
