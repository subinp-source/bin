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

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isNullable;

import de.hybris.platform.odata2services.odata.persistence.exception.MissingPropertyException;

import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * @deprecated replaced by {@link de.hybris.platform.inboundservices.persistence.validation.RequiredAttributePersistenceContextValidator}
 */
@Deprecated(since = "1905.2003-CEP", forRemoval = true)
public class NullablePropertiesValidator implements CreateItemValidator
{
	@Override
	public void beforeCreateItem(final EdmEntityType entityType, final ODataEntry oDataEntry) throws EdmException
	{
		for (final String propertyName : entityType.getPropertyNames())
		{
			final EdmTyped typedProperty = entityType.getProperty(propertyName);
			if (requiredPropertyMissing(oDataEntry, propertyName, typedProperty))
			{
				throw new MissingPropertyException(entityType.getName(), propertyName);
			}
		}
	}

	private boolean requiredPropertyMissing(final ODataEntry oDataEntry, final String propertyName, final EdmTyped property) throws EdmException
	{
		return property instanceof EdmProperty &&
				!INTEGRATION_KEY_PROPERTY_NAME.equals(propertyName) &&
				!isNullable((EdmProperty)property) &&
				oDataEntry.getProperties().get(propertyName) == null;
	}
}
