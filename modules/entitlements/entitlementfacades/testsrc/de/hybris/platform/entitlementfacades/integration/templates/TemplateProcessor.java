/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementfacades.integration.templates;

import java.io.Writer;
import java.util.Map;


public interface TemplateProcessor
{
	void processTemplate(final Writer writer, final String templatePath, final Map<String, Object> binding);
}
