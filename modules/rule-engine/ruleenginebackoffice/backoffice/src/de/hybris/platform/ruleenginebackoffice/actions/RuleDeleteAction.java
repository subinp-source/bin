/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.actions;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.delete.DeleteAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;


public class RuleDeleteAction extends DeleteAction
{
	private static final Logger LOG = LoggerFactory.getLogger(RuleDeleteAction.class);

	@Override
	public ActionResult<Object> perform(final ActionContext<Object> context)
	{
		final List<Object> ctxObjects = getDataAsList(context);

		ObjectFacadeOperationResult result;

		try
		{
			result = getObjectFacade().delete(ctxObjects);
		}
		catch (final RuntimeException exc)
		{
			result = addAllItemsToFailedObjects(ctxObjects, exc);
			LOG.debug("Cannot delete item", exc);
		}

		if (result == null)
		{
			return new ActionResult("error", ctxObjects);
		}
		else
		{
			if (result.hasError())
			{
				final Map<Object, ObjectAccessException> f =
						(Map<Object, ObjectAccessException>) result.getFailedObjects().stream().collect(toMap(Function.identity(),
								result::getErrorForObject));

				showFailureNotification(context, f);
			}

			if (result.countSuccessfulObjects() > 0)
			{
				showSuccessNotification(context, ctxObjects);
			}
		}

		return new ActionResult<>(result.countSuccessfulObjects() < 1 ? "error" : "success", ctxObjects);
	}

	@Override
	public boolean canPerform(final ActionContext context)
	{
		return super.canPerform(context) && !getDataAsList(context).isEmpty();
	}

	protected List<Object> getDataAsList(final ActionContext<Object> context)
	{
		final List<Object> ctxObjects = new ArrayList();

		if (context.getData() instanceof Collection)
		{
			ctxObjects.addAll((Collection) context.getData());
		}
		else
		{
			ctxObjects.add(context.getData());
		}

		return ctxObjects.stream().filter(AbstractRuleModel.class::isInstance).map(AbstractRuleModel.class::cast)
				.filter(r -> !RuleStatus.PUBLISHED.equals(r.getStatus())).collect(
				toList());
	}

	protected ObjectFacadeOperationResult<Object> addAllItemsToFailedObjects(final List<Object> ctxObjects,
			final RuntimeException ex)
	{
		final ObjectFacadeOperationResult<Object> result = new ObjectFacadeOperationResult();
		ctxObjects.forEach(obj -> result.addFailedObject(obj, new ObjectAccessException(ex.getMessage(), ex)));
		return result;
	}
}
