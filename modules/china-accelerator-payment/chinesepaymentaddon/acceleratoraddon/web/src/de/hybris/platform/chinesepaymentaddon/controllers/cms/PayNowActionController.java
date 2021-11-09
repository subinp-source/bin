/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.GenericCMSAddOnComponentController;
import de.hybris.platform.chinesepaymentaddon.constants.ChinesepaymentaddonConstants;
import de.hybris.platform.chinesepaymentaddon.constants.ControllerConstants;
import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for PayNowAction
 */
@Controller("PayNowActionController")
@RequestMapping(ControllerConstants.Views.Cms.PayNowAction)
public class PayNowActionController extends GenericCMSAddOnComponentController
{

	@Resource(name = "chineseCheckoutFacade")
	private ChineseCheckoutFacade chineseCheckoutFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final AbstractCMSComponentModel component)
	{
		super.fillModel(request, model, component);
		model.addAttribute("payInNewWindow", chineseCheckoutFacade.needPayInNewWindow());
	}

	@Override
	protected String getAddonUiExtensionName(final AbstractCMSComponentModel component)
	{
		return ChinesepaymentaddonConstants.EXTENSIONNAME;
	}
}
