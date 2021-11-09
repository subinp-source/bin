/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.controllers.pages.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.addressfacades.address.AddressFacade;
import de.hybris.platform.chineseprofile.constants.WebConstants;
import de.hybris.platform.chineseprofile.controllers.ControllerConstants;
import de.hybris.platform.chineseprofilefacades.customer.ChineseCustomerFacade;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * This controller show verification code page.
 */
@Controller
@RequestMapping("/verification-code")
public class VerificationCodeController extends AbstractPageController
{
	@Resource(name = "chineseCustomerFacade")
	private ChineseCustomerFacade customerFacade;

	@Resource(name = "chineseAddressFacade")
	private AddressFacade chineseAddressFacade;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;


	@GetMapping
	public String getVerificationCode(final Model model)
	{

		final VerificationData data = getSessionService().getAttribute(WebConstants.VERIFICATION_CODE_FOR_MOBILE_BINDING);
		model.addAttribute("verificationData", data);

		return ControllerConstants.Views.Pages.Account.VerificationCodeMockPage;
	}

	@ResponseBody
	@GetMapping(value = "/create-code")
	public void createVerificationCode(final String mobileNumber)
	{
		final String verificationCode = customerFacade.generateVerificationCode();
		final VerificationData data = new VerificationData();
		data.setVerificationCode(verificationCode);

		data.setMobileNumber(mobileNumber);
		if (StringUtils.isBlank(mobileNumber))
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			data.setMobileNumber(customerData.getMobileNumber());
		}
		customerFacade.saveVerificationCodeInSession(data, WebConstants.VERIFICATION_CODE_FOR_MOBILE_BINDING);
		customerFacade.sendVerificationCode(data);
	}

	@ResponseBody
	@GetMapping(value = "/mobile/check")
	public String checkMobileNumber(final String mobileNumber)
	{
		if (chineseAddressFacade.isInvalidCellphone(mobileNumber))
		{
			return messageSource.getMessage("register.mobileNumber.invalid", new Object[] {},
					commerceCommonI18NService.getCurrentLocale());
		}

		if (!customerFacade.isMobileNumberUnique(mobileNumber))
		{
			return messageSource.getMessage("register.mobileNumber.registered", new Object[] {},
					commerceCommonI18NService.getCurrentLocale());
		}

		return "";
	}

}
