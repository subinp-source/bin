/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.notificationservices.cronjob.AbstractNotificationJob;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;


/**
 * Sends coupon notification to the customer when a customer coupon will take effect or expire soon
 */
public abstract class AbstractCouponNotificationJob extends AbstractNotificationJob
{
	private static final String COUPON_EXPIRE_NOTIFICATION_DAYS = "coupon.expire.notification.days";
	private static final String COUPON_EFFECTIVE_NOTIFICAITON_DAYS = "coupon.effective.notification.days";
	private static final Integer ZERO = 0;

	private CustomerCouponDao customerCouponDao;
	private CouponNotificationDao couponNotificationDao;

	/**
	 * Executes cronjob and sends coupon notification when a coupon will take effect or expire soon
	 * 
	 * @param job
	 *           cronjob model
	 * @return the cronjob execution result
	 */
	@Override
	public PerformResult perform(final CronJobModel job)
	{
		final Integer configEffectiveDays = Config.getInt(COUPON_EFFECTIVE_NOTIFICAITON_DAYS, ZERO);
		final DateTime effectiveDay = new DateTime().plusDays(configEffectiveDays);
		final Integer configExpireDays = Config.getInt(COUPON_EXPIRE_NOTIFICATION_DAYS, ZERO);
		final DateTime expireDay = new DateTime().plusDays(configExpireDays);

		final Map<CustomerCouponModel, SiteMessageModel> messages = new ConcurrentHashMap<>();
		getCouponNotificationDao().findAllCouponNotifications().forEach(notification -> {
			final boolean isUnassignedCoupon = getCustomerCouponDao()
					.countAssignedCouponForCustomer(notification.getCustomerCoupon().getCouponId(), notification.getCustomer()) < 1;
			if (new DateTime(notification.getCustomerCoupon().getEndDate()).isBeforeNow() || isUnassignedCoupon)
			{
				modelService.remove(notification);
				return;
			}
			sendNotification(notification, effectiveDay, expireDay, messages);
		});

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected void sendNotification(final CouponNotificationModel notification, final DateTime effectiveDay,
			final DateTime expireDay, final Map<CustomerCouponModel, SiteMessageModel> messages)
	{
		final CustomerCouponModel coupon = notification.getCustomerCoupon();
		if (notification.getStatus().equals(CouponNotificationStatus.INIT)
				&& new DateTime(coupon.getStartDate()).isBefore(effectiveDay))
		{
			if (!messages.containsKey(coupon))
			{
				messages.put(coupon, createSiteMessage(notification, NotificationType.COUPON_EFFECTIVE));
			}
			sendCouponNotificaiton(notification, NotificationType.COUPON_EFFECTIVE, messages.get(coupon));
		}
		if ((notification.getStatus().equals(CouponNotificationStatus.INIT)
				|| notification.getStatus().equals(CouponNotificationStatus.EFFECTIVESENT))
				&& new DateTime(coupon.getEndDate()).isBefore(expireDay))
		{
			if (!messages.containsKey(coupon))
			{
				messages.put(coupon, createSiteMessage(notification, NotificationType.COUPON_EXPIRE));
			}
			sendCouponNotificaiton(notification, NotificationType.COUPON_EXPIRE, messages.get(coupon));
		}
	}

	protected SiteMessageModel createSiteMessage(final CouponNotificationModel notification,
			final NotificationType notificationType)
	{
		final CustomerCouponModel coupon = notification.getCustomerCoupon();
		final String subject = Localization.getLocalizedMap("coupon.notification.sitemessage.subject").entrySet().stream()
				.filter(map -> map.getKey().getIsocode().equals(notification.getLanguage().getIsocode())).map(map -> map.getValue())
				.collect(Collectors.joining());

		String body = Localization.getLocalizedMap("coupon.notification.sitemessage." + notificationType.getCode() + ".body")
				.entrySet().stream().filter(map -> map.getKey().getIsocode().equals(notification.getLanguage().getIsocode()))
				.map(map -> map.getValue())
				.collect(Collectors.joining());
		body = MessageFormat.format(body, coupon.getCouponId());

		return getSiteMessageService().createMessage(subject, body, SiteMessageType.SYSTEM, coupon, notificationType);
	}

	protected void sendCouponNotificaiton(final CouponNotificationModel couponNotification,
			final NotificationType notificationType, final SiteMessageModel message)
	{
		final Map<String, ItemModel> data = new HashMap<>();
		data.put(CustomercouponservicesConstants.LANGUAGE, couponNotification.getLanguage());
		data.put(CustomercouponservicesConstants.COUPON_NOTIFICATION, couponNotification);
		data.put(CustomercouponservicesConstants.SITE_MESSAGE, message);
		final ItemModel notificationTypeItem = new ItemModel();
		notificationTypeItem.setProperty(CustomercouponservicesConstants.NOTIFICATION_TYPE, notificationType);
		data.put(CustomercouponservicesConstants.NOTIFICATION_TYPE, notificationTypeItem);

		getTaskExecutor().execute(createTask(data));

		couponNotification.setStatus(CouponNotificationStatus.EFFECTIVESENT);

		if (NotificationType.COUPON_EXPIRE.equals(notificationType))
		{
			couponNotification.setStatus(CouponNotificationStatus.EXPIRESENT);
		}

		modelService.save(couponNotification);
	}

	protected abstract CouponNotificationTask createTask(final Map<String, ItemModel> data);


	protected CustomerCouponDao getCustomerCouponDao()
	{
		return customerCouponDao;
	}

	@Required
	public void setCustomerCouponDao(final CustomerCouponDao customerCouponDao)
	{
		this.customerCouponDao = customerCouponDao;
	}

	protected CouponNotificationDao getCouponNotificationDao()
	{
		return couponNotificationDao;
	}

	@Required
	public void setCouponNotificationDao(final CouponNotificationDao couponNotificationDao)
	{
		this.couponNotificationDao = couponNotificationDao;
	}

}
