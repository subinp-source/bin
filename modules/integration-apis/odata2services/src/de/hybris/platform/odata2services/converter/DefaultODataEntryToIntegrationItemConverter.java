/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;
import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder;

import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.item.DefaultIntegrationItem;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.item.LocalizedValue;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.ODataContextLanguageExtractor;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

import org.apache.http.HttpHeaders;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

/**
 * Implementation of the converter, which populates {@code IntegrationItem} attributes by delegating conversion of their
 * values in the ODataEntry to {@link PayloadAttributeValueConverter}.
 */
public class DefaultODataEntryToIntegrationItemConverter implements ODataEntryToIntegrationItemConverter
{
	private static final Logger LOG = Log.getLogger(DefaultODataEntryToIntegrationItemConverter.class);
	private static final AttributeValueHandler DEFAULT_ATTRIBUTE_VALUE_HANDLER = IntegrationItem::setAttribute;
	private ModelEntityService modelService;
	private ItemTypeDescriptorService itemTypeDescriptorService;
	private ServiceNameExtractor serviceNameExtractor;
	private ODataContextLanguageExtractor languageExtractor;
	private IntegrationLocalizationService localizationService;
	private IntegrationKeyValueGenerator<TypeDescriptor, IntegrationItem> keyValueGenerator;
	private PayloadAttributeValueConverter attributeValueConverter = new DefaultPayloadAttributeValueConverter();

	@Override
	public IntegrationItem convert(
			@NotNull final ODataContext context,
			@NotNull final EdmEntitySet entitySet,
			@NotNull final ODataEntry entry)
			throws EdmException
	{
		Preconditions.checkArgument(entitySet != null, "Entity set is required for the entry conversion");

		final String typeCode = entitySet.getEntityType().getName();
		final String objectCode = getServiceNameExtractor().extract(context);
		final TypeDescriptor typeDescriptor = getItemTypeDescriptorService()
				.getTypeDescriptor(objectCode, typeCode)
		        .orElseThrow(() -> new IntegrationObjectItemNotFoundException(objectCode, typeCode));
		return convert(context, typeDescriptor, entry);
	}

	@Override
	public IntegrationItem convert(@NotNull final ODataContext context,
	                               @NotNull final TypeDescriptor typeDesc,
	                               @NotNull final EdmEntitySet entitySet,
	                               @NotNull final ODataEntry entry)
	{
		return convert(context, typeDesc, entry);
	}

	@Override
	public IntegrationItem convert(@NotNull final ODataContext context,
	                               @NotNull final TypeDescriptor typeDesc,
	                               @NotNull final ODataEntry entry)
	{
		return convert(context, typeDesc, entry, null);
	}

	@Override
	public IntegrationItem convert(final ODataContext context,
	                               final TypeDescriptor typeDesc,
	                               final ODataEntry entry,
	                               final IntegrationItem parentItem)
	{
		Preconditions.checkArgument(context != null, "ODataContext is required for the entry conversion");
		Preconditions.checkArgument(entry != null, "Entry is required for the conversion");

		final DefaultIntegrationItem item = createIntegrationItem(typeDesc, parentItem);
		convertAllKeyAttributes(context, typeDesc, entry, item);
		populateMissingReferencedAttributesFromContext(item, TypeAttributeDescriptor::isKeyAttribute);
		item.setIntegrationKey(getKeyValueGenerator().generate(typeDesc, item));
		convertNonKeyAttributes(context, entry, item);
		populateMissingReferencedAttributesFromContext(item, this::isRequiredSimpleReferenceAttribute);
		return item;
	}

	/**
	 * Instantiates an item that will be populated and used as result of {@code convert(...)} methods call.
	 * @param typeDesc non-null type descriptor describing type of the integration item to create.
	 * @param contextItem a container (outer) item that references the item being created through one of its attributes.
	 *                    {@code null}, if this item is the top level item in the request payload.
	 * @return new instance of the integration item that corresponds to the ODataEntry passed into {@code convert(...)}
	 * methods
	 */
	protected DefaultIntegrationItem createIntegrationItem(final TypeDescriptor typeDesc, final IntegrationItem contextItem)
	{
		return new DefaultIntegrationItem(typeDesc, contextItem);
	}

	private void convertAllKeyAttributes(final ODataContext context, final TypeDescriptor typeDesc,
										 final ODataEntry entry, final IntegrationItem item)
	{
		for (final Map.Entry<String, Object> ent : entry.getProperties().entrySet())
		{
			@NotNull final Optional<TypeAttributeDescriptor> optionalAttr = typeDesc.getAttribute(ent.getKey());
			if (optionalAttr.isPresent() && optionalAttr.get().isKeyAttribute())
			{
				setAttributeValue(context, item, ent);
			}
		}
	}

	private void convertNonKeyAttributes(final ODataContext context,
	                                     final ODataEntry entry,
	                                     final IntegrationItem item)
	{
		Map.Entry<String, Object> localizedAttributes = null;
		for (final Map.Entry<String, Object> ent : entry.getProperties().entrySet())
		{
			if (LOCALIZED_ATTRIBUTE_NAME.equals(ent.getKey()))
			{
				localizedAttributes = ent;
				continue;
			}
			if (item.getAttribute(ent.getKey()) == null)
			{
				setAttributeValue(context, item, ent);
			}
		}
		overrideValuesWithLocalizedAttributes(context, item, localizedAttributes);
	}

	private void populateMissingReferencedAttributesFromContext(final IntegrationItem item,
																final Predicate<TypeAttributeDescriptor> condition)
	{
		item.getItemType().getAttributes().stream()
		    .filter(descriptor -> item.getAttribute(descriptor) == null && !descriptor.isPrimitive())
		    .filter(condition)
		    .forEach(descriptor -> populateFromContext(item, descriptor));
	}

