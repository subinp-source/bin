/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.mail;

import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import java.io.Writer;


/**
 * An interface for rendering {@link RendererTemplateModel}'s.
 *
 * @deprecated Since 4.4. User {@link de.hybris.platform.commons.renderer.Renderer} and its vlocity impl
 *             {@link de.hybris.platform.commons.renderer.impl.VelocityTemplateRenderer}
 */
@Deprecated(since = "4.4", forRemoval = true)
public interface TemplateRenderer
{

	/**
	 * Render templates via velocity by default can be overwritten to use any other templating engine.
	 *
	 * @param template
	 *           the Renderer template define in hybris
	 * @param context
	 *           A POJO holding data to populate the template.
	 * @param writer
	 *           where to write the result of rendering.
	 */
	public abstract void render(RendererTemplateModel template, Object context, Writer writer);
}
