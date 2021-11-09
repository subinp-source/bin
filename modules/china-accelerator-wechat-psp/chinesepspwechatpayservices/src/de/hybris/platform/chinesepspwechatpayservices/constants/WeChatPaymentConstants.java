/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.constants;

import de.hybris.platform.payment.dto.TransactionStatus;

import java.util.HashMap;
import java.util.Map;


/**
 * Global class for all WeChat constants.
 */
public interface WeChatPaymentConstants
{
	/**
	 * Basic request constants
	 */
	interface Basic
	{
		String EXTENSIONNAME = "chinesepspalipayservices";
		String PAYMENT_PROVIDER = "Wechat Pay";
	}

	/**
	 * Controller constants
	 */
	interface Controller
	{
		String _Prefix = "/checkout/multi/summary" + "/wechat/";
		String _Suffix = "Controller";

		String NOTIFY_URL = _Prefix + "paymentresponse/" + "notify";
		String ERROR_NOTIFY_URL = _Prefix + "pspasynresponse/" + "error" + _Suffix;
		String GET_REFUND_URL = _Prefix + "refund" + _Suffix;
	}

	interface Notification
	{
		String RETURN_SUCCESS = "SUCCESS";
		String RETURN_FAIL = "FAIL";
		String RESULT_SUCCESS = "SUCCESS";
		String RESULT_FAIL = "FAIL";
	}

	class TransactionStatusMap
	{
		static final Map<String, TransactionStatus> WeChatPayToHybris = new HashMap<String, TransactionStatus>();

		private TransactionStatusMap()
		{
			throw new IllegalAccessError("TransactionStatusMap class");
		}

		public static Map<String, TransactionStatus> getWechatpaytohybris()
		{
			return WeChatPayToHybris;
		}

		static
		{
			WeChatPayToHybris.put("SUCCESS", TransactionStatus.ACCEPTED);
			WeChatPayToHybris.put("USERPAYING", TransactionStatus.REVIEW);
			WeChatPayToHybris.put("REFUND", TransactionStatus.REVIEW);
			WeChatPayToHybris.put("NOTPAY", TransactionStatus.REVIEW);
			WeChatPayToHybris.put("CLOSED", TransactionStatus.REJECTED);
			WeChatPayToHybris.put("REVOKED", TransactionStatus.REJECTED);
			WeChatPayToHybris.put("PAYERROR", TransactionStatus.ERROR);
		}


	}
}
