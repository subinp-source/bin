/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.ordermanagementwebservices.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.core.Registry;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestModificationWsDTO;
import de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants;
import de.hybris.platform.ordermanagementwebservices.dto.fraud.FraudReportListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.CancelReasonListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderCancelRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderEntrySearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderSearchPageWsDto;
import de.hybris.platform.ordermanagementwebservices.dto.order.OrderStatusListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.CancelReturnRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.RefundReasonListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnActionListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnEntrySearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnRequestWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnSearchPageWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.returns.ReturnStatusListWsDTO;
import de.hybris.platform.ordermanagementwebservices.dto.workflow.WorkflowActionListWsDto;
import de.hybris.platform.ordermanagementwebservices.dto.workflow.WorkflowCodesListWsDto;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.Optional;

import org.springframework.test.context.ContextConfiguration;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@NeedsEmbeddedServer(webExtensions = { OrdermanagementwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
@ContextConfiguration(locations = { "classpath:/ordermanagementwebservices-spring-test.xml" })
public class BaseOrderManagementWebservicesIntegrationTest extends ServicelayerTest
{
	protected static final String DEFAULT_FIELDS = "DEFAULT";
	protected static final String DEFAULT_CURRENT_PAGE = "0";
	protected static final String DEFAULT_PAGE_SIZE = "100";
	protected static final String ADMIN_USERNAME = "admin";
	protected static final String ADMIN_PASSWORD = "nimda";
	protected static final String CUSTOMER_SUPPORT_AGENT_USERNAME = "CustomerSupportAgent";
	protected static final String CUSTOMER_SUPPORT_AGENT_PASSWORD = "1234";
	protected static final String DEFAULT_CLIENT_ID = "trusted_client";
	protected static final String DEFAULT_CLIENT_SECRET = "secret";

	@Resource
	private EnumerationService enumerationService;
	@Resource
	private SetupImpexService setupImpexService;

	protected OrderSearchPageWsDto getAllOrderByDefault()
	{
		return getDefaultSecuredRestCall("orders", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS,
				DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, OrderSearchPageWsDto.class);
	}

	protected OrderWsDTO getOrderByCode(final String code)
	{
		return getDefaultSecuredRestCall("orders/" + code, CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD, "FULL",
				DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, OrderWsDTO.class);
	}

	protected <T> T getOrdersByStatuses(final String orderStatuses, final Class<T> responseType)
	{
		return getDefaultSecuredRestCall("orders/status/" + orderStatuses, CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, responseType);
	}

	protected OrderEntrySearchPageWsDTO getOrderEntriesForOrderCode(final String code)
	{
		return getDefaultSecuredRestCall("orders/" + code + "/entries", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, "FULL", DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, OrderEntrySearchPageWsDTO.class);
	}

	protected OrderEntryWsDTO getOrderEntryForOrderCodeAndEntryNumber(final String code, final String entryNumber)
	{
		return getDefaultSecuredRestCall("orders/" + code + "/entries/" + entryNumber, CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, "FULL", DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, OrderEntryWsDTO.class);
	}

	protected OrderStatusListWsDTO getOrderStatusByDefault()
	{
		return getDefaultSecuredRestCall("orders/statuses", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, OrderStatusListWsDTO.class);
	}

	protected FraudReportListWsDTO getOrderFraudReports(final String code)
	{
		return getDefaultSecuredRestCall("orders/" + code + "/fraud-reports", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, "FULL", DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, FraudReportListWsDTO.class);
	}

	protected CancelReasonListWsDTO getOrderCancellationReasons()
	{
		return getDefaultSecuredRestCall("orders/cancel-reasons", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, CancelReasonListWsDTO.class);
	}

	protected CancelReasonListWsDTO getReturnCancellationReasons()
	{
		return getDefaultSecuredRestCall("returns/cancel-reasons", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, CancelReasonListWsDTO.class);
	}

	protected RefundReasonListWsDTO getRefundReasons()
	{
		return getDefaultSecuredRestCall("returns/refund-reasons", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, RefundReasonListWsDTO.class);
	}

	protected ReturnActionListWsDTO getReturnActions()
	{
		return getDefaultSecuredRestCall("returns/actions", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, ReturnActionListWsDTO.class);
	}

	protected ReturnStatusListWsDTO getReturnStatuses()
	{
		return getDefaultSecuredRestCall("returns/statuses", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, ReturnStatusListWsDTO.class);
	}

	protected ReturnSearchPageWsDTO getReturns()
	{
		return getDefaultSecuredRestCall("returns", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, ReturnSearchPageWsDTO.class);
	}

	protected <T> T getReturnsByStatus(final String statuses, final Class<T> responseType)
	{
		return getDefaultSecuredRestCall("returns/status/" + statuses, CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, responseType);
	}

	protected ReturnRequestWsDTO getReturnForReturnCode(final String returnCode)
	{
		return getDefaultSecuredRestCall("returns/" + returnCode, CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, ReturnRequestWsDTO.class);
	}

	protected ReturnEntrySearchPageWsDTO getReturnEntriesForReturnCode(final String returnCode)
	{
		return getDefaultSecuredRestCall("returns/" + returnCode + "/entries", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
				ReturnEntrySearchPageWsDTO.class);
	}

	protected Response postRequestManualPaymentReversalForReturnRequest(final String returnCode)
	{
		return postEmptyBodySecuredRestCall("returns/" + returnCode + "/manual/reverse-payment", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD);
	}

	protected Response postRequestManualTaxReversalForReturnRequest(final String returnCode)
	{
		return postEmptyBodySecuredRestCall("returns/" + returnCode + "/manual/reverse-tax", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD);
	}

	protected Response postCancelReturnRequestByDefault(final CancelReturnRequestWsDTO cancelReturnRequestWsDTO)
	{
		return postDefaultSecuredRestCall("returns/cancel", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, cancelReturnRequestWsDTO);
	}

	protected Response postApproveFraudulentOrder(final String code)
	{
		return postEmptyBodySecuredRestCall("orders/" + code + "/fraud-reports/approve", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD);
	}

	protected Response postRejectFraudulentOrder(final String code)
	{
		return postEmptyBodySecuredRestCall("orders/" + code + "/fraud-reports/reject", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD);
	}

	protected Response postCancelOrder(final OrderCancelRequestWsDTO orderCancelRequestWsDTO, final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/cancel/", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, orderCancelRequestWsDTO);
	}

	protected Response postCreateOrder(final OrderRequestWsDTO orderRequestWsDTO)
	{
		return postDefaultSecuredRestCall("orders", CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, orderRequestWsDTO);
	}

	protected ReturnRequestWsDTO putUpdateReturnByReturnCode(final String code,
			final ReturnRequestModificationWsDTO returnRequestModificationWsDTO)
	{
		return putDefaultSecuredRestCall("/returns/" + code, CUSTOMER_SUPPORT_AGENT_USERNAME, CUSTOMER_SUPPORT_AGENT_PASSWORD,
				DEFAULT_FIELDS, returnRequestModificationWsDTO, ReturnRequestWsDTO.class);
	}

	protected Response postManualPaymentVoid(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/void-payment", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected Response postManualTaxVoid(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/void-tax", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected Response postManualTaxCommit(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/commit-tax", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected Response postManualTaxRequote(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/requote-tax", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected Response postManualPaymentReauth(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/reauth-payment", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected Response postManualDeliveryCostCommit(final String code)
	{
		return postDefaultSecuredRestCall("orders/" + code + "/manual/delivery-cost-commit", CUSTOMER_SUPPORT_AGENT_USERNAME,
				CUSTOMER_SUPPORT_AGENT_PASSWORD, DEFAULT_FIELDS, null);
	}

	protected WorkflowActionListWsDto getWorkflowActions()
	{
		return getDefaultSecuredRestCall("workflows/actions", ADMIN_USERNAME, ADMIN_PASSWORD, DEFAULT_FIELDS, DEFAULT_CURRENT_PAGE,
				DEFAULT_PAGE_SIZE, WorkflowActionListWsDto.class);
	}

	protected Response postDecideWorkflowAction(final String workflowCode, final String workflowDecisionCode)
	{
		return postDefaultSecuredRestCall("workflows/" + workflowCode + "/" + workflowDecisionCode, ADMIN_USERNAME, ADMIN_PASSWORD,
				DEFAULT_FIELDS, null);
	}

	protected Response postDecideWorkflowActions(final WorkflowCodesListWsDto workflowCodesListWsDto, final String decisionCode)
	{
		return postDefaultSecuredRestCall("workflows/decide-action/" + decisionCode, ADMIN_USERNAME, ADMIN_PASSWORD, DEFAULT_FIELDS,
				workflowCodesListWsDto);
	}

	/**
	 * Builds a secured GET rest call
	 *
	 * @param path
	 * 		the url for the call
	 * @param username
	 * 		the username used for authentication
	 * @param password
	 * 		the password used for authentication
	 * @param fields
	 * 		contains pagination information
	 * @param currentPage
	 * 		the current page of the request
	 * @param pageSize
	 * 		total page size
	 * @param responseType
	 * 		the response entity type
	 * @return
	 */
	protected <T> T getDefaultSecuredRestCall(final String path, final String username, final String password, final String fields,
			final String currentPage, final String pageSize, final Class<T> responseType)
	{
		final Response result = getWsSecuredRequestBuilder().resourceOwner(username, password)
				.client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET).grantResourceOwnerPasswordCredentials().path(path)
				.queryParam("fields", fields).queryParam("currentPage", currentPage).queryParam("pageSize", pageSize).build()
				.accept(MediaType.APPLICATION_JSON).get();
		result.bufferEntity();
		return result.readEntity(responseType);
	}

	/**
	 * Builds a POST rest call with the return type <T>.
	 *
	 * @param path
	 * 		the url for the call
	 * @param username
	 * 		the username used for authentication
	 * @param password
	 * 		the password used for authentication
	 * @param fields
	 * 		contains pagination information
	 * @param requestBodyWsDTO
	 * 		the dto object sent with the request
	 * @param responseType
	 * 		type of class to return
	 * @param <T>
	 * 		type of the body object
	 * @return the request class to return after the execution of the call
	 */
	protected <T> T postDefaultSecuredRestCall(final String path, final String username, final String password,
			final String fields, final T requestBodyWsDTO, final Class<T> responseType)
	{
		return getWsSecuredRequestBuilder().resourceOwner(username, password).client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET)
				.grantResourceOwnerPasswordCredentials().path(path).queryParam("fields", fields).build()
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON), responseType);
	}

	/**
	 * Builds a secured POST rest call.
	 *
	 * @param path
	 * 		the url for the call
	 * @param username
	 * 		the username used for authentication
	 * @param password
	 * 		the password used for authentication
	 * @param fields
	 * 		contains pagination information
	 * @param requestBodyWsDTO
	 * 		the dto object sent with the request
	 * @param <T>
	 * 		type of the body object
	 * @return the result of the call
	 */
	protected <T> Response postDefaultSecuredRestCall(final String path, final String username, final String password,
			final String fields, final T requestBodyWsDTO)
	{
		final Response result = getWsSecuredRequestBuilder().resourceOwner(username, password)
				.client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET).grantResourceOwnerPasswordCredentials().path(path)
				.queryParam("fields", fields).build().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON));
		result.bufferEntity();
		return result;
	}

	/**
	 * Builds a secured POST rest call with an empty body
	 *
	 * @param path
	 * 		the url for the call
	 * @param username
	 * 		the username for authentication
	 * @param password
	 * 		the password for authentication
	 * @return the result of the call
	 */
	protected Response postEmptyBodySecuredRestCall(final String path, final String username, final String password)
	{
		return getWsSecuredRequestBuilder().resourceOwner(username, password).client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET)
				.grantResourceOwnerPasswordCredentials().path(path).build().post(Entity.entity(null, MediaType.APPLICATION_JSON));
	}

	/**
	 * Builds a PUT rest call
	 *
	 * @param path
	 * 		the url for the call
	 * @param username
	 * 		the username used for authentication
	 * @param password
	 * 		the password used for authentication
	 * @param fields
	 * 		contains pagination information
	 * @param requestBodyWsDTO
	 * 		the current dto which is to be updated
	 * @param responseType
	 * 		type of class to return
	 * @param <T>
	 * @return the request class to return after the execution of the call
	 */
	protected <S, T> T putDefaultSecuredRestCall(final String path, final String username, final String password,
			final String fields, final S requestBodyWsDTO, final Class<T> responseType)
	{
		return getWsSecuredRequestBuilder().resourceOwner(username, password).client(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET)
				.grantResourceOwnerPasswordCredentials().path(path).queryParam("fields", fields).build().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(requestBodyWsDTO, MediaType.APPLICATION_JSON), responseType);
	}

	/**
	 * Validates the first {@link ErrorWsDTO} content of the {@link ErrorListWsDTO} of the bad request
	 *
	 * @param response
	 * 		bad request response
	 * @param errorReason
	 * 		error reason
	 * @param errorSubject
	 * 		error subject
	 * @param ErrorSubjectType
	 * 		error subject type
	 */
	protected void assertBadRequestWithContent(final Response response, final String errorReason, final String errorSubject,
			final String ErrorSubjectType)
	{
		assertResponse(Status.BAD_REQUEST, Optional.empty(), response);
		final ErrorListWsDTO errors = response.readEntity(ErrorListWsDTO.class);
		assertNotNull(errors);
		assertNotNull(errors.getErrors());
		assertEquals(errors.getErrors().size(), 1);
		final ErrorWsDTO error = errors.getErrors().get(0);
		assertEquals(error.getReason(), errorReason);
		assertEquals(error.getSubject(), errorSubject);
		assertEquals(error.getSubjectType(), ErrorSubjectType);
	}

	/**
	 * Retrieves a new {@link WsSecuredRequestBuilder} in order to build web requests.
	 *
	 * @return the {@link WsSecuredRequestBuilder}
	 */
	protected WsSecuredRequestBuilder getWsSecuredRequestBuilder()
	{
		return new WsSecuredRequestBuilder().extensionName(OrdermanagementwebservicesConstants.EXTENSIONNAME);
	}

	/**
	 * Gets a {@link List} of all extensions loaded in the current setup.
	 *
	 * @return populated {@link List} of all loaded extensions
	 */
	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected SetupImpexService getSetupImpexService()
	{
		return setupImpexService;
	}

	public void setSetupImpexService(final SetupImpexService setupImpexService)
	{
		this.setupImpexService = setupImpexService;
	}

	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
