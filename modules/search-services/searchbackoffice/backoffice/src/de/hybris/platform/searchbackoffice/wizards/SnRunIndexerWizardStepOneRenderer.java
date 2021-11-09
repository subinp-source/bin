/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.wizards;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;


public class SnRunIndexerWizardStepOneRenderer extends DefaultCustomViewRenderer
{
	protected static final String WIDGET_CONTEXT_KEY = "currentContext";
	protected static final String WIDGET_CONTROLLER_KEY = "widgetController";

	protected static final String INDEXER_CRON_JOBS_KEY = "snIndexerCronJobs";
	protected static final String SELECTED_INDEXER_CRON_JOB_CODE_KEY = "snSelectedIndexerCronJobCode";

	private LabelService labelService;

	@Override
	public void render(final Component component, final ViewType viewType, final Map<String, String> map, final DataType dataType,
			final WidgetInstanceManager widgetInstanceManager)
	{
		initCustomView(component, widgetInstanceManager);
	}

	protected void initCustomView(final Component component, final WidgetInstanceManager widgetInstanceManager)
	{
		// label

		final String indexerCronJobsLabel = labelService.getObjectLabel(AbstractSnIndexerCronJobModel._TYPECODE);
		final Label label = new Label(indexerCronJobsLabel);

		// cron job selection

		final List<AbstractSnIndexerCronJobModel> indexerCronJobs = (List<AbstractSnIndexerCronJobModel>) resolveWidgetContext(
				widgetInstanceManager).get(INDEXER_CRON_JOBS_KEY);

		final Combobox combobox = new Combobox();

		if (CollectionUtils.isNotEmpty(indexerCronJobs))
		{
			combobox.setModel(new ListModelList<Object>(indexerCronJobs));
		}

		combobox.addEventListener(Events.ON_CHANGE, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				final Comboitem selectedItem = combobox.getSelectedItem();
				final CronJobModel selectedCronJob = selectedItem == null ? null : selectedItem.getValue();
				final String selectedCronJobCode = selectedCronJob == null ? null : selectedCronJob.getCode();

				final ConfigurableFlowController widgetController = resolveWidgetController(widgetInstanceManager);
				widgetController.getModel().setValue(SELECTED_INDEXER_CRON_JOB_CODE_KEY, selectedCronJobCode);
				widgetController.updateNavigation();
			}
		});

		combobox.setItemRenderer((comboitem, entity, index) -> {
			final AbstractSnIndexerCronJobModel snIndexerCronJob = (AbstractSnIndexerCronJobModel) entity;
			comboitem.setLabel(getLabelService().getObjectLabel(snIndexerCronJob));
			comboitem.setValue(entity);
		});

		// layout

		final Vlayout vlayout = new Vlayout();
		vlayout.appendChild(label);
		vlayout.appendChild(combobox);

		component.appendChild(vlayout);
	}

	protected Map<String, Object> resolveWidgetContext(final WidgetInstanceManager widgetInstanceManager)
	{
		return widgetInstanceManager.getModel().getValue(WIDGET_CONTEXT_KEY, Map.class);
	}

	protected ConfigurableFlowController resolveWidgetController(final WidgetInstanceManager widgetInstanceManager)
	{
		return (ConfigurableFlowController) widgetInstanceManager.getWidgetslot().getAttribute(WIDGET_CONTROLLER_KEY);
	}

	public LabelService getLabelService()
	{
		return labelService;
	}

	@Required
	public void setLabelService(final LabelService labelService)
	{
		this.labelService = labelService;
	}
}
