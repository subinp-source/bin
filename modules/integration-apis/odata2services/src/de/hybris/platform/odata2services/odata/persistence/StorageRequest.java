/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence;

import static de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest.itemLookupRequestBuilder;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.impl.DefaultPersistenceContext;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.ImmutableItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;

import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.google.common.base.Preconditions;

/**
 * Request which contains an item for persistence.
 */
public class StorageRequest extends CrudRequest implements PersistenceContext
{
	private final DefaultPersistenceContext persistenceContext;
	private String postPersistHook;
	private String prePersistHook;

	private StorageRequest(final DefaultPersistenceContext ctx, final EdmEntitySet entitySet) throws EdmException
	{
		Preconditions.checkArgument(ctx != null, "DefaultPersistenceContext cannot be null");
		Preconditions.checkArgument(entitySet != null, "EdmEntitySet cannot be null");
		Preconditions.checkArgument(entitySet.getEntityType() != null, "EdmEntityType cannot be null");

		persistenceContext = ctx;
		setEntitySet(entitySet);
		setEntityType(entitySet.getEntityType());
	}

	public static StorageRequestBuilder storageRequestBuilder()
	{
		return new StorageRequestBuilder();
	}

	@Nonnull
	@Override
	public IntegrationItem getIntegrationItem()
	{
		return persistenceContext.getIntegrationItem();
	}

	public String getPrePersistHook()
	{
		return prePersistHook != null ? prePersistHook : "";
	}

	public String getPostPersistHook()
	{
		return postPersistHook != null ? postPersistHook : "";
	}

	@Nonnull
	@Override
	public Locale getAcceptLocale()
	{
		return persistenceContext.getAcceptLocale();
	}

	protected void setPostPersistHook(final String postPersistHook)
	{
		this.postPersistHook = postPersistHook;
	}

	protected void setPrePersistHook(final String prePersistHook)
	{
		this.prePersistHook = prePersistHook;
	}

