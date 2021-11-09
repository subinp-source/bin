/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.impl;

import de.hybris.platform.cmsfacades.navigations.service.NavigationEntryConverterRegistry;
import de.hybris.platform.cmsfacades.navigations.service.NavigationEntryItemModelConverter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the <code>NavigationEntryConverterRegistry</code>. This implementation uses
 * autowire-by-type to inject all beans implementing {@link NavigationEntryItemModelConverter}.
 *
 * @deprecated since 1811, please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryConverterRegistry implements NavigationEntryConverterRegistry, InitializingBean
{
	@Autowired
	private Set<NavigationEntryItemModelConverter> navigationEntryItemModelConverters;

	private TypeService typeService;

	private final Map<String, NavigationEntryItemModelConverter> navigationEntryItemModelConverterMap = new HashMap<>();

	@Override
	public Optional<NavigationEntryItemModelConverter> getNavigationEntryItemModelConverter(final String itemType)
	{
		if (StringUtils.isEmpty(itemType))
		{
			return Optional.empty();
		}
		final Optional<NavigationEntryItemModelConverter> navigationEntryItemModelConverter = Optional
				.ofNullable(getNavigationEntryItemModelConverterMap().get(itemType));

		if (navigationEntryItemModelConverter.isPresent())
		{
			return navigationEntryItemModelConverter;
		}
		else
		{
			final Set<String> supportedItemTypes = getNavigationEntryItemModelConverterMap().keySet();
			try
			{
				getTypeService().getComposedTypeForCode(itemType);
			}
			catch (final UnknownIdentifierException e)
			{
				return Optional.empty();
			}
			return getTypeService().getComposedTypeForCode(itemType).getAllSuperTypes().stream()
					.filter(composedType -> supportedItemTypes.contains(composedType.getCode()))
					.map(composedType -> getNavigationEntryItemModelConverterMap().get(composedType.getCode())).findFirst();
		}
	}

	@Override
	@Deprecated(since = "1811", forRemoval = true)
	public Optional<List<String>> getSupportedItemTypes()
	{
		return Optional.ofNullable(getNavigationEntryItemModelConverterMap().keySet().stream().collect(Collectors.toList()));
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		getNavigationEntryItemModelConverters()
				.forEach(entry -> getNavigationEntryItemModelConverterMap().put(entry.getItemType(), entry));
	}

	protected Set<NavigationEntryItemModelConverter> getNavigationEntryItemModelConverters()
	{
		return navigationEntryItemModelConverters;
	}

	public void setNavigationEntryItemModelConverters(
			final Set<NavigationEntryItemModelConverter> navigationEntryItemModelConverters)
	{
		this.navigationEntryItemModelConverters = navigationEntryItemModelConverters;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected Map<String, NavigationEntryItemModelConverter> getNavigationEntryItemModelConverterMap()
	{
		return navigationEntryItemModelConverterMap;
	}
}
