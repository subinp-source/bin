/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.component.renderer.impl;

import de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer;
import de.hybris.platform.acceleratorservices.util.HtmlSanitizerPolicyProvider;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.util.Config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Value;


/**
 * {@link CMSComponentRenderer} implementation handling {@link CMSParagraphComponentModel} instances.
 */
public class CMSParagraphComponentRenderer implements CMSComponentRenderer<CMSParagraphComponentModel>
{
	private static final String UNSAFE_JAVASCRIPT_ALLOWED = "cms.components.allowUnsafeJavaScript";
	
	@Override
	public void renderComponent(final PageContext pageContext, final CMSParagraphComponentModel component)
			throws ServletException, IOException
	{
		// <div class="content">${content}</div>
		final JspWriter out = pageContext.getOut();

		out.write("<div class=\"content\">");
		
		final String content = component.getContent() == null ? StringUtils.EMPTY : component.getContent();
		
		if(isUnsafeJavaScriptAllowed())
		{
				out.write(content);
		}
		else
		{
				out.write(HtmlSanitizerPolicyProvider.defaultPolicy().sanitize(content));
		}
		out.write("</div>");
	}
	
	protected boolean isUnsafeJavaScriptAllowed()
	{
		return Config.getBoolean(UNSAFE_JAVASCRIPT_ALLOWED, false);
	}
}