	/**
	 * Creates an {@link ItemLookupRequest} from this {@link StorageRequest}
	 *
	 * @return the newly constructed ItemLookupRequest
	 * @throws EdmException if encounters an OData problem
	 * @deprecated replaced with {@link #toItemSearchRequest()}
	 */
	@Deprecated(since = "2105", forRemoval = true)
	public ItemLookupRequest toLookupRequest() throws EdmException
	{
		return itemLookupRequestBuilder()
				.withAcceptLocale(getAcceptLocale())
				.withEntitySet(getEntitySet())
				.withIntegrationObject(getIntegrationObjectCode())
				.withODataEntry(getODataEntry())
				.withServiceRoot(getServiceRoot())
				.withContentType(getContentType())
				.withRequestUri(getRequestUri())
				.withIntegrationItem(getIntegrationItem())
				.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Nonnull
	@Override
	public ItemSearchRequest toItemSearchRequest()
	{
		final ImmutableItemSearchRequest.Builder searchRequestBuilder = new ImmutableItemSearchRequest.Builder();
		return searchRequestBuilder
				.withIntegrationItem(getIntegrationItem())
				.withLocale(getAcceptLocale())
				.build();
	}

	@Nonnull
	@Override
	public Locale getContentLocale()
	{
		return persistenceContext.getContentLocale();
	}

	@Override
	public PersistenceContext getReferencedContext(final TypeAttributeDescriptor attribute)
	{
		return persistenceContext.getReferencedContext(attribute);
	}

	@Override
	public Collection<PersistenceContext> getReferencedContexts(final TypeAttributeDescriptor attribute)
	{
		return persistenceContext.getReferencedContexts(attribute);
	}

	@Override
	public boolean isReplaceAttributes()
	{
		return persistenceContext.isReplaceAttributes();
	}

	@Override
	public boolean isItemCanBeCreated()
	{
		return persistenceContext.isItemCanBeCreated();
	}

	@Override
	public Optional<PersistenceContext> getSourceContext()
	{
		return persistenceContext.getSourceContext();
	}

	@Override
	public Optional<ItemModel> getContextItem()
	{
		return persistenceContext.getContextItem();
	}

	@Override
	public void putItem(final ItemModel item)
	{
		persistenceContext.putItem(item);
	}

	@Nonnull
	@Override
	public PersistenceContext getRootContext()
	{
		return persistenceContext.getRootContext();
	}

	public static class StorageRequestBuilder
	{
		private EdmEntitySet entitySet;
		private ODataEntry oDataEntry;
		private URI serviceRoot;
		private URI requestUri;
		private String contentType;
		private String integrationObjectCode;
		private String prePersistHook;
		private String postPersistHook;
		private String integrationKey;
		private final DefaultPersistenceContext.DefaultPersistenceContextBuilder contextBuilder;

		private StorageRequestBuilder()
		{
			this(DefaultPersistenceContext.persistenceContextBuilder());
		}

		private StorageRequestBuilder(final DefaultPersistenceContext.DefaultPersistenceContextBuilder builder)
		{
			contextBuilder = builder;
		}

		public StorageRequestBuilder withEntitySet(final EdmEntitySet set)
		{
			entitySet = set;
			return this;
		}

		/**
		 * Specifies OData entry associated with the request
		 * @param entry an OData entry to be handled by the request
		 * @return a builder with the OData entry specified
		 * @deprecated since 1905. Use {@link #withIntegrationItem(IntegrationItem)} instead
		 */
		@Deprecated(since = "1905", forRemoval= true )
		public StorageRequestBuilder withODataEntry(final ODataEntry entry)
		{
			oDataEntry = entry;
			return this;
		}

		public StorageRequestBuilder withServiceRoot(final URI uri)
		{
			serviceRoot = uri;
			return this;
		}

		public StorageRequestBuilder withContentType(final String mimeType)
		{
			contentType = mimeType;
			return this;
		}

		public StorageRequestBuilder withRequestUri(final URI uri)
		{
			requestUri = uri;
			return this;
		}

		public StorageRequestBuilder withIntegrationObject(final String code)
		{
			integrationObjectCode = code;
			return this;
		}

		/**
		 * Specifies integration key for the request to build
		 * @param key integration key value
		 * @return a builder with the integration key specified
		 * @deprecated the method has no effect because the only way to specify an integration key is to specify
		 * the integration item, i.e. {@link #withIntegrationItem(IntegrationItem)}
		 */
		@Deprecated(since = "2105", forRemoval = true)
		public StorageRequestBuilder withIntegrationKey(final String key)
		{
			integrationKey = key;
			return this;
		}

		public StorageRequestBuilder withPrePersistHook(final String hook)
		{
			prePersistHook = hook;
			return this;
		}

		public StorageRequestBuilder withPostPersistHook(final String hook)
		{
			postPersistHook = hook;
			return this;
		}

		public StorageRequestBuilder withIntegrationItem(final IntegrationItem item)
		{
			contextBuilder.withIntegrationItem(item);
			return this;
		}

		public StorageRequestBuilder withAcceptLocale(final Locale locale)
		{
			contextBuilder.withAcceptLocale(locale);
			return this;
		}

		public StorageRequestBuilder withContentLocale(final Locale locale)
		{
			contextBuilder.withContentLocale(locale);
			return this;
		}

		public StorageRequestBuilder withReplaceAttributes(final boolean flag)
		{
			contextBuilder.withReplaceAttributes(flag);
			return this;
		}

		public StorageRequestBuilder withItemCanBeCreated(final boolean value)
		{
			contextBuilder.withItemCanBeCreated(value);
			return this;
		}

		public StorageRequestBuilder from(final StorageRequest request)
		{
			final var ctxBuilder = contextBuilder.from(request.persistenceContext);
			return new StorageRequestBuilder(ctxBuilder)
					.withODataEntry(request.getODataEntry())
					.withEntitySet(request.getEntitySet())
					.withIntegrationObject(request.getIntegrationObjectCode())
					.withPrePersistHook(request.getPrePersistHook())
					.withPostPersistHook(request.getPostPersistHook());
		}
		
		public final StorageRequest build() throws EdmException
		{
			final StorageRequest request = new StorageRequest(contextBuilder.build(), entitySet);
			request.setIntegrationObjectCode(integrationObjectCode);
			request.setIntegrationKey(integrationKey);
			request.setODataEntry(oDataEntry);
			request.setServiceRoot(serviceRoot);
			request.setRequestUri(requestUri);
			request.setContentType(contentType);
			request.setPrePersistHook(prePersistHook);
			request.setPostPersistHook(postPersistHook);
			return request;
		}
	}
}
