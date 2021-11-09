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
package de.hybris.platform.ruleenginebackoffice.renderer;

import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineJobModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;

import com.hybris.backoffice.widgets.processes.renderer.DefaultProcessItemRenderingStrategy;

import java.util.Optional;


/**
 * Rule engine extension of {@link DefaultProcessItemRenderingStrategy}
 */
public class RuleEngineProcessItemRenderingStrategy extends DefaultProcessItemRenderingStrategy
{
	@Override
	public boolean canHandle(final CronJobHistoryModel cronJobHistory)
	{
		return Optional.ofNullable(cronJobHistory.getCronJob()).map(CronJobModel::getJob).map(AbstractItemModel::getItemtype)
				.filter(RuleEngineJobModel._TYPECODE::equals).isPresent();
	}
	
	@Override
	public boolean isProgressSupported(final CronJobHistoryModel cronJobHistory)
	{
		return true;
	}

	@Override
	public boolean isRerunApplicable(final CronJobHistoryModel cronJobHistory)
	{
		return false;
	}
	
}
