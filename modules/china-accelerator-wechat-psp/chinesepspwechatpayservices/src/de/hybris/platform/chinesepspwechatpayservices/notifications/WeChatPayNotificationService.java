/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.notifications;

import de.hybris.platform.chinesepspwechatpayservices.data.WeChatRawDirectPayNotification;


/**
 * Provide method to handle notification from WeChat
 */
public interface WeChatPayNotificationService
{
	/**
	 * Handling the Asyn-response of the 3rd part payment service provider server
	 *
	 * @param weChatPayNotification
	 *           The parameters of WeChatRawDirectPayNotification
	 */
	void handleWeChatPayPaymentResponse(final WeChatRawDirectPayNotification weChatPayNotification);
}
