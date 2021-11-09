/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.integrationservices.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.uri.NavigationPropertySegment;
import org.apache.olingo.odata2.api.uri.NavigationSegment;
import org.slf4j.Logger;

public class ConversionOptions
{
	private static final Logger LOG = Log.getLogger(ConversionOptions.class);

	private boolean includeCollections = true;
	private List<NavigationSegment> navigationSegments;
	private List<ExpandPath> expandPaths;

	protected ConversionOptions()
	{
		navigationSegments = Collections.emptyList();
		expandPaths = Collections.emptyList();
	}

	public static ConversionOptionsBuilder conversionOptionsBuilder()
	{
		return new ConversionOptionsBuilder();
	}

	/**
	 * Determines whether collection conversion should be done.
	 *
	 * @return {@code true}, if nested collection(s) should be converted; {@code false}, otherwise.
	 * @deprecated not used anymore, the solution is generalized for entities and collections.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	public boolean isIncludeCollections()
	{
		return includeCollections;
	}

	/**
	 * Specifies to include or exclude nested collections from further item grapsh references conversions.
	 *
	 * @param includeCollections {@code true}, if nested collection(s) should be converted; {@code false}, if not.
	 * @deprecated not used anymore, the solution is generalized for entities and collections.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	protected void setIncludeCollections(final boolean includeCollections)
	{
		this.includeCollections = includeCollections;
	}

	public List<NavigationSegment> getNavigationSegments()
	{
		return navigationSegments;
	}

	protected void setNavigationSegments(final List<NavigationSegment> segments)
	{
		navigationSegments = segments != null
				? Collections.unmodifiableList(segments)
				: Collections.emptyList();
	}

	protected void setExpand(final List<List<NavigationPropertySegment>> expand)
	{
		final List<ExpandPath> paths = expand == null
				? Collections.emptyList()
				: expand.stream()
				        .map(ExpandPath::new)
				        .collect(Collectors.toList());
		setExpandPaths(paths);
	}

	private void setExpandPaths(final List<ExpandPath> paths)
	{
		expandPaths = paths != null
				? paths
				: Collections.emptyList();
	}

	public List<List<NavigationPropertySegment>> getExpand()
	{
		return expandPaths.stream()
		                  .map(p -> p.pathSegments)
		                  .collect(Collectors.toList());
	}

	/**
	 * Determines whether navigation segments are present in this options.
	 *
	 * @return {@code true}, if at least one navigation segment was added to this options; {@code false}, otherwise.
	 * @see #setNavigationSegments(List)
	 */
	public boolean isNavigationSegmentPresent()
	{
		return !getNavigationSegments().isEmpty();
	}

	/**
	 * Determines whether an expand option is present in this options.
	 *
	 * @return {@code true}, if at least one nagivagtion property segment was added to this options; {@code false}, otherwise.
	 * @see #setExpand(List)
	 */
	public boolean isExpandPresent()
	{
		return getExpand().stream()
		                  .anyMatch(CollectionUtils::isNotEmpty);
	}

	/**
	 * Navigates the navigation segment.
	 *
	 * @param propertyName name of the item property for the navigation segment to navigate.
	 * @return new instance of conversion of options without the consumed navigation segment or this options, if the navigation
	 * did not happen because the segment does not exist or the property name is invalid.
	 */
	public ConversionOptions navigate(final String propertyName)
	{
		return StringUtils.isBlank(propertyName)
				? this
				: createOptionsForNavigationProperty(propertyName);
	}

	private ConversionOptions createOptionsForNavigationProperty(final String propertyName)
	{
		return conversionOptionsBuilder()
				.withIncludeCollections(includeCollections)
				.withNavigationSegments(consumeNavigationProperty(propertyName))
				.withExpandPaths(consumeExpandSegment(propertyName))
				.build();
	}

	private List<ExpandPath> consumeExpandSegment(final String propertyName)
	{
		return expandPaths.stream()
		                  .map(path -> path.navigate(propertyName))
		                  .filter(ExpandPath::isNotEmpty)
		                  .collect(Collectors.toList());
	}

