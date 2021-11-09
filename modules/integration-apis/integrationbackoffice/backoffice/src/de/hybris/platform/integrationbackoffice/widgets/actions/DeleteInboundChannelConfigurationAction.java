/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.actions;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.persistence.populator.InboundChannelConfigurationDeletionException;
import de.hybris.platform.inboundservices.strategies.ICCDeletionValidationStrategy;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.assertj.core.util.Arrays;
import org.zkoss.lang.Strings;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

public final class DeleteInboundChannelConfigurationAction extends DeleteAction
{
	private static final Logger LOG = Logger.getLogger(DeleteInboundChannelConfigurationAction.class);

	@Resource
	private EditorAccessRights editorAccessRights;
	@Resource
	private ICCDeletionValidationStrategy iCCDeletionValidationStrategy;
	@Resource
	private ModelService modelService;

	private static final ActionResult<Object> CLASS_CASS_EXCEPTION_RESULT = new ActionResult<>(ActionResult.ERROR,
			"Passing a non-InboundChannelConfiguration object here.");

	private static final ActionResult<Object> EXCEPTION_RESULT = new ActionResult<>(ActionResult.ERROR,
			"InboundChannelConfiguration deletion failed.");

	@Override
	public ActionResult<Object> perform(final ActionContext<Object> ctx)
	{
		try
		{
			InboundChannelConfigurationModel inboundChannelConfigurationModel = (InboundChannelConfigurationModel) getContextObjects(
					ctx).get(0);
			modelService.refresh(inboundChannelConfigurationModel);
			iCCDeletionValidationStrategy.checkICCLinkedWithExposedDestination(inboundChannelConfigurationModel);
		}
		catch (InboundChannelConfigurationDeletionException ex)
		{
			super.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.WARNING, ctx.getLabel("integrationbackoffice.delete.error.msg.ICCDeletion",
							Arrays.array(ex.getDestinationTargets(), ex.getExposedDestinations())));
			LOG.error(ex.getErrorMessage());
			return EXCEPTION_RESULT;
		}
		catch (final ClassCastException ex)
		{
			return CLASS_CASS_EXCEPTION_RESULT;
		}
		catch (final Exception ex)
		{
			return EXCEPTION_RESULT;
		}

		return super.perform(ctx);
	}

	@Override
	public boolean canPerform(ActionContext<Object> ctx)
	{
		final boolean isEmptyCollection = ctx.getData() == null || (ctx.getData() instanceof Collection && ((Collection) ctx.getData())
				.isEmpty());
		return !isEmptyCollection && editorAccessRights.isUserAdmin();
	}

	private List<Object> getContextObjects(final ActionContext<Object> ctx)
	{
		final List<Object> ctxObjects = new ArrayList<>();
		if (ctx.getData() instanceof Collection)
		{
			ctxObjects.addAll((Collection) ctx.getData());
		}
		else
		{
			ctxObjects.add(ctx.getData());
		}
		return ctxObjects;
	}

	@Override
	public String getConfirmationMessage(ActionContext<Object> ctx) {
		return ctx.getLabel("integrationbackoffice.delete.confirm");
	}

}
