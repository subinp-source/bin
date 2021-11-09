/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookbackoffice.widgets.modals.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.webhookbackoffice.services.WebhookConfigBackofficeService;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

public final class CreateWebhookConfigurationModalController extends DefaultWidgetController
{
	private static final Logger LOG = Log.getLogger(CreateWebhookConfigurationModalController.class);

	private static final String DEFAULT_EVENTTYPE = "de.hybris.platform.webhookservices.event.ItemSavedEvent";
	private static final String OBJECTS_UPDATED_EVENT = "objectsUpdated";
	private static final String OBJECTS_UPDATED_EVENT_TYPE = "updatedObjectIsNew";
	private static final String LOGIC_LOCATION_TEMPLATE = "model://%s";
	private static final DestinationChannel DEFAULT_CHANNEL = DestinationChannel.WEBHOOKSERVICES;

	@WireVariable
	private transient ReadService readService;
	@WireVariable
	private transient WebhookConfigBackofficeService webhookConfigBackofficeService;
	@WireVariable
	private transient CockpitEventQueue cockpitEventQueue;
	@WireVariable
	private transient NotificationService notificationService;

	private Combobox integrationObjectComboBox;
	private Combobox consumedDestinationComboBox;
	private Combobox filterLocationComboBox;
	private Button createButton;

	@SocketEvent(socketId = "createWebhookConfiguration")
	public void loadCreateWebhookConfigurationModal(final String openModal)
	{
		loadIntegrationObject();
		loadConsumedDestinationForWebhook();
		loadFilterLocations();

		createButton.setDisabled(true);

		integrationObjectComboBox.setAutocomplete(false);
		integrationObjectComboBox.setAutodrop(true);
	}

	@ViewEvent(componentID = "integrationObjectComboBox", eventName = Events.ON_CHANGE)
	public void integrationObjectComboBoxOnChange()
	{
		setCreateButtonStatus();
	}
	@ViewEvent(componentID = "consumedDestinationComboBox", eventName = Events.ON_CHANGE)
	public void consumedDestinationComboBoxOnChange()
	{
		setCreateButtonStatus();
	}

	private void setCreateButtonStatus()
	{
		if (integrationObjectComboBox.getSelectedItem() != null && consumedDestinationComboBox.getSelectedItem() != null)
		{
			createButton.setDisabled(false);
			return;
		}
		createButton.setDisabled(true);
	}

	private void loadIntegrationObject()
	{
		integrationObjectComboBox.getItems().clear();
		final List<IntegrationObjectModel> unsortedIntegrationObjectModels = readService.getIntegrationObjectModels();
		unsortedIntegrationObjectModels.stream()
		                               .sorted(Comparator.comparing(IntegrationObjectModel::getCode))
		                               .forEach(iO -> integrationObjectComboBox.appendChild(createComboItem(iO.getCode(), iO)));
	}

	private void loadConsumedDestinationForWebhook()
	{
		consumedDestinationComboBox.getItems().clear();
		final List<ConsumedDestinationModel> destinationList = webhookConfigBackofficeService.getConsumedDestinationByChannel(
				DEFAULT_CHANNEL);
		destinationList.stream()
		               .sorted(Comparator.comparing(ConsumedDestinationModel::getId))
		               .forEach(destination -> consumedDestinationComboBox.appendChild(
				               createComboItem(destination.getId(), destination)));
	}

	private void loadFilterLocations()
	{
		filterLocationComboBox.getItems().clear();
		List<ScriptModel> filterLocations = webhookConfigBackofficeService.getActiveGroovyScripts();
		filterLocations.stream()
		               .sorted(Comparator.comparing(ScriptModel::getCode))
		               .forEach(filterLocation -> filterLocationComboBox.appendChild(
				               createComboItem(String.format(LOGIC_LOCATION_TEMPLATE, filterLocation.getCode()), filterLocation)));
	}

	@ViewEvent(componentID = "createButton", eventName = Events.ON_CLICK)
	public void createWebhookConfiguration()
	{
		final IntegrationObjectModel integrationObject = integrationObjectComboBox.getSelectedItem().getValue();
		final ConsumedDestinationModel consumedDestination = consumedDestinationComboBox.getSelectedItem().getValue();
		final String filterLocation = filterLocationComboBox.getSelectedItem() != null ? filterLocationComboBox.getSelectedItem().getLabel() : "";
		final List<WebhookConfigurationModel> webhookList = webhookConfigBackofficeService.getWebhookConfiguration(
				integrationObject,
				consumedDestination, DEFAULT_EVENTTYPE);
		if (webhookList != null && !webhookList.isEmpty())
		{
			Messagebox.show(getLabel("webhookbackoffice.createWebhookConfiguration.error.msg.webhookExists"),
					getLabel("webhookbackoffice.createWebhookConfiguration.error.title.webhookExists"), 1,
					Messagebox.ERROR);
			LOG.error(getLabel("webhookbackoffice.createWebhookConfiguration.error.title.webhookExists"));
		}
		else
		{
			final WebhookConfigurationModel newWebhook = webhookConfigBackofficeService.createAndPersistWebhookConfiguration(
					integrationObject, consumedDestination,
					DEFAULT_EVENTTYPE, filterLocation);
			publishEvent(newWebhook);
			sendOutput("cancel", null);
			notificationService.notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.SUCCESS,
					getLabel("webhookbackoffice.createWebhookConfiguration.info.msg.webhookConfigCreated"));
		}

	}

	private void publishEvent(final WebhookConfigurationModel webhook)
	{
		final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(OBJECTS_UPDATED_EVENT, webhook, null);
		cockpitEvent.getContext().put(OBJECTS_UPDATED_EVENT_TYPE, true);
		cockpitEventQueue.publishEvent(cockpitEvent);
	}

}
