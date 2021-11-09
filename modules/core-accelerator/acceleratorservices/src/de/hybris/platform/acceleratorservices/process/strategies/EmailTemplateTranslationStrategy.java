/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.strategies;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;

import java.util.Map;


/**
 * 
 */
public interface EmailTemplateTranslationStrategy
{
	Map<String, Object> translateMessagesForTemplate(final RendererTemplateModel renderTemplate, final String languageIso);
}
