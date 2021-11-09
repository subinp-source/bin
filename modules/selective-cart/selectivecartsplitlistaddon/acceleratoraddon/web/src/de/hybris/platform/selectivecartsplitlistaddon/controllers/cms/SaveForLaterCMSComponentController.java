/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartsplitlistaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.selectivecartfacades.SelectiveCartFacade;
import de.hybris.platform.selectivecartsplitlistaddon.controllers.SelectivecartsplitlistaddonControllerConstants;
import de.hybris.platform.selectivecartsplitlistaddon.model.components.SaveForLaterCMSComponentModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller for CMS SaveForLaterComponent.
 */
@Controller("SaveForLaterCMSComponentController")
@RequestMapping(SelectivecartsplitlistaddonControllerConstants.Actions.Cms.SaveForLaterComponent)
public class SaveForLaterCMSComponentController extends AbstractCMSAddOnComponentController<SaveForLaterCMSComponentModel>
{

	@Resource
	private SelectiveCartFacade selectiveCartFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final SaveForLaterCMSComponentModel component)
	{
		model.addAttribute("wishlist2Data", getSelectiveCartFacade().getWishlistForSelectiveCart());
	}

	protected SelectiveCartFacade getSelectiveCartFacade()
	{
		return selectiveCartFacade;
	}

}