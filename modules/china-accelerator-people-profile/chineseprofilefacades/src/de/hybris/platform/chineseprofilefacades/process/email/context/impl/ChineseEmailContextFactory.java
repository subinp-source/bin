/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofilefacades.process.email.context.impl;

import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.acceleratorservices.process.email.context.impl.DefaultEmailContextFactory;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


public class ChineseEmailContextFactory extends DefaultEmailContextFactory
{
	private static final Logger LOG = Logger.getLogger(ChineseEmailContextFactory.class);
	private static final String MEDIA = "media";

	/**
	 * Overrides processProperties from {@DefaultEmailContextFactory}.
	 *
	 * @deprecated (replaced by processProperties(final Locale locale, final AbstractCMSComponentModel component, final
	 *             Map<String, Object> componentContext))
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Override
	protected void processProperties(final AbstractCMSComponentModel component, final Map<String, Object> componentContext)
	{
		for (final String property : getCmsComponentService().getEditorProperties(component))
		{
			try
			{
				final Object value;

				if (component instanceof CMSImageComponentModel && MEDIA.equals(property))
				{

					final AbstractEmailContext context = (AbstractEmailContext) componentContext.get("parentContext");

					final Locale locale = new Locale(context.getEmailLanguage().getIsocode());

					value = getModelService().getAttributeValue(component, property, locale);

				}
				else
				{
					value = getModelService().getAttributeValue(component, property);
				}

				componentContext.put(property, value);
			}
			catch (final AttributeNotSupportedException ignore)
			{
				LOG.error("Attribute not supported exception!");
			}
		}
	}

	@Override
	protected void processProperties(final Locale locale, final AbstractCMSComponentModel component,
			final Map<String, Object> componentContext)
	{
		for (final String property : getCmsComponentService().getReadableEditorProperties(component))
		{
			try
			{
				Object value;

				if (component instanceof CMSImageComponentModel && MEDIA.equals(property))
				{

					final AbstractEmailContext context = (AbstractEmailContext) componentContext.get("parentContext");

					final Locale emailLanguageLocale = new Locale(context.getEmailLanguage().getIsocode());

					value = getModelService().getAttributeValue(component, property, emailLanguageLocale);

				}
				else
				{// this block logic is copied from @AbstractHybrisVelocityContextFactory
					value = getAttributeValue(locale, component, property);
					if (value instanceof String)
					{
						/*
						 * component content may include HTML (added, say, through a CKEditor) this content will have been
						 * HTML escaped prior to saving in DB it needs be unescaped to restore velocity placeholders
						 */
						value = StringEscapeUtils.unescapeHtml(value.toString());
					}
				}

				componentContext.put(property, value);
			}
			catch (final AttributeNotSupportedException ignore)
			{
				LOG.error("Attribute not supported exception!");
			}
		}
	}


}
