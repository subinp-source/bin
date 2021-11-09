/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.strategies;

import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundNotification;

import java.util.List;
import java.util.Map;


/**
 * Deals with payment related responses after reception
 */
public interface AlipayHandleResponseStrategy
{
	/**
	 * Formats the response from alipay to camelCase
	 *
	 * @param responseMap
	 *           original response map from http response
	 * @param responseRawData
	 *           target response POJO data whose properties are camel cased
	 * @return object response POJO in camel case
	 */
	Object camelCaseFormatter(final Map<String, String> responseMap, Object responseRawData);



	/**
	 * Gets refund data list from refund notification
	 *
	 * @param alipayRefundNotification
	 *           alipay refund notification
	 * @return list of all refund data {@link AlipayRefundData}
	 */
	List<AlipayRefundData> getAlipayRefundDataList(final AlipayRefundNotification alipayRefundNotification);


}
