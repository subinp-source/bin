/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpay.filters;

import de.hybris.platform.chinesepspwechatpay.constants.ChinesepspwechatpaymentaddonWebConstants;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.UserCodeRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WeChatPayConfiguration;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter to get wechat user open id if has no open id in session.
 */
public class WechatOpenIdFilter extends OncePerRequestFilter
{

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "weChatPayConfiguration")
	private WeChatPayConfiguration weChatPayConfiguration;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws ServletException, IOException
	{
		final boolean isWechatBrowser = StringUtils.containsIgnoreCase(request.getHeader("user-agent"), "MicroMessenger");
		final boolean isCallbackUrl = request.getRequestURI().contains("/wechat/openid");
		final boolean hasOpenId = sessionService.getAttribute(ChinesepspwechatpaymentaddonWebConstants.WECHAT_OPENID) != null;

		if (isWechatBrowser && !isCallbackUrl && !hasOpenId)
		{
			new UserCodeRequestProcessor(weChatPayConfiguration, request, response).process();
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

}