	private List<NavigationSegment> consumeNavigationProperty(final String property)
	{
		return isNextNavigationSegment(property)
				? navigationSegments.subList(1, navigationSegments.size())
				: navigationSegments;
	}

	/**
	 * Determines whether the specified property name is the next navigation segment of this options.
	 *
	 * @param propertyName name of the property to enquire about
	 * @return {@code true}, if next navigation segment in this options matches the property name; {@code false}, if next navigation
	 * segment in this options does not match the property name of when there are no navigation segments in this options.
	 */
	public boolean isNextNavigationSegment(final String propertyName)
	{
		try
		{
			return !navigationSegments.isEmpty() && navigationSegments.get(0).getNavigationProperty().getName().equals(propertyName);
		}
		catch (final EdmException e)
		{
			LOG.debug("Failed to check property '{}' being next segment in {}", propertyName, navigationSegments, e);
			return false;
		}
	}

	/**
	 * Determines whether the specified property name is the next expand segment of this options.
	 *
	 * @param propertyName name of the property to enquire about
	 * @return {@code true}, if next expand segment in this options matches the property name; {@code false}, if next expand
	 * segment in this options does not match the property name of when there are no expand segments in this options.
	 */
	public boolean isNextExpandSegment(final String propertyName)
	{
		return expandPaths.stream().anyMatch(path -> path.isNextExpandSegment(propertyName));
	}

	public static class ConversionOptionsBuilder
	{
		private final ConversionOptions options = new ConversionOptions();

		/**
		 * Specifies whether collection conversion should be done.
		 *
		 * @param includeCollections {@code true}, if nested collection(s) should be converted; {@code false}, if not.
		 * @deprecated not used anymore, the solution is generalized for entities and collections.
		 */
		@Deprecated(since = "1905.10-CEP", forRemoval = true)
		public ConversionOptionsBuilder withIncludeCollections(final boolean includeCollections)
		{
			this.options.setIncludeCollections(includeCollections);
			return this;
		}

		public ConversionOptionsBuilder withNavigationSegment(final NavigationSegment s)
		{
			final List<NavigationSegment> segments = new ArrayList<>(options.getNavigationSegments());
			segments.add(s);
			return withNavigationSegments(segments);
		}

		public ConversionOptionsBuilder withNavigationSegments(final List<NavigationSegment> navigationSegments)
		{
			this.options.setNavigationSegments(navigationSegments);
			return this;
		}

		public ConversionOptionsBuilder withExpand(final List<? extends List<NavigationPropertySegment>> expand)
		{
			final List<List<NavigationPropertySegment>> segments = expand != null
					? new ArrayList<>(expand)
					: null;
			this.options.setExpand(segments);
			return this;
		}

		private ConversionOptionsBuilder withExpandPaths(final List<ExpandPath> paths)
		{
			this.options.setExpandPaths(paths);
			return this;
		}

		public ConversionOptionsBuilder from(final ConversionOptions options)
		{
			withIncludeCollections(options.isIncludeCollections());
			withNavigationSegments(options.getNavigationSegments());
			withExpand(options.getExpand());

			return this;
		}

		public ConversionOptions build()
		{
			return options;
		}
	}

	private static class ExpandPath
	{
		private final List<NavigationPropertySegment> pathSegments;

		private ExpandPath(final List<NavigationPropertySegment> segments)
		{
			pathSegments = segments != null
					? segments
					: Collections.emptyList();
		}

		private ExpandPath navigate(final String property)
		{
			return isNextExpandSegment(property)
					? new ExpandPath(pathSegments.subList(1, pathSegments.size()))
					: this;
		}

		private boolean isNextExpandSegment(final String propertyName)
		{
			try
			{
				return !pathSegments.isEmpty() && pathSegments.get(0).getNavigationProperty().getName().equals(propertyName);
			}
			catch (final EdmException e)
			{
				LOG.debug("Failed to check property '{}' being next segment in {}", propertyName, pathSegments, e);
				return false;
			}
		}

		private boolean isNotEmpty()
		{
			return !pathSegments.isEmpty();
		}
	}
}
