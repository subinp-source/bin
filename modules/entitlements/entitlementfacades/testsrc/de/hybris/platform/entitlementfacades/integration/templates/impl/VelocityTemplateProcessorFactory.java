/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementfacades.integration.templates.impl;

import de.hybris.platform.entitlementfacades.integration.templates.TemplateProcessor;
import de.hybris.platform.entitlementfacades.integration.templates.TemplateProcessorFactory;


public class VelocityTemplateProcessorFactory implements TemplateProcessorFactory
{
	@Override
	public TemplateProcessor createTemplateProcessor()
	{
		return new VelocityTemplateProcessor();
	}
}
