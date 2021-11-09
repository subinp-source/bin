/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmAnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;

/**
 * A helper class for exploring and evaluating content of an OData EDM {@code Property}
 */
public class ODataNavigationProperty implements ODataAnnotatable
{
	private final NavigationProperty navigationProperty;

	public ODataNavigationProperty(final NavigationProperty property)
	{
		navigationProperty = property;
	}

	@Override
	public Collection<String> getAnnotationNames()
	{
		return getAnnotations().stream()
						.map(EdmAnnotationAttribute::getName)
						.collect(Collectors.toList());
	}

	@Override
	public Optional<AnnotationAttribute> getAnnotation(final String name)
	{
		return getAnnotations().stream().filter(annotation -> annotation.getName() == name).findFirst();
	}

	public List<AnnotationAttribute> getAnnotations()
	{
		final List<AnnotationAttribute> attributes = navigationProperty.getAnnotationAttributes();
		return attributes != null
				? attributes
				: Collections.emptyList();
	}

	public boolean hasName(final String expectedName)
	{
		return navigationProperty.getName().equals(expectedName);
	}

	public boolean hasFromRole(final String fromRole)
	{
		return navigationProperty.getFromRole().equals(fromRole);
	}

	public boolean hasToRole(final String toRole)
	{
		return navigationProperty.getToRole().equals(toRole);
	}

	public boolean hasRelationshipName(final String relationshipName)
	{
		if(navigationProperty.getRelationship() != null)
		{
			return navigationProperty.getRelationship().getName().equals(relationshipName);
		}
		return false;
	}
}
