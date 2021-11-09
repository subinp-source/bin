/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsyncbackoffice.widgets.actions;

import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;
import de.hybris.platform.integrationbackoffice.widgets.modals.builders.AuditReportBuilder;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;


/**
 * Action responsible for downloading a audit report of one OutboundChannelConfiguration object
 */
public final class OutboundChannelConfigAuditReportDownloadAction implements CockpitAction<Object, String>
{
	@WireVariable
	private AuditReportBuilder oBChannelConfigurationAuditReportBuilder;

	@Resource
	private EditorAccessRights editorAccessRights;

	private static final ActionResult<String> CLASS_CASS_EXCEPTION_RESULT = new ActionResult<>(ActionResult.ERROR,
			"Passing a non OutboundChannelConfiguration object here.");

	private static final ActionResult<String> EXCEPTION_RESULT = new ActionResult<>(ActionResult.ERROR,
			"Report generating failure.");

	private static final ActionResult<String> SUCCESS_RESULT = new ActionResult<>(ActionResult.SUCCESS, "");

	@Override
	public ActionResult<String> perform(final ActionContext<Object> ctx)
	{
		try
		{
			final OutboundChannelConfigurationModel outboundChannelConfigurationModel = (OutboundChannelConfigurationModel) getContextObjects(
					ctx).get(0);
			oBChannelConfigurationAuditReportBuilder.generateAuditReport(outboundChannelConfigurationModel);
		}
		catch (final ClassCastException ex)
		{
			return CLASS_CASS_EXCEPTION_RESULT;
		}
		catch (final Exception ex)
		{
			return EXCEPTION_RESULT;
		}
		return SUCCESS_RESULT;
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

	protected AuditReportBuilder getoBChannelConfigurationAuditReportBuilder()
	{
		return oBChannelConfigurationAuditReportBuilder;
	}

	public void setoBChannelConfigurationAuditReportBuilder(AuditReportBuilder oBChannelConfigurationAuditReportBuilder)
	{
		this.oBChannelConfigurationAuditReportBuilder = oBChannelConfigurationAuditReportBuilder;
	}
}
