/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.i18n.facade.impl;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static de.hybris.platform.smarteditwebservices.configuration.facade.DefaultConfigurationKey.DEFAULT_LANGUAGE;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.forLanguageTag;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.remove;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.smarteditwebservices.configuration.facade.SmarteditConfigurationFacade;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;
import de.hybris.platform.smarteditwebservices.i18n.facade.SmarteditI18nFacade;
import de.hybris.platform.util.localization.TypeLocalization;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SmarteditI18nFacade} to retrieve cms management data
 */
public class DefaultSmarteditI18nFacade implements SmarteditI18nFacade, InitializingBean
{
	private L10NService l10nService;
	private I18NService i18nService;
	private CommonI18NService commonI18NService;
	private AbstractPopulatingConverter<Locale, SmarteditLanguageData> smarteditLanguageConverter;
	private SmarteditConfigurationFacade smarteditConfigurationFacade;
	private Comparator<SmarteditLanguageData> smarteditLanguageDataComparator;
	private TenantListener tenantListener;
	private Tenant currentTenant;

	@Override
	public void afterPropertiesSet()
	{
		currentTenant = getCurrentTenant();
		tenantListener = new TenantListener()
		{
			@Override
			public void afterTenantStartUp(final Tenant tenant)
			{
				if (isSystemInitialized() && currentTenant.equals(tenant))
				{
					initializeLocalizations();
				}
			}

			@Override
			public void beforeTenantShutDown(final Tenant tenant)
			{
				// NOOP
			}

			@Override
			public void afterSetActivateSession(final Tenant tenant)
			{
				// NOOP
			}

			@Override
			public void beforeUnsetActivateSession(final Tenant tenant)
			{
				// NOOP
			}
		};
		Registry.registerTenantListener(tenantListener);
	}

	/**
	 * Cache localizations list to improve performance.
	 */
	protected void initializeLocalizations()
	{
		TypeLocalization.getInstance().getLocalizations();
	}

	@Override
	public Map<String, String> getTranslationMap(final Locale locale)
	{
		final Optional<ConfigurationData> configurationData = getSmarteditConfigurationFacade()
				.tryAndFindByDefaultConfigurationKey(DEFAULT_LANGUAGE);
		final Locale defaultLanguage = configurationData.isPresent()
				? forLanguageTag(remove(configurationData.get().getValue(), '"'))
				: ENGLISH;
		final Map<String, String> defaultMap = resolveLanguageMap(defaultLanguage);

		if (locale == null || !languageIsSupported(locale))
		{
			return defaultMap;
		}

		final Map<String, String> languageMap = resolveLanguageMap(locale);
		final Map<String, String> aggregatedLanguageMap = Objects.isNull(defaultMap) ? newHashMap() : defaultMap;
		safePutAll(aggregatedLanguageMap, languageMap);
		return aggregatedLanguageMap;
	}

	/**
	 * Checks the supported {@link Locale} languages against the {@link Locale} language
	 *
	 * @param locale
	 *           {@link Locale} to check
	 * @return is the language of the {@link Locale} locale supported
	 */
	protected boolean languageIsSupported(final Locale locale)
	{
		return getI18nService().getSupportedLocales().parallelStream()
				.anyMatch(supportedLocale -> StringUtils.equals(supportedLocale.getLanguage(), locale.getLanguage()));
	}

	/**
	 * Will put the entries of the sourceMap into the targetMap if the {@link java.util.Map.Entry} of the sourceMap has a
	 * value that is not null, empty, ot blank
	 *
	 * @param targetMap
	 *           The {@link Map} to be populated
	 * @param sourceMap
	 *           The {@link Map} to be populated from
	 */
	protected void safePutAll(final Map<String, String> targetMap, final Map<String, String> sourceMap)
	{
		sourceMap.keySet().parallelStream().filter(key -> isNotBlank(sourceMap.get(key)))
				.forEach(key -> targetMap.put(key, sourceMap.get(key)));
	}

	@Override
	public List<SmarteditLanguageData> getSupportedLanguages()
	{
		final List<SmarteditLanguageData> languageData = ofNullable(getI18nService().getSupportedLocales()).orElse(newHashSet())
				.stream().map(locale -> getSmarteditLanguageConverter().convert(locale))
				.sorted((left, right) -> getSmarteditLanguageDataComparator().compare(left, right)).collect(Collectors.toList());

		return languageData;
	}

	protected Map<String, String> resolveLanguageMap(final Locale locale)
	{
		final ResourceBundle resourceBundle = getL10nService().getResourceBundle(getI18nService().getAllLocales(locale));
		return resourceBundle.keySet().stream().collect(toMap(key -> key, resourceBundle::getString));
	}

	/**
	 * Converts a languageData.isocode to hybris format (i.e. de-CH)
	 *
	 * @param languageDataList
	 *           List of language Data
	 * @deprecated since 1808
	 */
	@Deprecated(since = "1808", forRemoval = true)
	protected void convertLocalToIsoCodes(final List<SmarteditLanguageData> languageDataList)
	{
		languageDataList.stream().forEach(languageData -> {
			final Locale locale = getCommonI18NService().getLocaleForIsoCode(languageData.getIsoCode());
			languageData.setIsoCode(locale.toLanguageTag());
		});
	}

	protected L10NService getL10nService()
	{
		return l10nService;
	}

	@Required
	public void setL10nService(final L10NService l10nService)
	{
		this.l10nService = l10nService;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected AbstractPopulatingConverter<Locale, SmarteditLanguageData> getSmarteditLanguageConverter()
	{
		return smarteditLanguageConverter;
	}

	@Required
	public void setSmarteditLanguageConverter(
			final AbstractPopulatingConverter<Locale, SmarteditLanguageData> smarteditLanguageConverter)
	{
		this.smarteditLanguageConverter = smarteditLanguageConverter;
	}

	protected SmarteditConfigurationFacade getSmarteditConfigurationFacade()
	{
		return smarteditConfigurationFacade;
	}

	@Required
	public void setSmarteditConfigurationFacade(final SmarteditConfigurationFacade smarteditConfigurationFacade)
	{
		this.smarteditConfigurationFacade = smarteditConfigurationFacade;
	}

	public Comparator<SmarteditLanguageData> getSmarteditLanguageDataComparator()
	{
		return smarteditLanguageDataComparator;
	}

	@Required
	public void setSmarteditLanguageDataComparator(final Comparator<SmarteditLanguageData> smarteditLanguageDataComparator)
	{
		this.smarteditLanguageDataComparator = smarteditLanguageDataComparator;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected TenantListener getTenantListener()
	{
		return tenantListener;
	}

	protected Tenant getCurrentTenant()
	{
		return Registry.getCurrentTenantNoFallback();
	}

	protected boolean isSystemInitialized()
	{
		return Registry.getCurrentTenantNoFallback().getJaloConnection().isSystemInitialized();
	}
}
