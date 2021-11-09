/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import de.hybris.platform.apiregistrybackoffice.constants.ApiregistrybackofficeConstants;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.apiregistryservices.services.ServiceWarning;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;


public class DeleteDestinationTargetAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
	private static final String OBJECTS_DELETED = "objectsDeleted";
	private static final String FAIL = "FAIL";
	private static final String WARN = "WARN";
	private static final Logger LOG = LoggerFactory.getLogger(DeleteDestinationTargetAction.class);
	private static final String DESTINATION_TARGETS_DELETE_CONFIRMATION = "destinationTarget.delete.confirmation";
	private static final String DESTINATION_TARGETS_DELETE_WARNING = "destinationTarget.delete.warning";
	private static final String DESTINATION_TARGETS_DELETE_FAILED = "destinationTarget.delete.failed";
	private static final String DESTINATION_TARGETS_DELETE_SUCCESS = "destinationTarget.delete.success";

	@Resource
	private DestinationTargetService destinationTargetService;

	@Resource
	private NotificationService notificationService;
	
	@Resource
	private CockpitGlobalEventPublisher eventPublisher;

	@Override
	public ActionResult<Object> perform(final ActionContext<Object> actionContext)
	{
		final List<DestinationTargetModel> destinationTargets = getDestinationTarget(actionContext.getData());

		final List<Pair<String, String>> notOkDeletionStatuses = destinationTargets.stream()
				.map(this::deleteDestinationTarget).filter(result -> !Objects.isNull(result))
				.collect(Collectors.toList());

		final String warnings = notOkDeletionStatuses.stream().filter(result -> WARN.equals(result.getKey())).map(Pair::getValue)
				.collect(Collectors.joining(", "));
		final String errors = notOkDeletionStatuses.stream().filter(result -> FAIL.equals(result.getKey())).map(Pair::getValue)
				.collect(Collectors.joining(", "));

		ActionResult<Object> result = new ActionResult<>(ActionResult.SUCCESS, destinationTargets);

		if (StringUtils.isNotEmpty(warnings))
		{
			final String message = actionContext.getLabel(DESTINATION_TARGETS_DELETE_WARNING,
					new String[]
					{ warnings });

			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(actionContext),
					ApiregistrybackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.WARNING, message);
		}

		if (StringUtils.isNotEmpty(errors))
		{
			final String message = actionContext.getLabel(DESTINATION_TARGETS_DELETE_FAILED,
					new String[]
					{ errors });

			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(actionContext),
					ApiregistrybackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.FAILURE, message);
			result = new ActionResult<>(ActionResult.ERROR, destinationTargets);
		}
		else if (StringUtils.isEmpty(warnings))
		{
			final String successMessage = actionContext.getLabel(DESTINATION_TARGETS_DELETE_SUCCESS,
					new String[]
					{ getDestinationIdsListed(destinationTargets) });
			LOG.info(successMessage);

			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(actionContext),
					ApiregistrybackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.SUCCESS, successMessage);
		}

		eventPublisher.publish(OBJECTS_DELETED, Collections.unmodifiableCollection(destinationTargets), new DefaultContext());
		return result;
	}

	protected Pair<String, String> deleteDestinationTarget(DestinationTargetModel destinationTarget)
	{
		try
		{
			final Collection<ServiceWarning<DestinationTargetModel>> deletionResult = getDestinationTargetService()
					.deregisterAndDeleteDestinationTarget(destinationTarget);
			if (deletionResult.isEmpty())
			{
				return null;
			}
			else
			{
				return Pair.of(WARN,
						deletionResult.stream().map(warn -> warn.getMessage()).collect(Collectors.joining(", ")));
			}
		}
		catch (final ApiRegistrationException | RuntimeException e)
		{
			final String errorMessage = String.format("'%s' (\"%s\")", destinationTarget.getId(), e.getMessage());
			LOG.error(errorMessage, e);
			return Pair.of(FAIL, errorMessage);
		}

	}

	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		return ctx != null && CollectionUtils.isNotEmpty(getDestinationTarget(ctx.getData()));
	}


	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		return ctx != null && destinationTargetsNotInProgress(ctx.getData());
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{
		final List<DestinationTargetModel> destinationTargets = getDestinationTarget(ctx.getData());

		return ctx.getLabel(DESTINATION_TARGETS_DELETE_CONFIRMATION,
				new String[]
				{ getDestinationIdsListed(destinationTargets) });
	}

	protected boolean destinationTargetsNotInProgress(Object data)
	{
		final List<DestinationTargetModel> destinationTargets = getDestinationTarget(data);
		return CollectionUtils.isNotEmpty(destinationTargets)
				&& destinationTargets.stream().noneMatch(this::destinationTargetInProgress);
	}

	protected boolean destinationTargetInProgress(final DestinationTargetModel destinationTarget)
	{
		return RegistrationStatus.IN_PROGRESS.equals(destinationTarget.getRegistrationStatus());
	}

	protected List<DestinationTargetModel> getDestinationTarget(Object data)
	{
		if (data == null)
		{
			return Collections.emptyList();
		}
		if (data instanceof DestinationTargetModel)
		{
			return Collections.singletonList((DestinationTargetModel) data);
		}
		else if (data instanceof Collection && ((Collection<?>) data).stream().allMatch(DestinationTargetModel.class::isInstance))
		{
			return ((Collection<?>) data).stream().map(DestinationTargetModel.class::cast)
					.collect(Collectors.toList());
		}
		throw new IllegalArgumentException("Unexpected data provided: " + data.toString());
	}

	protected String getDestinationIdsListed(List<DestinationTargetModel> destinationTargets)
	{
		return destinationTargets.stream().map(DestinationTargetModel::getId).collect(Collectors.joining(", "));
	}

	protected DestinationTargetService getDestinationTargetService()
	{
		return destinationTargetService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

}
