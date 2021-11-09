/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.EdmAnnotationUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

public class ItemConversionRequest extends AbstractRequest
{
	private static final Logger LOG = Log.getLogger(ItemConversionRequest.class);
	private static final int CONVERSION_LEVEL_LIMIT = 20;

	private Object value;
	private int conversionLevel;
	private ConversionOptions options;

	protected ItemConversionRequest()
	{
		options = new ConversionOptions();
	}

	public static ItemConversionRequestBuilder itemConversionRequestBuilder()
	{
		return new ItemConversionRequestBuilder(new ItemConversionRequest());
	}

	/**
	 * @deprecated use {@link #getValue()}. If item model was passed to {@link #setValue(Object)} method, this method will return the
	 * set value; otherwise this method returns {@code null} and {@link #getValue()} returns the set value.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public ItemModel getItemModel()
	{
		return value instanceof ItemModel
				? (ItemModel) value
				: null;
	}

	public Object getValue()
	{
		return value;
	}

	public ConversionOptions getOptions()
	{
		return options;
	}

	/**
	 * @deprecated use {@link #setValue(Object)}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected void setItemModel(final ItemModel itemModel)
	{
		setValue(itemModel);
	}

	protected void setValue(final Object value)
	{
		this.value = value;
	}

	protected void setOptions(final ConversionOptions options)
	{
		this.options = options != null
				? options
				: ConversionOptions.conversionOptionsBuilder().build();
	}

	/**
	 * @deprecated use {@link #propertyConversionRequest(String, Object)}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public ItemConversionRequest propertyConversionRequest(final String propertyName, final ItemModel item) throws EdmException
	{
		return propertyConversionRequest(propertyName, (Object) item);
	}

	public ItemConversionRequest propertyConversionRequest(final String propertyName, final Object value) throws EdmException
	{
		final ItemConversionRequest subrequest = itemConversionRequestBuilder().from(this)
		                                                                       .withOptions(getOptions().navigate(propertyName))
		                                                                       .withEntitySet(getEntitySetReferencedByProperty(
				                                                                       propertyName))
		                                                                       .withValue(value)
		                                                                       .build();
		subrequest.conversionLevel = conversionLevel + 1;
		return subrequest;
	}

	/**
	 * Determines how deep the item being converted by this request is located in the object graph. Every time
	 * {@link #propertyConversionRequest(String, Object)} is called it increases the conversion level (the distance of the
	 * resulting request from the very original conversion request).
	 *
	 * @return number of times {@link #propertyConversionRequest(String, Object)} was called from the original request to get
	 * this request or 0, if this request is the original request. For example, if an item {@code Product} has property "catalog",
	 * which refers to {@code Catalog} item, then conversion request for {@code Product} item will have conversion level 0,
	 * conversion request for {@code Catalog} item will have conversion level 1, and so on for any subsequent item referenced from
	 * the {@code Catalog}.
	 */
	public int getConversionLevel()
	{
		return conversionLevel;
	}

	/**
	 * Determines whether the specified property value should be subsequently converted to an ODataEntry or not.
	 *
	 * @param property name of the item property
	 * @return {@code true}, if the property is a key property or it is next segment in the navigation/expand path specified for
	 * the request; {@code false}, otherwise.
	 */
	public boolean isPropertyValueShouldBeConverted(final String property)
	{
		return getConversionLevel() < CONVERSION_LEVEL_LIMIT
				&& (isKeyProperty(property) || options.isNextNavigationSegment(property) || options.isNextExpandSegment(property));
	}

	public List<String> getAllPropertyNames() throws EdmException
	{
		return Stream.of(getEntityType().getPropertyNames(), getEntityType().getNavigationPropertyNames())
		             .filter(Objects::nonNull)
		             .flatMap(Collection::stream)
		             .collect(Collectors.toList());
	}

	private boolean isKeyProperty(final String name)
	{
		try
		{
			final EdmTyped np = getEntityType().getProperty(name);
			return EdmAnnotationUtils.isKeyProperty(np);
		}
		catch (final EdmException e)
		{
			LOG.debug("Failed to determine whether '{}' is a key property", name, e);
			return false;
		}
	}

	public static class ItemConversionRequestBuilder
			extends AbstractRequestBuilder<ItemConversionRequestBuilder, ItemConversionRequest>
	{
		ItemConversionRequestBuilder(final ItemConversionRequest conversionRequest)
		{
			super(conversionRequest);
		}


		/**
		 * @deprecated use {@link #withValue(Object)} instead.
		 */
		@Deprecated(since = "1905.09-CEP", forRemoval = true)
		public ItemConversionRequestBuilder withItemModel(final ItemModel itemModel)
		{
			return withValue(itemModel);
		}

		public ItemConversionRequestBuilder withValue(final Object value)
		{
			request().setValue(value);
			return myself();
		}

		public ItemConversionRequestBuilder withOptions(final ConversionOptions.ConversionOptionsBuilder builder)
		{
			return builder != null
					? withOptions(builder.build())
					: withOptions((ConversionOptions) null);
		}

		public ItemConversionRequestBuilder withOptions(final ConversionOptions options)
		{
			request().setOptions(options);
			return myself();
		}

		ItemConversionRequestBuilder withConversionLevel(final int level)
		{
			request().conversionLevel = level;
			return myself();
		}

		@Override
		public ItemConversionRequestBuilder from(final ItemConversionRequest request)
		{
			return super.from(request)
			            .withItemModel(request.getItemModel())
			            .withValue(request.getValue())
			            .withOptions(request.getOptions())
			            .withConversionLevel(request.getConversionLevel());
		}

		@Override
		protected void assertValidValues() throws EdmException
		{
			super.assertValidValues();
			Preconditions.checkArgument(request().getIntegrationObjectCode() != null, "IntegrationObject must be provided");
			Preconditions.checkArgument(request().getValue() != null, "value cannot be null");
		}
	}
}
