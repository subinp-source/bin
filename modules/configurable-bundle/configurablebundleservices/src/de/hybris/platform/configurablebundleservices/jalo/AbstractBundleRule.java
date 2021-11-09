/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.Date;
import java.util.Objects;

import org.apache.log4j.Logger;


public abstract class AbstractBundleRule extends GeneratedAbstractBundleRule
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AbstractBundleRule.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final AbstractBundleRule item = (AbstractBundleRule) super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		item.markBundleTemplateModified();
		// and return the item
		return item;
	}

	@Override
	public void remove(final SessionContext ctx) throws ConsistencyCheckException
	{
		markBundleTemplateModified();
		super.remove(ctx);
	}

	@Override
	public Object setProperty(final SessionContext ctx, final String name, final Object value)
	{
		final Object prev = super.setProperty(ctx, name, value);
		if (!Objects.equals(prev, value))
		{
			markBundleTemplateModified();

			if (prev instanceof BundleTemplate)
			{
				markBundleTemplateModified((BundleTemplate) prev);
			}
		}

		return prev;
	}

	protected void markBundleTemplateModified()
	{
		if (Boolean.FALSE.equals(getSession().getSessionContext().getAttribute("bundletemplate.mark.modified")))
		{
			return;
		}
		markBundleTemplateModified(getBundleTemplateOfRule());
	}

	protected void markBundleTemplateModified(final BundleTemplate bundleTemplate)
	{
		if ((bundleTemplate == null) || (isCurrentlyRemoving(bundleTemplate)))
		{
			return;
		}
		bundleTemplate.setModificationTime(new Date());
	}

	abstract public BundleTemplate getBundleTemplateOfRule();

}
