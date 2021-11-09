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

package de.hybris.platform.integrationservices.config;

import de.hybris.platform.integrationservices.search.ItemTypeMatch;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * Encapsulates the extension configuration properties
 */
public class DefaultIntegrationServicesConfiguration extends BaseIntegrationServicesConfiguration
		implements IntegrationServicesConfiguration, ReadOnlyAttributesConfiguration, ItemSearchConfiguration
{
	private static final Logger LOG = Log.getLogger(DefaultIntegrationServicesConfiguration.class);

	private static final String SAP_PASSPORT_SYSTEM_ID_PROPERTY_KEY = "integrationservices.sap.passport.systemid";
	private static final String DEFAULT_SAP_PASSPORT_SYSTEM_ID = "SAP Commerce";

	private static final String SAP_PASSPORT_SERVICE_PROPERTY_KEY = "integrationservices.sap.passport.service";
	private static final int DEFAULT_SAP_PASSPORT_SERVICE = 39;

	private static final String SAP_PASSPORT_USER_PROPERTY_KEY = "integrationservices.sap.passport.user";
	private static final String DEFAULT_SAP_PASSPORT_USER = "";

	private static final String MEDIA_PERSISTENCE_MEDIA_NAME_PREFIX_PROPERTY_KEY = "integrationservices.media.persistence.media.name.prefix";
	private static final String DEFAULT_PREFIX_VALUE = "Payload_";

	private static final String READ_ONLY_ATTRIBUTES = "integrationservices.global.attributes.read-only";

	private static final String AUTHORIZATION_ACCESS_RIGHTS = "integrationservices.authorization.accessrights.enabled";
	private static final boolean AUTHORIZATION_ACCESS_RIGHTS_DEFAULT = false;

	private ItemTypeMatch itemTypeMatch;

	@Override
	public String getMediaPersistenceMediaNamePrefix()
	{
		return getStringProperty(MEDIA_PERSISTENCE_MEDIA_NAME_PREFIX_PROPERTY_KEY, DEFAULT_PREFIX_VALUE);
	}

	@Override
	public String getSapPassportSystemId()
	{
		return getStringProperty(SAP_PASSPORT_SYSTEM_ID_PROPERTY_KEY, DEFAULT_SAP_PASSPORT_SYSTEM_ID);
	}

	@Override
	public int getSapPassportServiceValue()
	{
		return getIntegerProperty(SAP_PASSPORT_SERVICE_PROPERTY_KEY, DEFAULT_SAP_PASSPORT_SERVICE);
	}

	@Override
	public String getSapPassportUser()
	{
		try
		{
			final String value = getConfigurationService().getConfiguration().getString(SAP_PASSPORT_USER_PROPERTY_KEY);
			if (StringUtils.isNotBlank(value))
			{
				return value;
			}
		}
		catch (final NoSuchElementException | ConversionException e)
		{
			LOG.trace(FALLBACK_MESSAGE, SAP_PASSPORT_USER_PROPERTY_KEY, "from current session user");
			LOG.trace("Exception thrown", e);
		}
		return getUserId();
	}

	@Override
	public boolean isAccessRightsEnabled()
	{
		return getBooleanProperty(AUTHORIZATION_ACCESS_RIGHTS, AUTHORIZATION_ACCESS_RIGHTS_DEFAULT);
	}

	@Override
	public Set<String> getReadOnlyAttributes()
	{
		final String readOnlyAttributes = getConfigurationService().getConfiguration().getString(READ_ONLY_ATTRIBUTES);
		if (StringUtils.isEmpty(readOnlyAttributes))
		{
			return Collections.emptySet();
		}
		else
		{
			return Set.of(readOnlyAttributes.split(","));
		}
	}

	private String getUserId()
	{
		try
		{
			return Objects.toString(getSessionUser().getUid(), DEFAULT_SAP_PASSPORT_USER);
		}
		catch (final RuntimeException e2)
		{
			LOG.warn(FALLBACK_MESSAGE, SAP_PASSPORT_USER_PROPERTY_KEY, DEFAULT_SAP_PASSPORT_USER, e2);
		}
		return DEFAULT_SAP_PASSPORT_USER;
	}

	protected User getSessionUser()
	{
		return JaloSession.getCurrentSession().getUser();
	}

	@Override
	public ItemTypeMatch getItemTypeMatch()
	{
		return itemTypeMatch != null ? itemTypeMatch : ItemTypeMatch.DEFAULT;
	}

	public void setItemTypeMatch(final ItemTypeMatch match)
	{
		itemTypeMatch = match;
	}
}
