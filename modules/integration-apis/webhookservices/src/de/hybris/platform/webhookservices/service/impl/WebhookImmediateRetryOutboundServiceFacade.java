/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.outboundservices.facade.SyncParameters;
import de.hybris.platform.outboundservices.facade.impl.DefaultOutboundServiceFacade;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;

import com.google.common.base.Preconditions;

import rx.Observable;
import rx.exceptions.OnErrorFailedException;

/**
 * Implements the {@link OutboundServiceFacade} interface with the {@link SimpleRetryPolicy}
 * to enable resending in case of server error failures.
 * The actual sending is delegated to the default implementation {@link DefaultOutboundServiceFacade}.
 */
public class WebhookImmediateRetryOutboundServiceFacade implements OutboundServiceFacade
{
	private static final Logger LOGGER = Log.getLogger(WebhookImmediateRetryOutboundServiceFacade.class);
	private final RetryTemplate webhookServicesRetryTemplate;
	private OutboundServiceFacade outboundServiceFacade;

	/**
	 * Constructs a new WebhookOutboundServiceFacade.
	 *
	 * @param outboundServiceFacade OutboundServiceFacade used to send the event out to the destination
	 * @param retryTemplate         RetryTemplate to enable resending in case of getting server errors
	 */
	public WebhookImmediateRetryOutboundServiceFacade(@NotNull final OutboundServiceFacade outboundServiceFacade,
	                                                  @NotNull final RetryTemplate retryTemplate)
	{
		Preconditions.checkArgument(outboundServiceFacade != null, "OutboundServiceFacade cannot be null!");
		Preconditions.checkArgument(retryTemplate != null, "RetryTemplate cannot be null!");
		this.outboundServiceFacade = outboundServiceFacade;
		this.webhookServicesRetryTemplate = retryTemplate;
	}

	@Override
	public Observable<ResponseEntity<Map>> send(final ItemModel itemModel, final String integrationObjectCode,
	                                            final String destination)
	{
		final SyncParameters params = SyncParameters.syncParametersBuilder()
		                                            .withItem(itemModel)
		                                            .withIntegrationObjectCode(integrationObjectCode)
		                                            .withDestinationId(destination)
		                                            .build();
		return send(params);
	}

	/**
	 * {@inheritDoc}
	 * The webhook retry template delegates the sending to the default implementation {@link DefaultOutboundServiceFacade}.
	 * The send action will be attempted twice in case of server error failure, that throws {@link WebhookRetryableException}
	 * to trigger the retry mechanism.
	 *
	 * @param params {@link SyncParameters} object which contains the information relevant to perform an outbound request
	 *               action, containing the item, integration object and destination
	 * @return Observable<ResponseEntity < Map>> with the response from the outbound request
	 */
	@Override
	public Observable<ResponseEntity<Map>> send(final SyncParameters params)
	{
		try
		{
			return webhookServicesRetryTemplate.execute(
					retryContext -> sendIntegrationObject(params));
		}
		catch (final OnErrorFailedException ex)
		{
			return Observable.error(new WebhookRetryableException("All webhook sending attempts failed after retry!", ex));
		}
	}

	private Observable<ResponseEntity<Map>> sendIntegrationObject(final SyncParameters params)
	{
		final AtomicReference<Observable<ResponseEntity<Map>>> newResponseEntity = new AtomicReference<>();

		outboundServiceFacade.send(params).subscribe(response -> {
					newResponseEntity.set(Observable.just(response));
					handleResponse(response);
				},
				error -> {
					newResponseEntity.set(Observable.error(error));
					handleException(params, error);
				});

		return newResponseEntity.get();
	}

	private void handleResponse(final ResponseEntity<Map> response)
	{
		checkForServerError(response.getStatusCode());
	}

	private void handleException(final SyncParameters params, final Throwable error)
	{
		LOGGER.debug("Failed attempt to send item of type '{}' to destination with id '{}' using integration object '{}'",
				params.getItem().getItemtype(), params.getDestination().getId(), params.getIntegrationObject().getCode());

		if (error instanceof HttpServerErrorException)
		{
			checkForServerError(((HttpServerErrorException) error).getStatusCode());
		}
		else if (error instanceof WebhookRetryableException)
		{
			throw (WebhookRetryableException) error;
		}
	}

	private void checkForServerError(final HttpStatus httpStatus)
	{
		if (httpStatus.is5xxServerError())
		{
			throw new WebhookRetryableException("Webhook server error exception!");
		}
	}

	void setOutboundServiceFacade(final OutboundServiceFacade outboundServiceFacade)
	{
		this.outboundServiceFacade = outboundServiceFacade;
	}
}
