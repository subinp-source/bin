/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.preview.strategies.impl;

import de.hybris.platform.acceleratorcms.preview.strategies.PreviewContextInformationLoaderStrategy;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;

import org.springframework.beans.factory.annotation.Required;


public class UiExperienceLevelPreviewStrategy implements PreviewContextInformationLoaderStrategy
{

	private UiExperienceService uiExperienceService;

	@Override
	public void initContextFromPreview(final PreviewDataModel preview)
	{
		getUiExperienceService().setDetectedUiExperienceLevel(preview.getUiExperience());
	}

	public UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}
}
