/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceaddon.controllers.cms;

import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;
import de.hybris.platform.marketplaceaddon.controllers.MarketplaceaddonControllerConstants;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("RotatingImagesComponentController")
@RequestMapping(value = MarketplaceaddonControllerConstants.Actions.Cms.RotatingImagesComponent)
public class RotatingImagesComponentController extends GenericMarketplaceCMSComponentController<RotatingImagesComponentModel>
{
	// Just specify the model type here, @see GenericMarketplaceCMSComponentController for business logic
}
