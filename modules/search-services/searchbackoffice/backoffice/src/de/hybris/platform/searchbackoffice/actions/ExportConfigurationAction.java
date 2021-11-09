/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.actions;

import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;


public class ExportConfigurationAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(ExportConfigurationAction.class);

	protected static final String EVENT_TYPE = "SnExportConfiguration";

	@Resource
	private SnContextFactory snContextFactory;

	@Resource
	private SnSearchProviderFactory snSearchProviderFactory;

	@Resource
	private NotificationService notificationService;

	@Override
	public ActionResult<Object> perform(final ActionContext<Object> context)
	{
		final Object data = context.getData();
		try
		{
			return internalPerformAction(context, data);
		}
		catch (final SnException e)
		{
			LOG.error(e.getMessage(), e);

			notificationService.notifyUser(context, EVENT_TYPE, Level.FAILURE);
			return new ActionResult<>(ActionResult.ERROR, null);
		}
	}

	protected ActionResult<Object> internalPerformAction(final ActionContext<Object> context, final Object data)
		throws SnException {
		if (data instanceof SnIndexConfigurationModel)
		{
			for (final SnIndexTypeModel indexType : ((SnIndexConfigurationModel) data).getIndexTypes())
			{
				exportConfiguration(indexType);
			}

			notificationService.notifyUser(context, EVENT_TYPE, Level.SUCCESS);
			return new ActionResult<>(ActionResult.SUCCESS, null);
		}
		else if (data instanceof SnIndexTypeModel)
		{
			exportConfiguration(((SnIndexTypeModel) data));

			notificationService.notifyUser(context, EVENT_TYPE, Level.SUCCESS);
			return new ActionResult<>(ActionResult.SUCCESS, null);
		} else {
			notificationService.notifyUser(context, EVENT_TYPE, Level.FAILURE);
			return new ActionResult<>(ActionResult.ERROR, null);
		}
	}

	protected void exportConfiguration(final SnIndexTypeModel indexType) throws SnException
	{
		final SnContext context = snContextFactory.createContext(indexType.getId());
		final SnSearchProvider<?> searchProvider = snSearchProviderFactory.getSearchProviderForContext(context);

		searchProvider.exportConfiguration(context);
	}

	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		final Object data = ctx.getData();

		return data instanceof SnIndexConfigurationModel || data instanceof SnIndexTypeModel;
	}
}
