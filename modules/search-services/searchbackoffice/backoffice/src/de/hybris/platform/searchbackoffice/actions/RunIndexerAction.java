/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.actions;

import de.hybris.platform.searchservices.model.SnIndexTypeModel;

import java.util.HashMap;
import java.util.Map;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;


public class RunIndexerAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<SnIndexTypeModel, Object>
{
	protected static final String INDEXER_CRON_JOBS_KEY = "snIndexerCronJobs";

	protected static final String OUTPUT_SOCKET = "indexerOperationContext";

	@Override
	public ActionResult<Object> perform(final ActionContext<SnIndexTypeModel> ctx)
	{
		final Map<Object, Object> parameters = new HashMap<>();
		parameters.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), "sn-run-indexer");
		parameters.put(INDEXER_CRON_JOBS_KEY, ctx.getData().getIndexerCronJobs());

		sendOutput(OUTPUT_SOCKET, parameters);

		return new ActionResult<>(ActionResult.SUCCESS, null);
	}
}
