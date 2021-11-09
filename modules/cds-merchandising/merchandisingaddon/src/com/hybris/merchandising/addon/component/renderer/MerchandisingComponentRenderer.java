/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.addon.component.renderer;

import java.util.Map;
import java.util.Optional;

import javax.servlet.jsp.PageContext;

import org.apache.commons.text.StringEscapeUtils;

import com.hybris.merchandising.constants.MerchandisingaddonConstants;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.strategies.ConsumedDestinationLocatorStrategy;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;


/**
 * MerchandisingComponentRenderer is a custom component renderer for Merch v2 CMS components.
 * This is intended to allow us to expose additional values to the page if required (e.g.
 * the component ID).
 *
 * @param <C> a Component which extends {@code AbstractCMSComponentModel}.
 */
public class MerchandisingComponentRenderer<C extends AbstractCMSComponentModel> extends DefaultAddOnCMSComponentRenderer<C> {
	public static final String COMPONENT_ID = "componentID";
	public static final String SERVICE_URL = "serviceUrl";
	public static final String COMPONENT_NAME = "name";
	public static final String CURRENCY_SYMBOL = "currency";

	private ConsumedDestinationLocatorStrategy consumedDestinationLocatorStrategy;
	private MerchProductDirectoryConfigService productDirectoryConfigService;
	private CommerceCommonI18NService commerceCommonI18NService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final ConsumedDestinationModel model = consumedDestinationLocatorStrategy.lookup(MerchandisingaddonConstants.STRATEGY_SERVICE);
		final Map<String, Object> variables = super.getVariablesToExpose(pageContext, component);
		model.getDestinationTarget().getDestinations()
									.stream()
									.filter(dest -> dest.getEndpoint().getId().equals(MerchandisingaddonConstants.STRATEGY_SERVICE))
									.forEach(dest -> {
										variables.put(COMPONENT_ID, component.getUid());
										variables.put(SERVICE_URL, dest.getUrl());
										variables.put(COMPONENT_NAME, component.getName());
										variables.put(CURRENCY_SYMBOL, getCurrencyInUse());
									});
		return variables;
	}
	
	/**
	 * getCurrencyInUse is a method for retrieving what currency symbol to use for the carousel. This is through looking up the current
	 * product directory in use and using the associated currency. If this is missing it defaults to the site's default currency.
	 */
	private String getCurrencyInUse()
	{
		return StringEscapeUtils.escapeHtml4(productDirectoryConfigService
			.getMerchProductDirectoryConfigForCurrentBaseSite().map(MerchProductDirectoryConfigModel :: getCurrency).map(CurrencyModel::getSymbol)
			.orElse(Optional.ofNullable(commerceCommonI18NService.getDefaultCurrency()).map(CurrencyModel :: getSymbol).orElse("")));
	}


	/**
	 * Sets the injected {@link ConsumedDestinationLocatorStrategy}.
	 * @param consumedDestinationLocatorStrategy the locator strategy to inject.
	 */
	public void setConsumedDestinationLocatorStrategy(
			ConsumedDestinationLocatorStrategy consumedDestinationLocatorStrategy) {
		this.consumedDestinationLocatorStrategy = consumedDestinationLocatorStrategy;
	}
	
	/**
	 * Sets the {@link MerchProductDirectoryConfigService} being used by the renderer.
	 * @param productDirectoryConfigService the service to use.
	 */
	public void setProductDirectoryConfigService(final MerchProductDirectoryConfigService productDirectoryConfigService)
	{
		this.productDirectoryConfigService = productDirectoryConfigService;
	}

	/**
	 * Sets the {@link CommerceCommonI18NService} being used by the renderer.
	 * @param commerceCommonI18NService the service to use.
	 */
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
}
