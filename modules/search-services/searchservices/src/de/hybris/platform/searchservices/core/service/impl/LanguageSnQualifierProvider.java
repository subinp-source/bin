/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierProvider;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.searchservices.admin.data.SnLanguage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Required;


/**
 * Qualifier provider for languages. The available qualifiers will be created based on the configured languages for the
 * index configuration.
 *
 * <p>
 * It supports the following qualifier classes:
 * <ul>
 * <li>{@link LanguageModel}
 * <li>{@link Locale}
 * </p>
 */
public class LanguageSnQualifierProvider implements SnQualifierProvider
{
	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(LanguageModel.class, Locale.class);
	protected static final String LANGUAGE_QUALIFIERS_KEY = LanguageSnQualifierProvider.class.getName() + ".languageQualifiers";

	private CommonI18NService commonI18NService;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses()
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	public List<SnQualifier> getAvailableQualifiers(final SnContext context)
	{
		Objects.requireNonNull(context, "context is null");

		List<SnQualifier> qualifiers = (List<SnQualifier>) context.getAttributes().get(LANGUAGE_QUALIFIERS_KEY);
		if (qualifiers == null)
		{
			qualifiers = CollectionUtils.emptyIfNull(context.getIndexConfiguration().getLanguages()).stream().map(this::createQualifier)
					.collect(Collectors.toList());

			context.getAttributes().put(LANGUAGE_QUALIFIERS_KEY, qualifiers);
		}

		return qualifiers;
	}

	@Override
	public List<SnQualifier> getCurrentQualifiers(final SnContext context)
	{
		Objects.requireNonNull(context, "context is null");

		final LanguageModel language = commonI18NService.getCurrentLanguage();

		if (language == null)
		{
			return Collections.emptyList();
		}

		final List<SnQualifier> qualifiers = new ArrayList<>();
		qualifiers.add(createQualifier(language));

		final List<LanguageModel> fallbackLanguages = language.getFallbackLanguages();
		if (CollectionUtils.isNotEmpty(fallbackLanguages))
		{
			for (final LanguageModel fallbackLanguage : fallbackLanguages)
			{
				qualifiers.add(createQualifier(fallbackLanguage));
			}
		}

		return Collections.unmodifiableList(qualifiers);
	}

	protected LanguageSnQualifier createQualifier(final SnLanguage source)
	{
		final LanguageModel language = commonI18NService.getLanguage(source.getId());
		final Locale locale = commonI18NService.getLocaleForLanguage(language);
		return new LanguageSnQualifier(language, locale);
	}

	protected LanguageSnQualifier createQualifier(final LanguageModel source)
	{
		final Locale locale = commonI18NService.getLocaleForLanguage(source);
		return new LanguageSnQualifier(source, locale);
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected static class LanguageSnQualifier implements SnQualifier
	{
		private final LanguageModel language;
		private final Locale locale;

		public LanguageSnQualifier(final LanguageModel language, final Locale locale)
		{
			Objects.requireNonNull(language, "language is null");
			Objects.requireNonNull(locale, "locale is null");

			this.language = language;
			this.locale = locale;
		}

		@Override
		public String getId()
		{
			return language.getIsocode();
		}

		@Override
		public boolean canGetAs(final Class<?> qualifierClass)
		{
			for (final Class<?> supportedQualifierClass : SUPPORTED_QUALIFIER_CLASSES)
			{
				if (qualifierClass.isAssignableFrom(supportedQualifierClass))
				{
					return true;
				}
			}

			return false;
		}

		@Override
		public <Q> Q getAs(final Class<Q> qualifierClass)
		{
			Objects.requireNonNull(qualifierClass, "qualifierClass is null");

			if (qualifierClass.isAssignableFrom(LanguageModel.class))
			{
				return (Q) language;
			}
			else if (qualifierClass.isAssignableFrom(Locale.class))
			{
				return (Q) locale;
			}

			throw new IllegalArgumentException("Qualifier class not supported");
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}

			if (obj == null || this.getClass() != obj.getClass())
			{
				return false;
			}

			final LanguageSnQualifier that = (LanguageSnQualifier) obj;
			return new EqualsBuilder().append(this.language, that.language).append(this.locale, that.locale).isEquals();
		}

		@Override
		public int hashCode()
		{
			return language.hashCode();
		}
	}
}
