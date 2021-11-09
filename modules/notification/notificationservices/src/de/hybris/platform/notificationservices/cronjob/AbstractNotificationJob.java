/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.cronjob;



import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.notificationservices.service.NotificationService;
import de.hybris.platform.notificationservices.service.SiteMessageService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.task.TaskExecutor;


/**
 * Abstract base class to provide the common properties for cron jobs.
 */
public abstract class AbstractNotificationJob extends AbstractJobPerformable<CronJobModel>
{

	private TaskExecutor taskExecutor;
	private NotificationService notificationService;
	private SiteMessageService siteMessageService;
	private CommerceCommonI18NService commerceCommonI18NService;

	public AbstractNotificationJob()
	{
		super();
	}

	@Required
	public void setTaskExecutor(final TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	protected TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	@Required
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	protected SiteMessageService getSiteMessageService()
	{
		return siteMessageService;
	}

	@Required
	public void setSiteMessageService(final SiteMessageService siteMessageService)
	{
		this.siteMessageService = siteMessageService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
}
