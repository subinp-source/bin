/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.converter.populator;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.admin.data.SnCatalogVersion;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.service.SnFieldFactory;
import de.hybris.platform.searchservices.model.SnFieldModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.util.ConverterUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link SnIndexType} from {@link SnIndexTypeModel}.
 */
public class SnIndexTypePopulator implements Populator<SnIndexTypeModel, SnIndexType>
{
	private I18NService i18NService;
	private Converter<CatalogVersionModel, SnCatalogVersion> snCatalogVersionConverter;
	private SnFieldFactory snFieldFactory;
	private Converter<SnFieldModel, SnField> snFieldConverter;

	@Override
	public void populate(final SnIndexTypeModel source, final SnIndexType target)
	{
		target.setId(source.getId());
		target.setName(buildLocalizedName(source));

		final ComposedTypeModel itemComposedType = source.getItemComposedType();
		if (itemComposedType != null)
		{
			target.setItemComposedType(itemComposedType.getCode());
		}

		target.setIdentityProvider(source.getIdentityProvider());
		target.setIdentityProviderParameters(source.getIdentityProviderParameters());
		target.setDefaultValueProvider(source.getDefaultValueProvider());
		target.setDefaultValueProviderParameters(source.getDefaultValueProviderParameters());
		target.setCatalogsIds(ConverterUtils.convertAll(source.getCatalogs(), CatalogModel::getId));
		target.setCatalogVersions(ConverterUtils.convertAll(source.getCatalogVersions(), snCatalogVersionConverter::convert));

		final List<String> listeners = source.getListeners();
		if (CollectionUtils.isNotEmpty(listeners))
		{
			target.setListeners(new ArrayList<>(listeners));
		}

		final SnIndexConfigurationModel indexConfiguration = source.getIndexConfiguration();
		if (indexConfiguration != null)
		{
			target.setIndexConfigurationId(source.getIndexConfiguration().getId());
		}

		target.setFields(buildFields(source, target));
	}

	protected Map<Locale, String> buildLocalizedName(final SnIndexTypeModel source)
	{
		final Set<Locale> supportedLocales = i18NService.getSupportedLocales();
		final Map<Locale, String> target = new LinkedHashMap<>();

		for (final Locale locale : supportedLocales)
		{
			target.put(locale, source.getName(locale));
		}

		return target;
	}

	protected final Map<String, SnField> buildFields(final SnIndexTypeModel source, final SnIndexType target)
	{
		final Map<String, SnField> fields = new HashMap<>();

		final List<SnField> defaultFields = snFieldFactory.getDefaultFields(target);
		if (CollectionUtils.isNotEmpty(defaultFields))
		{
			for (final SnField field : defaultFields)
			{
				fields.put(field.getId(), field);
			}
		}

		final List<SnFieldModel> sourceFields = source.getFields();
		if (CollectionUtils.isNotEmpty(sourceFields))
		{
			for (final SnFieldModel field : sourceFields)
			{
				fields.put(field.getId(), snFieldConverter.convert(field));
			}
		}

		return fields;
	}

	public I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	public Converter<CatalogVersionModel, SnCatalogVersion> getSnCatalogVersionConverter()
	{
		return snCatalogVersionConverter;
	}

	@Required
	public void setSnCatalogVersionConverter(final Converter<CatalogVersionModel, SnCatalogVersion> snCatalogVersionConverter)
	{
		this.snCatalogVersionConverter = snCatalogVersionConverter;
	}

	public SnFieldFactory getSnFieldFactory()
	{
		return snFieldFactory;
	}

	@Required
	public void setSnFieldFactory(final SnFieldFactory snFieldFactory)
	{
		this.snFieldFactory = snFieldFactory;
	}

	public Converter<SnFieldModel, SnField> getSnFieldConverter()
	{
		return snFieldConverter;
	}

	@Required
	public void setSnFieldConverter(final Converter<SnFieldModel, SnField> snFieldConverter)
	{
		this.snFieldConverter = snFieldConverter;
	}
}
