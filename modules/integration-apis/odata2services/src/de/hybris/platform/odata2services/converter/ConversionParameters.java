/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpHeaders;
import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * Parameters providing context for converting an attribute value in the payload. Depending on the attribute type the value
 * may be converted into a collection of primitives or of {@link IntegrationItem}s, or a primitive, an {@link IntegrationItem},
 * a {@link java.util.Map}, etc. value.
 */
public class ConversionParameters
{
	private static final String PATCH_METHOD = "PATCH";

	private String attributeName;
	private Object attributeValue;
	private ODataContext context;
	private IntegrationItem integrationItem;
	private Locale contentLocale;

	private ConversionParameters()
	{
		// non-instantiable for immutability
	}

	/**
	 * Retrieves name of the attribute, whose value is being converted.
	 *
	 * @return name of the attribute to convert value for.
	 */
	public String getAttributeName()
	{
		return attributeName;
	}

	/**
	 * Retrieves value to be converted.
	 *
	 * @return value the attribute has in ODataEntry and, which has to be converted to be acceptable by
	 * OData independent {@link IntegrationItem}.
	 */
	public Object getAttributeValue()
	{
		return attributeValue;
	}

	/**
	 * Retrieves OData request context.
	 *
	 * @return OData context describing the request.
	 */
	public ODataContext getContext()
	{
		return context;
	}

	/**
	 * Retrieves {@code IntegrationItem}, in which the converted attribute value will be set.
	 *
	 * @return item to set converted value to.
	 */
	public IntegrationItem getIntegrationItem()
	{
		return integrationItem;
	}

	/**
	 * Retrieves type descriptor for the attribute to set in the {@link IntegrationItem}
	 *
	 * @return type descriptor for the attribute to set, if the ODataEntry attribute exists in the
	 * corresponding type in the type system or {@code null} otherwise.
	 * @see #getIntegrationItem()
	 */
	public TypeAttributeDescriptor getTypeAttributeDescriptor()
	{
		return attribute().orElse(null);
	}

	/**
	 * Retrieves type descriptor for the type in the type system corresponding to the ODataEntry being converted.
	 *
	 * @return IntegrationItem type.
	 * @see #getIntegrationItem()
	 */
	public TypeDescriptor getTypeDescriptor()
	{
		return integrationItem.getItemType();
	}

	/**
	 * Specifies the request content language.
	 *
	 * @return a locale corresponding to the value specified in the Content-Language header or the default system locale, if
	 * the content language was not specified explicitly.
	 * @see #isContentLanguagePresent()
	 */
	public Locale getContentLocale()
	{
		return contentLocale;
	}

	/**
	 * Determines whether attribute values should be replaced or appended in those case when append is possible.
	 *
	 * @return {@code true}, if the attribute value should replace current value in the item existing in the platform;
	 * {@code false}, if the item in the platform can have multiple values, e.g. when the value is a collection
	 * or a map, and the values present in the ODataEntry attribute should be appended to the values in the
	 * platform item.
	 */
	boolean isReplaceAttributesRequest()
	{
		return context != null && PATCH_METHOD.equalsIgnoreCase(context.getHttpMethod());
	}

	/**
	 * Determines whether the ODataEntry attribute being converted corresponds to a Map attribute in the platform.
	 *
	 * @return {@code true}, if the context attribute is a Map attribute in the type system; {@code false} otherwise.
	 */
	public boolean isMapAttributeValue()
	{
		return attribute()
				.filter(a -> !a.isLocalized())
				.map(TypeAttributeDescriptor::isMap)
				.orElse(false);
	}

	/**
	 * Determines whether Content-Language was explicitly specified for the request.
	 *
	 * @return {@code true}, if the Content-Language is present in the request; {@code false}, otherwise.
	 */
	public boolean isContentLanguagePresent()
	{
		return context.getRequestHeader(HttpHeaders.CONTENT_LANGUAGE) != null;
	}

	@NotNull
	private Optional<TypeAttributeDescriptor> attribute()
	{
		return getTypeDescriptor().getAttribute(attributeName);
	}

	/**
	 * A builder for creating instances of {@link ConversionParameters}
	 */
	public static class ConversionParametersBuilder
	{
		private String attributeName;
		private Object attributeValue;
		private ODataContext oDataContext;
		private IntegrationItem integrationItem;
		private Locale contentLocale;

		/**
		 * Creates builder instance.
		 *
		 * @return new instance of the builder.
		 */
		public static ConversionParametersBuilder conversionParametersBuilder()
		{
			return new ConversionParametersBuilder();
		}

		/**
		 * Initializes this builder to the values like in the sample parameters provided. So, if a call is
		 * made:
		 * <pre>
		 *     conversionParametersBuilder().from(sample).build();
		 * </pre>
		 * it will produce a different instance of the {@link ConversionParameters}, which is in exactly
		 * same state as the {@code sample}.
		 *
		 * @param parameters a sample to use for initializing builder.
		 * @return a builder set to create a new {@code ConversionParameters} instance that is different from the {@code sample}
		 * but having same state.
		 */
		public ConversionParametersBuilder from(final ConversionParameters parameters)
		{
			return withAttributeName(parameters.getAttributeName())
					.withAttributeValue(parameters.getAttributeValue())
					.withContext(parameters.getContext())
					.withContentLocale(parameters.getContentLocale())
					.withIntegrationItem(parameters.getIntegrationItem());
		}

		/**
		 * Specifies context attribute name for the {@code ConversionParameters} to create.
		 *
		 * @param name name of the ODataEntry attribute being converted.
		 * @return builder with the attribute name specified.
		 */
		public ConversionParametersBuilder withAttributeName(final String name)
		{
			attributeName = name;
			return this;
		}

		/**
		 * Specifies attribute value for the {@code ConversionParameters} to create.
		 *
		 * @param value value of the ODataEntry attribute to be converted.
		 * @return builder with the attribute value specified.
		 */
		public ConversionParametersBuilder withAttributeValue(final Object value)
		{
			attributeValue = value;
			return this;
		}

		/**
		 * Specifies integration item for the {@code ConversionParameters} to create.
		 *
		 * @param item an integration item, in which the converted attribute value should be set.
		 * @return a builder with the integration item specified.
		 */
		public ConversionParametersBuilder withIntegrationItem(final IntegrationItem item)
		{
			integrationItem = item;
			return this;
		}

		/**
		 * Specifies locale of the ODataEntry content.
		 *
		 * @param locale locale of the localized attributes values.
		 * @return a builder with the content locale specified.
		 */
		public ConversionParametersBuilder withContentLocale(final Locale locale)
		{
			contentLocale = locale;
			return this;
		}

		/**
		 * Specifies OData context for the {@code ConversionParameters} to create.
		 *
		 * @param context a context providing additional information for the attribute value conversion.
		 * @return a builder with the OData context specified.
		 */
		public ConversionParametersBuilder withContext(final ODataContext context)
		{
			oDataContext = context;
			return this;
		}

		/**
		 * Creates new {@code ConversionParameters} instance.
		 *
		 * @return an instance that has characteristics that were specified by the {@code with...} methods prior to calling this
		 * method.
		 */
		public ConversionParameters build()
		{
			final var parameters = new ConversionParameters();
			parameters.attributeName = attributeName;
			parameters.attributeValue = attributeValue;
			parameters.integrationItem = integrationItem;
			parameters.context = oDataContext;
			parameters.contentLocale = contentLocale;
			return parameters;
		}
	}
}
