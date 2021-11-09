/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpay.controllers.pages;

import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.chinesepaymentfacades.checkout.ChineseCheckoutFacade;
import de.hybris.platform.chinesepspwechatpay.constants.ChinesepspwechatpaymentaddonWebConstants;
import de.hybris.platform.chinesepspwechatpayservices.data.StartPaymentData;
import de.hybris.platform.chinesepspwechatpayservices.exception.WeChatPayException;
import de.hybris.platform.chinesepspwechatpayservices.order.WeChatPayOrderService;
import de.hybris.platform.chinesepspwechatpayservices.payment.impl.DefaultWeChatPayPaymentService;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.OpenIdRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.StartPaymentRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.UnifiedOrderRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WeChatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WeChatPayHttpClient;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@Scope("tenant")
@RequestMapping("")
public class WeChatPayController extends AbstractPageController
{
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";

	@Resource(name = "weChatPayConfiguration")
	private WeChatPayConfiguration weChatPayConfiguration;

	@Resource(name = "weChatPayHttpClient")
	private WeChatPayHttpClient weChatPayHttpClient;

	@Resource(name = "weChatPayOrderService")
	private WeChatPayOrderService weChatPayOrderService;

	@Resource(name = "wechatpayPaymentService")
	private DefaultWeChatPayPaymentService weChatPayPaymentService;

	@Resource(name = "siteBaseUrlResolutionService")
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	@Resource(name = "chineseCheckoutFacade")
	private ChineseCheckoutFacade chineseCheckoutFacade;

	@ResponseBody
	@GetMapping(value = "/checkout/multi/wechat/pay/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	public StartPaymentData process(@PathVariable final String orderCode, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final Optional<OrderModel> optional = weChatPayOrderService.getOrderByCode(orderCode);
		if (!optional.isPresent())
		{
			throw new WeChatPayException("Can't find order for code:" + orderCode);
		}

		chineseCheckoutFacade.submitOrder(orderCode);
		
		final String openId = sessionService.getAttribute(ChinesepspwechatpaymentaddonWebConstants.WECHAT_OPENID);
		final String baseUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteService.getCurrentBaseSite(), true, "");
		final String prepayId = new UnifiedOrderRequestProcessor(weChatPayConfiguration, weChatPayHttpClient, openId,
				optional.get(), request.getRemoteAddr(), baseUrl).process();

		return new StartPaymentRequestProcessor(weChatPayConfiguration, prepayId).process();
	}

	@PostMapping(value = "/checkout/multi/wechat/startPay")
	public void paySuccess(@RequestParam(value = "orderCode", required = false) final String code)
	{
		weChatPayPaymentService.createTransactionForNewRequest(code);
	}

	@GetMapping("/wechat/openid")
	public String receiveCode(@RequestParam(value = "code", required = true) final String code, final HttpServletRequest request,
			final HttpServletResponse response, final Model model)
	{
		final String openId = new OpenIdRequestProcessor(weChatPayConfiguration, weChatPayHttpClient, code).process();
		sessionService.setAttribute(ChinesepspwechatpaymentaddonWebConstants.WECHAT_OPENID, openId);

		return REDIRECT_PREFIX + "/";
	}
}
