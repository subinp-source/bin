/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentaddon.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.chinesepaymentaddon.checkout.steps.validation.impl.ChinesePaymentCheckoutStepValidator;
import de.hybris.platform.chinesepaymentaddon.constants.ControllerConstants;
import de.hybris.platform.chinesepaymentaddon.controllers.pages.checkout.steps.imported.PaymentMethodCheckoutStepController;
import de.hybris.platform.chinesepaymentaddon.forms.ChinesePaymentMethodForm;
import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.PaymentModeService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Scope("tenant")
@RequestMapping(value = "/checkout/multi/payment-method")
public class ChinesePaymentMethodCheckoutStepController extends PaymentMethodCheckoutStepController
{
	private static final String CART_DATA = "cartData";
	private static final String PAYMENT_METHOD = "payment-method";
	private static final String PAYMENT_SERVICE_EXTENSION_PREFIX = "chinesepsp";
	private static final String PAYMENT_SERVICE_EXTENSION_SUFFIX = "services";

	@Resource(name = "chineseCheckoutFacade")
	private ChineseCheckoutFacade chineseCheckoutFacade;

	@Resource(name = "chinesePaymentCheckoutStepValidator")
	private ChinesePaymentCheckoutStepValidator chinesePaymentCheckoutStepValidator;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "paymentModeService")
	private PaymentModeService paymentModeService;

	@Resource(name = "chinesePaymentServicesStrategy")
	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;

	@Override
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = PAYMENT_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getCheckoutFacade().setDeliveryModeIfAvailable();
		setupAddPaymentPage(model);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		model.addAttribute(CART_DATA, chineseCheckoutFacade.getCheckoutCart());
		return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
	}

	@Override
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(final Model model, final PaymentDetailsForm paymentDetailsForm, final BindingResult bindingResult)
			throws CMSItemNotFoundException
	{
		chinesePaymentCheckoutStepValidator.validate(paymentDetailsForm, bindingResult);
		setupAddPaymentPage(model);

		if (bindingResult.hasErrors())
		{
			final CartData cartData = chineseCheckoutFacade.getCheckoutCart();
			model.addAttribute(CART_DATA, cartData);
			GlobalMessages.addErrorMessage(model, "checkout.error.paymentethod.formentry.invalid");
			return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
		}

		final String paymentId = paymentDetailsForm.getPaymentId();
		chineseCheckoutFacade.setPaymentMode(getPaymentModeModel(paymentId));
		chineseCheckoutFacade.setPaymentInfo(paymentId);

		model.addAttribute("paymentmethod", paymentId);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		final CartData cartData = chineseCheckoutFacade.getCheckoutCart();
		model.addAttribute(CART_DATA, cartData);
		return getCheckoutStep().nextStep();
	}

	@Override
	protected void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException
	{
		super.setupAddPaymentPage(model);
		final List<PaymentModeModel> supportedPaymentMethods = paymentModeService.getAllPaymentModes();
		final List<String> extensionList = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		final List<ChinesePaymentMethodForm> paymentMethodsList = new ArrayList<>();

		// make sure payment service providers are installed
		for (final PaymentModeModel supportedPaymentMethod : supportedPaymentMethods)
		{
			for (final String extensionName : extensionList)
			{
				if (extensionName.equals(PAYMENT_SERVICE_EXTENSION_PREFIX + supportedPaymentMethod.getCode()
						+ PAYMENT_SERVICE_EXTENSION_SUFFIX))
				{
					final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy
							.getPaymentService(supportedPaymentMethod.getCode());
					final ChinesePaymentMethodForm chinesePaymentMethodForm = new ChinesePaymentMethodForm();
					chinesePaymentMethodForm.setLogoUrl(chinesePaymentService.getPspLogoUrl());
					chinesePaymentMethodForm.setCode(supportedPaymentMethod.getCode());
					chinesePaymentMethodForm.setName(supportedPaymentMethod.getName());
					paymentMethodsList.add(chinesePaymentMethodForm);
					break;
				}
			}
		}
		model.addAttribute("paymentMethodsList", paymentMethodsList);
	}

	@Override
	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(PAYMENT_METHOD);
	}

	protected PaymentModeModel getPaymentModeModel(final String selectedPaymentCode)
	{
		return paymentModeService.getPaymentModeForCode(selectedPaymentCode);
	}
}
