/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementfacades.integration.templates.impl;


import de.hybris.platform.entitlementfacades.integration.templates.TemplateProcessor;

import java.io.Writer;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


public class VelocityTemplateProcessor implements TemplateProcessor
{
	protected final VelocityEngine velocityEngine;

	public VelocityTemplateProcessor()
	{
		velocityEngine = new VelocityEngine();
		initializeVelocityEninge();
	}

	protected final void initializeVelocityEninge()
	{
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init();
	}

	protected VelocityContext initializeVelocityContext(final Map<String, Object> binding)
	{
		final VelocityContext velocytyContext = new VelocityContext();

		for (final Map.Entry<String, Object> entry : binding.entrySet())
		{
			velocytyContext.put(entry.getKey(), entry.getValue());
		}

		return velocytyContext;
	}

	@Override
	public void processTemplate(final Writer writer, final String templatPath, final Map<String, Object> binding)
	{
		final VelocityContext velocityContext = initializeVelocityContext(binding);

		final Template template = velocityEngine.getTemplate(templatPath);
		template.merge(velocityContext, writer);
	}
}
