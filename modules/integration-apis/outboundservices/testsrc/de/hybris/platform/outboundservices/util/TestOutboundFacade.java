/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.util;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.enums.OutboundSource;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.outboundservices.facade.SyncParameters;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import rx.Observable;

public class TestOutboundFacade extends ExternalResource implements OutboundServiceFacade
{
	private static final Logger LOG = Log.getLogger(TestOutboundFacade.class);
	private static final URI SOME_URI = createUri();
	private static final ResponseEntity<Map> DEFAULT_RESPONSE = ResponseEntity.created(SOME_URI).build();

	private final Collection<Invocation> invocations;
	private final Queue<Supplier<ResponseEntity<Map>>> responseQueue;
	private Supplier<ResponseEntity<Map>> defaultResponseSupplier;

	/**
	 * Instantiates this facade, which by default, if nothing else is specified will respond with the CREATED status and an empty
	 * body.
	 */
	public TestOutboundFacade()
	{
		defaultResponseSupplier = () -> DEFAULT_RESPONSE;
		invocations = Collections.synchronizedList(new ArrayList<>());
		responseQueue = new ConcurrentLinkedQueue<>();
	}

	private static URI createUri()
	{
		try
		{
			return new URI("//does.not/matter");
		}
		catch (final URISyntaxException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Specifies to respond with HTTP 201 Created to all requests it receives, unless this spec is overridden then by a subsequent
	 * call to a {@code respondWith...} or {@code throwException} method
	 *
	 * @return test facade instance stubbed for CREATED response.
	 */
	public TestOutboundFacade respondWithCreated()
	{
		return respondWith(ResponseEntity.created(SOME_URI));
	}

	/**
	 * Specifies to respond with HTTP 404 Not Found to all requests it receives, unless this spec is not overridden then by a
	 * subsequent call to a {@code respondWith...} or {@code throwException} method.
	 *
	 * @return facade with the response specification applied.
	 */
	public TestOutboundFacade respondWithNotFound()
	{
		return respondWith(ResponseEntity.notFound());
	}

	/**
	 * Specifies to respond with HTTP 400 Bad Request to all requests it receives, unless this spec is not overridden then by a
	 * subsequent call to a {@code respondWith...} or {@code throwException} method.
	 *
	 * @return facade with the response specification applied.
	 */
	public TestOutboundFacade respondWithBadRequest()
	{
		return respondWith(ResponseEntity.badRequest());
	}

	/**
	 * Specifies to respond with HTTP 500 Internal Server Error to all requests it receives, unless this spec is not overridden then by a
	 * subsequent call to a {@code respondWith...} or {@code throwException} method.
	 *
	 * @return facade with the response specification applied.
	 */
	public TestOutboundFacade respondWithServerError()
	{
		return respondWith(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * Specifies to throw an exception when this facade's {@link #send(ItemModel, String, String)} method is called.
	 *
	 * @param e an exception to throw
	 * @return facade with the behavior applied
	 */
	public TestOutboundFacade throwException(final RuntimeException e)
	{
		return respondWith(new ExceptionResponse(e));
	}

	/**
	 * Specifies to perform an action before return the specified response from the {@link #send(ItemModel, String, String)} method.
	 *
	 * @param builder a builder for the response to return.
	 * @param act     an action to perform before the response is returned. This allows to inject time-sensitive behavior
	 *                to the test to call certain code when a desired item is being processed instead of relying on guessing the
	 *                right time to execute that code in parallel.
	 * @return a facade with the response specified.
	 */
	public TestOutboundFacade doAndRespondWith(final ResponseEntity.HeadersBuilder builder, final Runnable act)
	{
		final Supplier<ResponseEntity<Map>> supplier = new EntityResponse(builder, act);
		return respondWith(supplier);
	}

	private TestOutboundFacade respondWith(final ResponseEntity.HeadersBuilder builder)
	{
		final Supplier<ResponseEntity<Map>> supplier = new EntityResponse(builder);
		return respondWith(supplier);
	}

	private TestOutboundFacade respondWith(final Supplier<ResponseEntity<Map>> response)
	{
		responseQueue.add(response);
		defaultResponseSupplier = response;
		return this;
	}

	@Override
	public synchronized Observable<ResponseEntity<Map>> send(final ItemModel itemModel, final String integrationObjectCode,
	                                                         final String destination)
	{
		LOG.info("Sending {} item {} to {}", integrationObjectCode, itemModel, destination);
		final SyncParameters syncParameters = SyncParameters.syncParametersBuilder().withItem(itemModel)
		                                                    .withIntegrationObjectCode(integrationObjectCode)
		                                                    .withDestinationId(destination)
		                                                    .withSource(OutboundSource.OUTBOUNDSYNC)
		                                                    .build();
		return this.send(syncParameters);
	}

	@Override
	public synchronized Observable<ResponseEntity<Map>> send(final SyncParameters parameters)
	{
		invocations.add(new Invocation(parameters));
		final var responseSupplier = nextResponseSupplier();
		LOG.info("Responding with {}", responseSupplier);

		return Observable.just(responseSupplier.get());
	}

	private Supplier<ResponseEntity<Map>> nextResponseSupplier()
	{
		final var response = responseQueue.poll();
		return response != null ? response : defaultResponseSupplier;
	}

	public int invocations()
	{
		return invocations.size();
	}

	@Override
	protected void after()
	{
		responseQueue.clear();
		invocations.clear();
	}

	/**
	 * Retrieves items captured by this facade.
	 *
	 * @param dest   consumed destination, to which the items should have been sent.
	 * @param ioCode code of IntegrationObject used for the items sent.
	 * @return a collection of items send to the specified destination with the specified IntegrationObject code or an empty
	 * collection, if no items were sent with the specified parameters.
	 */
	Collection<ItemModel> itemsFromInvocationsTo(final ConsumedDestinationModel dest, final String ioCode)
	{
		return itemsFromInvocationsTo(dest.getId(), ioCode);
	}

	/**
	 * Retrieves items captured by this facade.
	 *
	 * @param dest   specifies consumed destination ID, to which the items should have been sent.
	 * @param ioCode code of IntegrationObject used for the items sent.
	 * @return a collection of items send to the specified destination with the specified IntegrationObject code or an empty
	 * collection, if no items were sent with the specified parameters.
	 */
	Collection<ItemModel> itemsFromInvocationsTo(final String dest, final String ioCode)
	{
		return invocations.stream()
		                  .filter(inv -> inv.matches(dest, ioCode))
		                  .map(Invocation::getItemModel)
		                  .collect(Collectors.toList());
	}

	private static class Invocation
	{
		private final String destination;
		private final String integrationObject;
		private final ItemModel item;

		Invocation(final ItemModel it, final String ioCode, final String dest)
		{
			destination = dest;
			integrationObject = ioCode;
			item = it;
		}

		Invocation(final SyncParameters params)
		{
			destination = params.getDestinationId();
			integrationObject = params.getIntegrationObjectCode();
			item = params.getItem();
		}

		ItemModel getItemModel()
		{
			return item;
		}

		boolean matches(final String dest, final String ioCode)
		{
			return destination.equals(dest) && integrationObject.equals(ioCode);
		}
	}

	private static class EntityResponse implements Supplier<ResponseEntity<Map>>
	{
		private final ResponseEntity<Map> entity;
		private final Runnable action;

		private EntityResponse(final ResponseEntity.HeadersBuilder builder)
		{
			this(builder, null);
		}

		public EntityResponse(final ResponseEntity.HeadersBuilder builder, final Runnable toDo)
		{
			this(builder.build(), toDo);
		}

		private EntityResponse(final ResponseEntity<Map> e, final Runnable toDo)
		{
			entity = e;
			action = toDo;
		}

		@Override
		public ResponseEntity<Map> get()
		{
			if (action != null)
			{
				LOG.info("Executing {}", action);
				action.run();
			}
			return entity;
		}

		@Override
		public String toString()
		{
			return action != null
					? action.toString() + " -> " + entity.toString()
					: entity.toString();
		}
	}

	private static class ExceptionResponse implements Supplier<ResponseEntity<Map>>
	{
		private final RuntimeException exception;

		private ExceptionResponse(final RuntimeException e)
		{
			exception = e;
		}

		@Override
		public ResponseEntity<Map> get()
		{
			throw exception;
		}

		@Override
		public String toString()
		{
			return exception.toString();
		}
	}
}
