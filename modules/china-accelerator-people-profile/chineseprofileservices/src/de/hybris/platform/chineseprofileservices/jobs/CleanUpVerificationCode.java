/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofileservices.jobs;

import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class CleanUpVerificationCode extends AbstractJobPerformable<CronJobModel>
{

	private ChineseCustomerAccountService customerAccountService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final List<MobileNumberVerificationModel> expiredCodes = getCustomerAccountService()
				.findExpiredVerificationCode(getCustomerAccountService().getVerificationCodeExpiredDate());
		modelService.removeAll(expiredCodes);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Required
	public void setCustomerAccountService(final ChineseCustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}
	
	protected ChineseCustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}


}
