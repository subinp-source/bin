/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.textfieldconfiguratortemplateaddon.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ConfigureForm;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.QuoteFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.quote.data.QuoteData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.textfieldconfiguratortemplateaddon.controllers.TextFieldConfigurationValidator;
import de.hybris.platform.textfieldconfiguratortemplateaddon.forms.TextFieldConfigurationForm;
import de.hybris.platform.textfieldconfiguratortemplatefacades.TextFieldFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProductTextfieldConfiguratorController extends AbstractPageController
{

	public static final String PRODUCT_CONFIGURATOR_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/productConfiguratorPage";
	public static final String ENTRY_CONFIGURATOR_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/cartEntryConfiguratorPage";
	protected static final String ENTRY_READ_ONLY_PAGE = "addon:/textfieldconfiguratortemplateaddon/pages/readOnlyEntryConfiguratorPage";
	public static final String TEXTFIELDCONFIGURATOR_TYPE = "TEXTFIELD";
	public static final String PAGE_LABEL = "configure" + TEXTFIELDCONFIGURATOR_TYPE;
	private static final String MODEL_ATTR_DOCUMENT_CODE = "documentCode";
	private static final String MODEL_ATTR_RETURN_DOCUMENT_TYPE = "returnDocumentType";
	private static final String MODEL_ATTR_ENTRY_NUMBER = "entryNumber";

	private static final Logger LOGGER = Logger.getLogger(ProductTextfieldConfiguratorController.class);

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "textFieldFacade")
	private TextFieldFacade textFieldFacade;


	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "quoteFacade")
	private QuoteFacade quoteFacade;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "saveCartFacade")
	private SaveCartFacade saveCartFacade;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "textFieldConfigurationValidator")
	private TextFieldConfigurationValidator textFieldConfigurationValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder)
	{
		if (binder.getTarget() != null && TextFieldConfigurationForm.class.isAssignableFrom(binder.getTarget().getClass()))
		{
			binder.setValidator(getTextFieldConfigurationValidator());
		}
	}

	/**
	 * Accepts HTTP-GET requests and delegates to
	 * {@link ProductTextfieldConfiguratorController#productConfigurator(String, Model, ConfigureForm)}
	 */
	@RequestMapping(value = "/**/p/{productCode}/configuratorPage/" + TEXTFIELDCONFIGURATOR_TYPE, method =
	{ RequestMethod.GET })
	public String productConfiguratorGet(@PathVariable("productCode")
	final String encodedProductCode, final Model model, final ConfigureForm configureForm) throws CMSItemNotFoundException
	{
		return productConfigurator(encodedProductCode, model, configureForm);
	}

	/**
	 * Accepts HTTP-POST requests and delegates to
	 * {@link ProductTextfieldConfiguratorController#productConfigurator(String, Model, ConfigureForm)}
	 */
	@PostMapping(value = "/**/p/{productCode}/configuratorPage/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String productConfiguratorPost(@PathVariable("productCode")
	final String encodedProductCode, final Model model, final ConfigureForm configureForm) throws CMSItemNotFoundException
	{
		return productConfigurator(encodedProductCode, model, configureForm);
	}

	/**
	 * Renders the dynamic product textfield configuration content page for the given product.
	 *
	 * @param encodedProductCode
	 *           code of the configurable product
	 * @param model
	 *           view model
	 * @param configureForm
	 *           Form for transferring product configuration options
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	protected String productConfigurator(final String encodedProductCode, final Model model, final ConfigureForm configureForm)
			throws CMSItemNotFoundException
	{
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		storePageData(productCode, getProductFacade().getConfiguratorSettingsForCode(productCode), model);
		model.addAttribute("qty", configureForm.getQty());
		return PRODUCT_CONFIGURATOR_PAGE;
	}


	@PostMapping(value = "/**/p/{productCode}/configure/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String addToCart(@PathVariable("productCode")
	final String encodedProductCode, final Model model, @ModelAttribute("foo")
	@Valid
	final TextFieldConfigurationForm form, final BindingResult bindingErrors, final HttpServletRequest request,
			final RedirectAttributes redirectModel)
	{
		final String productCode = decodeWithScheme(encodedProductCode, UTF_8);
		validateProductConfigurations(form, productCode, bindingErrors);
		boolean err = false;
		if (bindingErrors.hasErrors())
		{
			bindingErrors.getAllErrors().forEach(error -> GlobalMessages.addFlashMessage(redirectModel,
					GlobalMessages.ERROR_MESSAGES_HOLDER, error.getCode(), error.getArguments()));
			err = true;
		}
		else
		{
			final long qty = form.getQuantity();
			try
			{
				final CartModificationData cartModification = cartFacade.addToCart(productCode, qty);
				if (cartModification == null)
				{
					throw new CommerceCartModificationException("Null cart modification");
				}
				if (cartModification.getQuantityAdded() > 0)
				{
					cartFacade.updateCartEntry(enrichOrderEntryWithConfigurationData(form, cartModification.getEntry()));
					model.addAttribute("quantity", cartModification.getQuantityAdded());
					model.addAttribute("entry", cartModification.getEntry());
				}

				if (cartModification.getQuantityAdded() == 0L)
				{
					err = true;
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				}
				else if (cartModification.getQuantityAdded() < qty)
				{
					err = true;
					GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				}
			}
			catch (final CommerceCartModificationException ex)
			{
				LOGGER.error(ex.getMessage(), ex);
				err = true;
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "basket.error.occurred");
			}
		}

		if (err)
		{
			return getConfigurePageRedirectPath(productCode);
		}
		model.addAttribute("product",
				productFacade.getProductForCodeAndOptions(productCode, Collections.singletonList(ProductOption.BASIC)));
		return REDIRECT_PREFIX + "/cart";
	}

	@GetMapping(value = "/cart/{entryNumber}/configuration/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String editConfigurationInEntry(@PathVariable(MODEL_ATTR_ENTRY_NUMBER)
	final int entryNumber, final Model model) throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final CartData cart = cartFacade.getSessionCart();
		final OrderEntryData entry = getTextFieldFacade().getAbstractOrderEntry(entryNumber, cart);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_CONFIGURATOR_PAGE;
	}

	@PostMapping(value = "/cart/{entryNumber}/configuration/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String updateConfigurationInEntry(@PathVariable(MODEL_ATTR_ENTRY_NUMBER)
	final int entryNumber, final Model model, @ModelAttribute("foo")
	@Valid
	final TextFieldConfigurationForm form, final BindingResult bindingErrors, final HttpServletRequest request,
			final RedirectAttributes redirectModel) throws CommerceCartModificationException
	{
		final CartData cart = cartFacade.getSessionCart();
		final OrderEntryData entry = getTextFieldFacade().getAbstractOrderEntry(entryNumber, cart);
		validateProductConfigurations(form, entry.getProduct().getCode(), bindingErrors);
		if (bindingErrors.hasErrors())
		{
			bindingErrors.getAllErrors().forEach(error -> GlobalMessages.addFlashMessage(redirectModel,
					GlobalMessages.ERROR_MESSAGES_HOLDER, error.getCode(), error.getArguments()));
			return REDIRECT_PREFIX + request.getServletPath();
		}
		cartFacade.updateCartEntry(enrichOrderEntryWithConfigurationData(form, entry));
		model.addAttribute("product", productFacade.getProductForCodeAndOptions(entry.getProduct().getCode(),
				Collections.singletonList(ProductOption.BASIC)));
		model.addAttribute("quantity", entry.getQuantity());
		model.addAttribute("entry", entry);
		return REDIRECT_PREFIX + "/cart";
	}

	@GetMapping(value = "/my-account/my-quotes/{quoteCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInQuoteEntry(@PathVariable("quoteCode")
	final String quoteCode, @PathVariable(MODEL_ATTR_ENTRY_NUMBER)
	final int entryNumber, final Model model) throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final QuoteData quote = quoteFacade.getQuoteForCode(quoteCode);
		final OrderEntryData entry = getTextFieldFacade().getAbstractOrderEntry(entryNumber, quote);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, quoteCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "my-quotes");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}

	@GetMapping(value = "/my-account/order/{orderCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInOrderEntry(@PathVariable("orderCode")
	final String orderCode, @PathVariable(MODEL_ATTR_ENTRY_NUMBER)
	final int entryNumber, final Model model) throws CMSItemNotFoundException, CommerceCartModificationException
	{
		final OrderData order = orderFacade.getOrderDetailsForCode(orderCode);
		final OrderEntryData entry = getTextFieldFacade().getAbstractOrderEntry(entryNumber, order);
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, orderCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "order");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}

	@GetMapping(value = "/my-account/saved-carts/{cartCode}/{entryNumber}/configurationDisplay/" + TEXTFIELDCONFIGURATOR_TYPE)
	public String displayConfigurationInSavedCartEntry(@PathVariable("cartCode")
	final String cartCode, @PathVariable(MODEL_ATTR_ENTRY_NUMBER)
	final int entryNumber, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException, CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartCode);
		final CommerceSaveCartResultData commerceSaveCartResultData = saveCartFacade.getCartForCodeAndCurrentUser(parameters);
		final OrderEntryData entry = getTextFieldFacade().getAbstractOrderEntry(entryNumber,
				commerceSaveCartResultData.getSavedCartData());
		model.addAttribute(MODEL_ATTR_ENTRY_NUMBER, entryNumber);
		model.addAttribute(MODEL_ATTR_DOCUMENT_CODE, cartCode);
		model.addAttribute(MODEL_ATTR_RETURN_DOCUMENT_TYPE, "saved-carts");
		storePageData(entry.getProduct().getCode(), entry.getConfigurationInfos(), model);
		return ENTRY_READ_ONLY_PAGE;
	}


	protected void storePageData(final String productCode, final List<ConfigurationInfoData> configuration, final Model model)
			throws CMSItemNotFoundException
	{
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, productBreadcrumbBuilder.getBreadcrumbs(productCode));
		final Set<ProductOption> options = new HashSet<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
				ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE,
				ProductOption.DELIVERY_MODE_AVAILABILITY));

		final ProductData productData = getProductFacade().getProductForCodeAndOptions(productCode, options);
		model.addAttribute("product", productData);
		model.addAttribute("pageType", PageType.PRODUCT.name());
		final ContentPageModel pageModel = getContentPageForLabelOrId(PAGE_LABEL);
		storeCmsPageInModel(model, pageModel);
		model.addAttribute("configurations", configuration);
	}



	protected TextFieldFacade getTextFieldFacade()
	{
		return textFieldFacade;
	}

	protected OrderEntryData enrichOrderEntryWithConfigurationData(final TextFieldConfigurationForm form,
			final OrderEntryData orderEntryData)
	{
		final List<ConfigurationInfoData> configurationInfoDataList = new ArrayList<>();
		if (form != null && form.getConfigurationsKeyValueMap() != null)
		{
			for (final Map.Entry<ConfiguratorType, Map<String, String>> item : form.getConfigurationsKeyValueMap().entrySet())
			{
				item.getValue().entrySet().stream().map(formEntry -> {
					final ConfigurationInfoData configurationInfoData = new ConfigurationInfoData();
					configurationInfoData.setConfigurationLabel(formEntry.getKey());
					configurationInfoData.setConfigurationValue(formEntry.getValue());
					configurationInfoData.setConfiguratorType(item.getKey());
					configurationInfoData.setStatus(ProductInfoStatus.SUCCESS);
					return configurationInfoData;
				}).forEach(configurationInfoDataList::add);
			}
		}
		orderEntryData.setConfigurationInfos(configurationInfoDataList);
		return orderEntryData;
	}


	protected void validateProductConfigurations(final TextFieldConfigurationForm form, final String productCode,
			final BindingResult bindingErrors)
	{
		final Map<String, String> validConfigurationLabels = getValidConfigurationLabels(productCode);
		final Map<ConfiguratorType, Map<String, String>> values = form.getConfigurationsKeyValueMap();
		if (values != null)
		{
			values.entrySet().stream().filter(entry -> entry.getKey() == ConfiguratorType.TEXTFIELD)
					.flatMap(entry -> entry.getValue().entrySet().stream()).forEach(property -> {
						if (!validConfigurationLabels.containsKey(property.getKey()))
						{
							bindingErrors.rejectValue("configurationsKeyValueMap['" + ConfiguratorType.TEXTFIELD.getCode() + "']['"
									+ property.getKey() + "']", "configuration.invalid.key", new Object[]
							{ property.getKey() }, null);
						}
					});
		}
	}

	protected Map<String, String> getValidConfigurationLabels(final String productCode)
	{
		final List<ConfigurationInfoData> configuratorSettingsForCode = getProductFacade()
				.getConfiguratorSettingsForCode(productCode);
		final Map<String, String> result = new HashMap<>();
		configuratorSettingsForCode.stream()
				.forEach(infoData -> result.put(infoData.getConfigurationLabel(), infoData.getConfigurationValue()));
		return result;
	}

	protected String getConfigurePageRedirectPath(final String productCode)
	{
		return String.format("%s/p/%s/configuratorPage/%s", REDIRECT_PREFIX, urlEncode(productCode), TEXTFIELDCONFIGURATOR_TYPE);
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	protected TextFieldConfigurationValidator getTextFieldConfigurationValidator()
	{
		return textFieldConfigurationValidator;
	}
}
