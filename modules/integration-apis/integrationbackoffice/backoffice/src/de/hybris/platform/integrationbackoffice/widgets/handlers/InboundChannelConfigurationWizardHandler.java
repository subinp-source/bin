/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.handlers;

import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;
import de.hybris.platform.integrationbackoffice.widgets.authentication.InboundChannelConfigNotificationService;
import de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegisterIntegrationObjectDefaultService;
import de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegisterIntegrationObjectService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

/**
 * Wizard handler for the creation process of Inbound Channel Configurations. Allows for the extension of the default creation
 * wizard by handling the input of IntegrationClientCredentialsDetails to create IntegrationClientCredentialsDetails
 * and ExposedOAuthCredential.
 */
public class InboundChannelConfigurationWizardHandler implements FlowActionHandler
{
	private static final String OBJECTS_UPDATED_EVENT = "objectsUpdated";
	private static final String OBJECTS_UPDATED_EVENT_TYPE = "updatedObjectIsNew";
	private static final String INTEGRATION_ERROR_TITLE = "integrationbackoffice.inboundchannelconfiguration.error.title.invalidInput";
	private static final String INTEGRATION_CLIENT_CRED_ERROR_MESSAGE = "integrationbackoffice.inboundchannelconfiguration.error.msg.invalidInput";
	private static final String INTEGRATION_USER_ERROR_MESSAGE = "integrationbackoffice.inboundchannelconfiguration.error.msg.user";

	private ModelService modelService;
	private InboundChannelConfigNotificationService iccNotificationService;
	private CockpitEventQueue cockpitEventQueue;
	private RegisterIntegrationObjectService registerIntegrationObjectService;

	@Override
	public void perform(final CustomType customType, final FlowActionHandlerAdapter flowActionHandlerAdapter,
	                    final Map<String, String> map)
	{
		final InboundChannelConfigurationModel icc = flowActionHandlerAdapter.getWidgetInstanceManager()
		                                                                     .getModel()
		                                                                     .getValue("inboundchannelconfig",
				                                                                     InboundChannelConfigurationModel.class);

		final IntegrationClientCredentialsDetailsModel integrationCCD = flowActionHandlerAdapter.getWidgetInstanceManager()
		                                                                                        .getModel()
		                                                                                        .getValue(
				                                                                                        "newIntegrationServiceClientDetail",
				                                                                                        IntegrationClientCredentialsDetailsModel.class);

		if (AuthenticationType.OAUTH.equals(icc.getAuthenticationType()))
		{
			if(!isIntegrationClientCredentialsDetailsValid(integrationCCD))
			{
				Messagebox.show(Labels.getLabel(INTEGRATION_CLIENT_CRED_ERROR_MESSAGE),
						Labels.getLabel(INTEGRATION_ERROR_TITLE),1,
						Messagebox.ERROR);
			}
			else if(!isUserSelected(integrationCCD))
			{
				Messagebox.show(Labels.getLabel(INTEGRATION_USER_ERROR_MESSAGE),
						Labels.getLabel(INTEGRATION_ERROR_TITLE),1,
						Messagebox.ERROR);
			}
			else
			{
				registerIntegrationObjectService.createExposedOAuthCredential(integrationCCD);
				saveIntegrationChannelConfiguration(flowActionHandlerAdapter, icc);
			}

		}


		if (AuthenticationType.BASIC.equals(icc.getAuthenticationType()))
		{
			saveIntegrationChannelConfiguration(flowActionHandlerAdapter, icc);
		}

	}

	private void saveIntegrationChannelConfiguration(FlowActionHandlerAdapter flowActionHandlerAdapter, InboundChannelConfigurationModel icc) {

		modelService.save(icc);
		publishEvent(icc);
		renderNotification(flowActionHandlerAdapter, icc);
		flowActionHandlerAdapter.done();
	}

	private void publishEvent(final InboundChannelConfigurationModel inboundChannelConfig)
	{
		final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(OBJECTS_UPDATED_EVENT, inboundChannelConfig, null);
		cockpitEvent.getContext().put(OBJECTS_UPDATED_EVENT_TYPE, true);
		cockpitEventQueue.publishEvent(cockpitEvent);
	}

	private void renderNotification(final FlowActionHandlerAdapter flowActionHandlerAdapter,
	                                final InboundChannelConfigurationModel inboundChannelConfig)
	{
		iccNotificationService.createICCToExposedDestinationSuccessNotification(
				flowActionHandlerAdapter.getWidgetInstanceManager(), inboundChannelConfig);
	}

	private boolean isIntegrationClientCredentialsDetailsValid(IntegrationClientCredentialsDetailsModel integrationClientCredentialsDetails)
	{
		return !(StringUtils.isEmpty(integrationClientCredentialsDetails.getClientId()) || StringUtils.isEmpty(integrationClientCredentialsDetails.getClientSecret()));
	}

	private boolean isUserSelected(IntegrationClientCredentialsDetailsModel integrationClientCredentialsDetails)
	{
		return integrationClientCredentialsDetails.getUser() != null;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setIccNotificationService(final InboundChannelConfigNotificationService iccNotificationService)
	{
		this.iccNotificationService = iccNotificationService;
	}

	public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
	{
		this.cockpitEventQueue = cockpitEventQueue;
	}

	public void setRegisterIntegrationObjectService(
			final RegisterIntegrationObjectDefaultService registerIntegrationObjectService)
	{
		this.registerIntegrationObjectService = registerIntegrationObjectService;
	}

}
