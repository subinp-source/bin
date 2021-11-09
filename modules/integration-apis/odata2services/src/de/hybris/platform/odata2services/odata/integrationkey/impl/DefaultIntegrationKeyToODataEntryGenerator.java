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
package de.hybris.platform.odata2services.odata.integrationkey.impl;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROP_DIV;
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_TYPE_DIV;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.getAliasTextIfPresent;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isKeyProperty;

import static java.lang.Long.parseLong;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator;
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidIntegrationKeyException;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmType;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.core.edm.EdmBoolean;
import org.apache.olingo.odata2.core.edm.EdmByte;
import org.apache.olingo.odata2.core.edm.EdmDateTime;
import org.apache.olingo.odata2.core.edm.EdmDateTimeOffset;
import org.apache.olingo.odata2.core.edm.EdmDecimal;
import org.apache.olingo.odata2.core.edm.EdmDouble;
import org.apache.olingo.odata2.core.edm.EdmGuid;
import org.apache.olingo.odata2.core.edm.EdmInt16;
import org.apache.olingo.odata2.core.edm.EdmInt32;
import org.apache.olingo.odata2.core.edm.EdmInt64;
import org.apache.olingo.odata2.core.edm.EdmString;
import org.apache.olingo.odata2.core.edm.EdmTime;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class DefaultIntegrationKeyToODataEntryGenerator implements IntegrationKeyToODataEntryGenerator
{
	private static final Logger LOG = Log.getLogger(DefaultIntegrationKeyToODataEntryGenerator.class);
	private static final String ILLEGAL_ARG_MSG_PREFIX = "Cannot calculate ODataEntry for null ";
	private static final String EDM_ENTITY_SET_CANNOT_BE_NULL = ILLEGAL_ARG_MSG_PREFIX + "edm entity set";
	private static final String INTEGRATION_KEY_CANNOT_BE_NULL = ILLEGAL_ARG_MSG_PREFIX + "integrationKey";
	private static final String KEY_PREDICATES_CANNOT_BE_NULL_OR_EMPTY = ILLEGAL_ARG_MSG_PREFIX + "or empty key predicates";

	private String encoding;

	private final Map<Class<? extends EdmType>, Function<String, Object>> propertyConverters = Maps.newHashMap();

	public DefaultIntegrationKeyToODataEntryGenerator()
	{
		propertyConverters.put(EdmDateTime.class, v -> DateUtils.toCalendar(new Date(parseLong(v))));
		propertyConverters.put(EdmDateTimeOffset.class, v -> DateUtils.toCalendar(new Date(parseLong(v))));
		propertyConverters.put(EdmTime.class, v -> DateUtils.toCalendar(new Date(parseLong(v))));
		propertyConverters.put(EdmBoolean.class, Boolean::valueOf);
		propertyConverters.put(EdmByte.class, Byte::valueOf);
		propertyConverters.put(EdmGuid.class, UUID::fromString);
		propertyConverters.put(EdmDouble.class, Double::valueOf);
		propertyConverters.put(EdmDecimal.class, Double::valueOf);
		propertyConverters.put(EdmInt16.class, Long::valueOf);
		propertyConverters.put(EdmInt32.class, Long::valueOf);
		propertyConverters.put(EdmInt64.class, Long::valueOf);
		propertyConverters.put(EdmString.class, v -> v);
	}

	@Override
	public ODataEntry generate(final EdmEntitySet entitySet, final String integrationKey) throws EdmException
	{
		Preconditions.checkArgument(entitySet != null, EDM_ENTITY_SET_CANNOT_BE_NULL);
		Preconditions.checkArgument(integrationKey != null, INTEGRATION_KEY_CANNOT_BE_NULL);

		final Map<String, String> aliasValueMap = getKeyValuePerType(entitySet, integrationKey);
		final ODataEntry entry = populateODataEntry(entitySet, aliasValueMap);
		entry.getProperties().put(INTEGRATION_KEY_PROPERTY_NAME, integrationKey);
		return entry;
	}

	@Override
	public ODataEntry generate(final EdmEntitySet entitySet, final List<KeyPredicate> keyPredicates) throws EdmException
	{
		Preconditions.checkArgument(keyPredicates != null && !keyPredicates.isEmpty(), KEY_PREDICATES_CANNOT_BE_NULL_OR_EMPTY);

		return generate(entitySet, getIntegrationKey(entitySet, keyPredicates));
	}

	protected String getIntegrationKey(final EdmEntitySet edmEntitySet, final List<KeyPredicate> keyPredicates) throws EdmException
	{
		if (keyPredicates.size() == 1 && INTEGRATION_KEY_PROPERTY_NAME.equals(keyPredicates.get(0).getProperty().getName()))
		{
			return keyPredicates.get(0).getLiteral();
		}
		throw new InvalidIntegrationKeyException(edmEntitySet.getEntityType().getName());
	}

	protected Map<String, String> getKeyValuePerType(final EdmEntitySet entitySet, final String integrationKey) throws EdmException
	{
		final String[] integrationKeyValues = getKeyParts(integrationKey);
		final String[] integrationKeyAliasParts = getKeyParts(getAliasText(entitySet));

		if (integrationKeyValues.length != integrationKeyAliasParts.length)
		{
			throw new InvalidIntegrationKeyException(integrationKey, entitySet.getEntityType().getName());
		}

		final Map<String, String> keyValuePerType = Maps.newHashMap();
		for (int i = 0; i < integrationKeyValues.length; i++)
		{
			keyValuePerType.put(integrationKeyAliasParts[i], decodeValue(integrationKeyValues[i]));
		}
		return keyValuePerType;
	}

	protected String decodeValue(final String value)
	{
		try
		{
			return URLDecoder.decode(value, getEncoding());
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.warn("Value [{}] was not able to be decoded.", value, e);
			return value;
		}
	}

	protected ODataEntry populateODataEntry(final EdmEntitySet entitySet, final Map<String, String> aliasValueMap) throws EdmException
	{
		final EdmEntityType entityType = entitySet.getEntityType();
		final ODataEntry entry = entryWithSimpleKeyProperties(aliasValueMap, entityType);
		for (final String property : entityType.getNavigationPropertyNames())
		{
			final EdmNavigationProperty navigationProperty = (EdmNavigationProperty)entityType.getProperty(property);
			final EdmEntitySet navigationPropertyEntitySet = entitySet.getRelatedEntitySet(navigationProperty);
			if (isKeyProperty(navigationProperty) && !navigationPropertyIsNull(navigationPropertyEntitySet, aliasValueMap))
			{
				final ODataEntry navigationPropertyEntry = populateODataEntry(navigationPropertyEntitySet, aliasValueMap);
				entry.getProperties().put(property, navigationPropertyEntry);
			}
		}
		return entry;
	}

	private ODataEntry entryWithSimpleKeyProperties(final Map<String, String> keyValuePerType,
	                                                final EdmEntityType entityType) throws EdmException
	{
		final ODataEntry entry = new ODataEntryImpl(Maps.newHashMap(), null, null, null);
		for(final String propertyName: entityType.getPropertyNames()) {
			final String value = keyValuePerType.get(alias(entityType, propertyName));
			if(value != null) {
				entry.getProperties().put(propertyName, getODataEntryProperty((EdmProperty) entityType.getProperty(propertyName), value));
			}
		}
		return entry;
	}

	private boolean navigationPropertyIsNull(final EdmEntitySet navigationPropertyEntitySet,
	                                         final Map<String, String> keyValuePerType) throws EdmException
	{
		final List<String> aliasParts = Arrays.asList(getKeyParts(getAliasText(navigationPropertyEntitySet)));
		final Optional<String> nonNullValue = aliasParts.stream().filter(part -> !"null".equals(keyValuePerType.get(part))).findFirst();
		return nonNullValue.isEmpty();
	}

	private String[] getKeyParts(final String key)
	{
		return key.split(Pattern.quote(INTEGRATION_KEY_PROP_DIV));
	}

	private static String alias(final EdmEntityType entityType, final String propertyName) throws EdmException
	{
		return entityType.getName() + INTEGRATION_KEY_TYPE_DIV + propertyName;
	}

	private static String getAliasText(final EdmEntitySet entitySet) throws EdmException
	{
		final EdmEntityType entityType = entitySet.getEntityType();
		final String aliasText = getAliasTextIfPresent(entityType);
		if(aliasText.isEmpty()) {
			throw new MissingKeyException(entityType.getName());
		}
		return aliasText;
	}

	protected Object getODataEntryProperty(final EdmProperty property, final String value) throws EdmException
	{
		final Function<String, Object> function = getPropertyConverters().getOrDefault(property.getType().getClass(), v -> v);
		return function.apply(value);
	}

	protected Map<Class<? extends EdmType>, Function<String, Object>> getPropertyConverters()
	{
		return propertyConverters;
	}

	protected String getEncoding()
	{
		return encoding;
	}

	@Required
	public void setEncoding(final String encoding)
	{
		this.encoding = encoding;
	}
}