	private void populateFromContext(final IntegrationItem item, final TypeAttributeDescriptor attribute)
	{
		LOG.debug("The mandatory attribute {} on item {} is not populated. Attempting to deduce from the request.",
				attribute.getAttributeName(), item);
		final Optional<IntegrationItem> contextItem = item.getContextItem(attribute.getAttributeType());

		contextItem.ifPresentOrElse(
				requiredItem -> {
					LOG.debug("Found context item: {}", requiredItem);
					item.setAttribute(attribute.getAttributeName(), requiredItem);
				},
				() -> LOG.debug("Missing required item was not found in the context"));
	}

	private boolean isRequiredSimpleReferenceAttribute(final TypeAttributeDescriptor descriptor) {
		return !descriptor.isKeyAttribute() &&
				!descriptor.isNullable() &&
				!descriptor.isCollection() &&
				!descriptor.isMap();
	}

	private void setAttributeValue(final ODataContext context,
								   final IntegrationItem item,
								   final Map.Entry<String, Object> ent)
	{
		final Locale locale = languageExtractor.extractFrom(context, HttpHeaders.CONTENT_LANGUAGE);
		final ConversionParameters parameters = conversionParametersBuilder()
				.withAttributeName(ent.getKey())
				.withAttributeValue(ent.getValue())
				.withContext(context)
				.withContentLocale(locale)
				.withIntegrationItem(item)
				.build();
		final Object value = attributeValueConverter.convertAttributeValue(parameters);
		getAttributeValueHandler(value).setItemAttribute(item, ent.getKey(), value);
	}

	private void overrideValuesWithLocalizedAttributes(final ODataContext context, final IntegrationItem item,
	                                                   final Map.Entry<String, Object> localizedAttributes)
	{
		if (localizedAttributes != null)
		{
			setAttributeValue(context, item, localizedAttributes);
		}
	}

	static AttributeValueHandler getAttributeValueHandler(final Object value)
	{
		return value instanceof LocalizedAttributes
				? ((i, a, v) -> ((LocalizedAttributes) value).forEachAttribute(setAttribute(i)))
				: DEFAULT_ATTRIBUTE_VALUE_HANDLER;
	}

	private static BiConsumer<String, LocalizedValue> setAttribute(final IntegrationItem item)
	{
		return (attrName, localizedValue) -> {
			var valueToSet = localizedValue;
			final var currentValue = item.getAttribute(attrName);
			if (currentValue instanceof LocalizedValue)
			{
				valueToSet = ((LocalizedValue) currentValue).combine(localizedValue);
			}
			item.setAttribute(attrName, valueToSet);
		};
	}

	/**
	 * Injects {@link ModelEntityService} to use
	 *
	 * @param service a service implementation
	 * @deprecated not used anymore and does not have to be injected.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public void setModelEntityService(final ModelEntityService service)
	{
		modelService = service;
	}

	/**
	 * @deprecated this injected service was used only for calculating integration key and now it's not used. Instead this
	 * implementation uses {@link IntegrationKeyValueGenerator} now
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected ModelEntityService getModelEntityService()
	{
		return modelService;
	}

	/**
	 * Injects implementation of the {@code ItemTypeDescriptorService} to be used for the type descriptor lookup.
	 * This service is needed by {@link #convert(ODataContext, EdmEntitySet, ODataEntry)} method and therefore must be injected,
	 * if this method is called by the application.
	 *
	 * @param service a service to use.
	 */
	public void setItemTypeDescriptorService(final ItemTypeDescriptorService service)
	{
		itemTypeDescriptorService = service;
	}

	protected ItemTypeDescriptorService getItemTypeDescriptorService()
	{
		return itemTypeDescriptorService;
	}

	@Required
	public void setServiceNameExtractor(final ServiceNameExtractor extractor)
	{
		serviceNameExtractor = extractor;
	}

	protected ServiceNameExtractor getServiceNameExtractor()
	{
		return serviceNameExtractor;
	}

	@Required
	public void setLanguageExtractor(final ODataContextLanguageExtractor languageExtractor)
	{
		this.languageExtractor = languageExtractor;
	}

	protected ODataContextLanguageExtractor getLanguageExtractor()
	{
		return languageExtractor;
	}

	/**
	 * @deprecated not used anymore.
	 */
	@Deprecated(since = "1905.2004-CEP", forRemoval = true)
	protected IntegrationLocalizationService getLocalizationService()
	{
		return localizationService;
	}

	/**
	 * @param service implementation to use.
	 * @deprecated not used anymore.
	 */
	@Deprecated(since = "1905.2004-CEP", forRemoval = true)
	@Required
	public void setLocalizationService(final IntegrationLocalizationService service)
	{
		localizationService = service;
	}

	/**
	 * @param generator implementation to use.
	 * @deprecated use {@link #setKeyValueGenerator(IntegrationKeyValueGenerator)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setIntegrationKeyValueGenerator(final IntegrationKeyValueGenerator<EdmEntitySet, ODataEntry> generator)
	{
		/* The service is not used anymore - nothing to do here. This method is left only to keep backwards compatibility. */
	}

	protected IntegrationKeyValueGenerator<TypeDescriptor, IntegrationItem> getKeyValueGenerator()
	{
		return keyValueGenerator;
	}

	@Required
	public void setKeyValueGenerator(final IntegrationKeyValueGenerator<TypeDescriptor, IntegrationItem> generator)
	{
		keyValueGenerator = generator;
	}

	public void setAttributeValueConverter(@NotNull final PayloadAttributeValueConverter converter)
	{
		attributeValueConverter = converter;
	}

	private interface AttributeValueHandler
	{
		void setItemAttribute(IntegrationItem item, String attr, Object value);
	}
}