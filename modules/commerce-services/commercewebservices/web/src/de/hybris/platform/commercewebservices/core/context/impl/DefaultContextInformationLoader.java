/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservices.core.context.impl;

import de.hybris.platform.basecommerce.strategies.ActivateBaseSiteInSessionStrategy;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import de.hybris.platform.commercewebservices.core.constants.YcommercewebservicesConstants;
import de.hybris.platform.commercewebservices.core.context.ContextInformationLoader;
import de.hybris.platform.commercewebservices.core.exceptions.RecalculationException;
import de.hybris.platform.commercewebservices.core.exceptions.UnsupportedCurrencyException;
import de.hybris.platform.commercewebservices.core.exceptions.UnsupportedLanguageException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default context information loader
 */
public class DefaultContextInformationLoader implements ContextInformationLoader
{
	private static final String[] urlSplitters = { "/v1/", "/v2/" };

	private static final Logger LOG = Logger.getLogger(DefaultContextInformationLoader.class);

	private BaseSiteService baseSiteService;
	private ActivateBaseSiteInSessionStrategy activateBaseSiteInSessionStrategy;
	private ConfigurationService configurationService;
	private Set<String> baseSiteResourceExceptions;
	private CommonI18NService commonI18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private BaseStoreService baseStoreService;
	private CartService cartService;
	private CalculationService calculationService;

	@Override
	public LanguageModel setLanguageFromRequest(final HttpServletRequest request) throws UnsupportedLanguageException
	{
		final String languageString = request.getParameter(YcommercewebservicesConstants.HTTP_REQUEST_PARAM_LANGUAGE);
		LanguageModel languageToSet = null;

		if (!StringUtils.isBlank(languageString))
		{
			try
			{
				languageToSet = getCommonI18NService().getLanguage(languageString);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new UnsupportedLanguageException("Language  " + YSanitizer.sanitize(languageString) + " is not supported", e);
			}
		}

		if (languageToSet == null)
		{
			languageToSet = getCommerceCommonI18NService().getDefaultLanguage();
		}

		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore != null)
		{
			final Collection<LanguageModel> storeLanguages = getStoresLanguages(currentBaseStore);

			if (storeLanguages.isEmpty())
			{
				throw new UnsupportedLanguageException("Current base store supports no languages!");
			}

			if (!storeLanguages.contains(languageToSet))
			{
				throw new UnsupportedLanguageException(languageToSet);
			}
		}


		if (languageToSet != null && !languageToSet.equals(getCommerceCommonI18NService().getCurrentLanguage()))
		{
			getCommerceCommonI18NService().setCurrentLanguage(languageToSet);

			if (LOG.isDebugEnabled())
			{
				LOG.debug(languageToSet + " set as current language");
			}
		}
		return languageToSet;
	}


	protected Collection<LanguageModel> getStoresLanguages(final BaseStoreModel currentBaseStore)
	{
		if (currentBaseStore == null)
		{
			throw new IllegalStateException("No current base store was set!");
		}
		return currentBaseStore.getLanguages() == null ? Collections.<LanguageModel>emptySet() : currentBaseStore.getLanguages();
	}

	@Override
	public CurrencyModel setCurrencyFromRequest(final HttpServletRequest request)
			throws UnsupportedCurrencyException, RecalculationException
	{
		final String currencyString = request.getParameter(YcommercewebservicesConstants.HTTP_REQUEST_PARAM_CURRENCY);
		CurrencyModel currencyToSet = null;

		if (!StringUtils.isBlank(currencyString))
		{
			try
			{
				currencyToSet = getCommonI18NService().getCurrency(currencyString);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new UnsupportedCurrencyException("Currency " + YSanitizer.sanitize(currencyString) + " is not supported", e);
			}
		}

		if (currencyToSet == null)
		{
			currencyToSet = getCommerceCommonI18NService().getDefaultCurrency();
		}

		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();

		if (currentBaseStore != null)
		{
			final List<CurrencyModel> storeCurrencies = getCommerceCommonI18NService().getAllCurrencies();

			if (storeCurrencies.isEmpty())
			{
				throw new UnsupportedCurrencyException("Current base store supports no currencies!");
			}

			if (!storeCurrencies.contains(currencyToSet))
			{
				throw new UnsupportedCurrencyException(currencyToSet);
			}
		}

		if (currencyToSet != null && !currencyToSet.equals(getCommerceCommonI18NService().getCurrentCurrency()))
		{
			getCommerceCommonI18NService().setCurrentCurrency(currencyToSet);
			recalculateCart(currencyString);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(currencyToSet + " set as current currency");
			}
		}

		return currencyToSet;
	}

	/**
	 * Recalculates cart when currency has changed
	 */
	protected void recalculateCart(final String currencyString) throws RecalculationException
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			if (cart != null)
			{
				try
				{
					getCalculationService().recalculate(cart);
				}
				catch (final CalculationException e)
				{
					throw new RecalculationException(e, YSanitizer.sanitize(currencyString));
				}
			}
		}
	}


 
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public Set<String> getBaseSiteResourceExceptions()
	{
		return baseSiteResourceExceptions;
	}

	@Required
	public void setBaseSiteResourceExceptions(final Set<String> baseSiteResourceExceptions)
	{
		this.baseSiteResourceExceptions = baseSiteResourceExceptions;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public ActivateBaseSiteInSessionStrategy getActivateBaseSiteInSessionStrategy()
	{
		return activateBaseSiteInSessionStrategy;
	}

	@Required
	public void setActivateBaseSiteInSessionStrategy(final ActivateBaseSiteInSessionStrategy activateBaseSiteInSessionStrategy)
	{
		this.activateBaseSiteInSessionStrategy = activateBaseSiteInSessionStrategy;
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

	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	public CalculationService getCalculationService()
	{
		return calculationService;
	}

	@Required
	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}
}
