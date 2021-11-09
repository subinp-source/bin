/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addons.xyformssamples.controllers.pages;

import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Controller for yforms demo page.
 */

@Controller
@RequestMapping(value = "/forms/demo")
public class YFormsDemoPageController extends AbstractAddOnPageController
{
	private static final String FORMS_DEMO_CMS_PAGE_LABEL = "demoId";

	// Method to get the form
	@RequestMapping(method = RequestMethod.GET)
	public String getStoreFinderPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getPage());
		return getViewForPage(model);
	}

	protected AbstractPageModel getPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(FORMS_DEMO_CMS_PAGE_LABEL);
	}
}
