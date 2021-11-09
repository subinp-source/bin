/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * A helper class for finding {@link ReferencePath}s.
 */
class AttributePathFinder
{
	private static final int MAX_ITERATIONS = 1000;
	private final TypeDescriptor sourceType;
	private final TypeDescriptor destinationType;

	private AttributePathFinder(final TypeDescriptor src, final TypeDescriptor dest)
	{
		Preconditions.checkArgument(src != null, "Source type descriptor is required");
		Preconditions.checkArgument(dest != null, "Destination type descriptor is required");

		sourceType = src;
		destinationType = dest;
	}

	/**
	 * Finds all possible reference paths between the specified given item types.
	 * @param src a type to find references from
	 * @param dest a type to find reference paths to
	 * @return a list of paths leading from the source type to the destination type or an empty list, if there are no paths from
	 * the source to the destination. The paths are ordered from shortest to longest in the list.
	 */
	static List<ReferencePath> findPaths(final TypeDescriptor src, final TypeDescriptor dest)
	{
		return new AttributePathFinder(src, dest).searchTypeRelationGraph();
	}

	private List<ReferencePath> searchTypeRelationGraph()
	{
		final List<ReferencePath> found = new ArrayList<>();
		List<ReferencePath> paths = Lists.newArrayList(new EmptyReferencePath(sourceType));
		int loopGuard = 0; // guards against bugs in the expand() implementations and prevents infinite loops
		do
		{
			final List<ReferencePath> completed = paths.stream()
					.filter(p -> destinationType.equals(p.getDestination()))
					.collect(Collectors.toList());
			found.addAll(completed);
			paths.removeAll(completed);
			paths = paths.stream()
					.map(ReferencePath::expand)
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
		} while (! paths.isEmpty() && (loopGuard++ < MAX_ITERATIONS));
		return found;
	}
}
