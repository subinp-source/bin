/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseproductsharing.controllers.pages;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.chineseproductsharing.controllers.ChineseproductsharingaddonControllerConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;


@Controller
@Scope("tenant")
@RequestMapping("/product-share")
public class ProductQrPageController extends AbstractPageController
{

	@RequestMapping(value = "/qr", method = RequestMethod.GET)
	public String getQrPageView(final Model model,final String url) throws CMSItemNotFoundException
	{
		model.addAttribute("url",url);
		
		return ChineseproductsharingaddonControllerConstants.Views.Pages.QrPage;
	}

}
