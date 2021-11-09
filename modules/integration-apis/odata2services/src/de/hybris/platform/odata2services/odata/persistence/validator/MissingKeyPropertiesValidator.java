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

package de.hybris.platform.odata2services.odata.persistence.validator;

import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isKeyProperty;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isNullable;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyPropertyException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.EdmAnnotatable;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.slf4j.Logger;

/**
 * @deprecated replaced by {@link de.hybris.platform.integrationservices.search.validation.KeyPropertyValuePresenceValidator}
 */
@Deprecated(since = "1905.2002-CEP", forRemoval = true)
public class MissingKeyPropertiesValidator implements CreateItemValidator
{
	private static final Logger LOG = Log.getLogger(MissingKeyPropertiesValidator.class);

	@Override
	public void beforeItemLookup(final EdmEntityType entityType, final ODataEntry oDataEntry) throws EdmException
	{
		final Map<String, Object> properties = oDataEntry.getProperties();
		final String entityTypeName = entityType.getName();
		findNonNullableKeyProperties(entityType)
			.stream()
			.filter(name -> !properties.containsKey(name))
			.findFirst()
			.ifPresent(name -> { throw new MissingKeyPropertyException(entityTypeName, name); });
	}

	protected List<String> findNonNullableKeyProperties(final EdmEntityType entityType) throws EdmException
	{
		return entityType.getPropertyNames()
				.stream()
				.filter(name -> isNonNullableKeyProperty(entityType, name))
				.collect(Collectors.toList());
	}

	private boolean isNonNullableKeyProperty(final EdmEntityType entityType, final String propertyName)
	{
		try
		{
			final EdmTyped property = entityType.getProperty(propertyName);
			return property instanceof EdmProperty && isKeyProperty(property) && !isNullable((EdmAnnotatable) property);
		}
		catch(final EdmException e)
		{
			LOG.warn("An exception occurred while determining whether a property is a non-nullable key: {}", e.getMessage());
			return false;
		}
	}
}