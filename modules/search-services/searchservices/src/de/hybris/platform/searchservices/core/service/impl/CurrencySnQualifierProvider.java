/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierProvider;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.searchservices.admin.data.SnCurrency;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Required;


/**
 * Qualifier provider for currencies. The available qualifiers will be created based on the configured currencies for
 * the index configuration.
 *
 * <p>
 * It supports the following types:
 * <ul>
 * <li>{@link CurrencyModel}
 * </p>
 *
 */
public class CurrencySnQualifierProvider implements SnQualifierProvider
{
	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(CurrencyModel.class);
	protected static final String CURRENCY_QUALIFIERS_KEY = CurrencySnQualifierProvider.class.getName() + ".currencyQualifiers";

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

		List<SnQualifier> qualifiers = (List<SnQualifier>) context.getAttributes().get(CURRENCY_QUALIFIERS_KEY);
		if (qualifiers == null)
		{
			qualifiers = CollectionUtils.emptyIfNull(context.getIndexConfiguration().getCurrencies()).stream().map(this::createQualifier)
					.collect(Collectors.toList());

			context.getAttributes().put(CURRENCY_QUALIFIERS_KEY, qualifiers);
		}

		return qualifiers;
	}

	@Override
	public List<SnQualifier> getCurrentQualifiers(final SnContext context)
	{
		Objects.requireNonNull(context, "context is null");

		final CurrencyModel currency = commonI18NService.getCurrentCurrency();

		if (currency == null)
		{
			return Collections.emptyList();
		}

		return List.of(createQualifier(currency));
	}

	protected CurrencySnQualifier createQualifier(final SnCurrency source)
	{
		final CurrencyModel currency = commonI18NService.getCurrency(source.getId());
		return new CurrencySnQualifier(currency);
	}

	protected CurrencySnQualifier createQualifier(final CurrencyModel source)
	{
		return new CurrencySnQualifier(source);
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

	protected static class CurrencySnQualifier implements SnQualifier
	{
		private final CurrencyModel currency;

		public CurrencySnQualifier(final CurrencyModel currency)
		{
			Objects.requireNonNull(currency, "currency is null");

			this.currency = currency;
		}

		public CurrencyModel getCurrency()
		{
			return currency;
		}

		@Override
		public String getId()
		{
			return currency.getIsocode();
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

			if (qualifierClass.isAssignableFrom(CurrencyModel.class))
			{
				return (Q) currency;
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

			final CurrencySnQualifier that = (CurrencySnQualifier) obj;
			return new EqualsBuilder().append(this.currency, that.currency).isEquals();
		}

		@Override
		public int hashCode()
		{
			return currency.hashCode();
		}
	}
}
