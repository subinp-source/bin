/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpayservices.processors.impl;

import de.hybris.platform.chinesepspwechatpayservices.exception.WeChatPayException;
import de.hybris.platform.chinesepspwechatpayservices.processors.AbstractWeChatPayRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WeChatPayConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang3.StringUtils;


/**
 * Processor for fetching usercode.
 */
public class UserCodeRequestProcessor extends AbstractWeChatPayRequestProcessor<Void>
{

	private static final String WECHAT_REDIRECT_PARAM = "#wechat_redirect";

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public UserCodeRequestProcessor(final WeChatPayConfiguration config, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		super(config, null);
		super.setUrl(config.getOauthURL());
		this.request = request;
		this.response = response;
		super.addParameter("redirect_uri", getRedirectUri());
		super.addParameter("response_type", "code");
		super.addParameter("scope", "snsapi_base");
	}

	private String getRedirectUri()
	{
		final String urlPrefix = StringUtils.remove(request.getRequestURL().toString(), request.getRequestURI());
		final StringBuilder redirectUri = new StringBuilder();
		redirectUri.append(urlPrefix);
		redirectUri.append(urlPrefix.endsWith("/") ? "wechat/openid?showwxpaytitle=1" : "/wechat/openid?showwxpaytitle=1");

		try
		{
			return URLEncoder.encode(redirectUri.toString(), CharEncoding.UTF_8);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new WeChatPayException("Error encode redirect uri: " + redirectUri.toString(), e);
		}
	}

	@Override
	public Void process()
	{
		final String url =  getParams().generateGetURL(this.getUrl()) + WECHAT_REDIRECT_PARAM;
		try
		{
			response.sendRedirect(response.encodeRedirectURL(url));	//NOSONAR
		}
		catch (final IOException e)
		{
			throw new WeChatPayException("Error redirect to " + url, e);
		}
		return null;
	}
}
