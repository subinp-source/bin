/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.ItemModel;

import java.util.Collection;

/**
 * Represents a reference path from one item to another as a sequence of some Java property or attribute invocation.
 * For example, let's say we have {@code Address} type, with attributes {@code owner}, which is a {@code Customer};
 * the {@code Customer} has attribute {@code organization}, which is a {@code Company}. Then a path that contains {@code owner} and
 * {@code organization} attributes would lead us from a specific instance of {@code Address} to the corresponding instance of
 * {@code Company}.
 */
public interface ReferencePath
{
	/**
	 * Retrieves destination type for this path.
	 * @return destination type of this navigation path
	 */
	TypeDescriptor getDestination();

	/**
	 * Determines how many attributes need to be called in the navigation process until the destination item is reached.
	 * @return number of attribute calls chained to form this path. 0 means no attribute invocation is involved and the
	 * source {@link TypeDescriptor} matches the destination {@link TypeDescriptor}
	 */
	int length();

	/**
	 * Determines whether this path navigates through the specified integration item type.
	 * @param type integration item type to check
	 * @return {@code true}, if this path has the specified item type as its source or its destination or it's present anywhere
	 * in between the source and destination; {@code false}, if this path does not navigate through the specified type at all.
	 */
	boolean reaches(TypeDescriptor type);

	/**
	 * Expands current path into all possible references from the current destination in this path to other integration items. For
	 * example, let's say current path contains navigation from {@code Category} to {@code Product} and {@code Product} has references
	 * to {@code Unit} and {@code CatalogVersion}, then the result of the expansion will contain two paths:
	 * <ol>
	 *     <li>Category -> Product -> Unit</li>
	 *     <li>Category -> Product -> CatalogVersion</li>
	 * </ol>
	 * @return a collection of all paths expanded from the destination. If the destination references itself as possible
	 * navigation, this reference will be excluded from the result. For example, if a {@code Category} has a property/attribute that
	 * references another {@code Category} item instance, e.g. its {@code supercategory}, this navigation from {@code Category}
	 * to {@code Category} is excluded. Moreover, the navigation is excluded, if it forms a loop in the path. For example, current
	 * path contains navigation from {@code Category} to {@code Product}. If product has navigation back to the {@code Category} or
	 * one of its predecessors such reference from the {@code Product} will be ignored and not expanded. If the destination
	 * in this path does not have references to other integration items, then it cannot be expanded and therefore an empty {@code Collection}
	 * is returned.
	 * @see #getDestination()
	 */
	Collection<ReferencePath> expand();

	/**
	 * Performs navigation to the referenced item(s) by following this reference path.
	 * @param item an item to resolve attribute references from.
	 * @return a collection of the items or values referenced by this path from the specified item. The collection contains multiple
	 * elements, if some reference along the path resolves to a collection of values. For example, first attribute in this path
	 * resolves to a collection of items {@code [a1, a2]}, then next attribute in this path resolves to {@code [b11, b12]} values
	 * for {@code a1} and to {@code [b21, b22]} for {@code a2}. The resulting collection will contain values of {@code [b11, b12, b21, b22]}.
	 * If this path does not resolve to collections along the way, then the resulting collection will have only one element of the
	 * destination type. If this path resolves to {@code null} somewhere along the way, then the resulting collection is empty.
	 */
	Collection<Object> execute(ItemModel item);
}
