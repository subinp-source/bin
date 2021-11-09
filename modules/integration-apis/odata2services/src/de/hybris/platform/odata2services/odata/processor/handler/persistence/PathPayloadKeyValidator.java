/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;

import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyCalculationException;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.odata2services.converter.IntegrationObjectItemNotFoundException;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;

import java.util.Optional;
import java.util.Set;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

/**
 * Validates request payload to ensure that the key attributes provided in the payload body matches the integration key value in
 * the request URI. Payload body may not contain key attributes and this case the request is valid.
 */
public class PathPayloadKeyValidator implements RequestValidator
{
	private IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> keyValueGenerator;
	private ServiceNameExtractor serviceNameExtractor;
	private ItemTypeDescriptorService itemTypeDescriptorService;

	@Override
	public void validate(final PersistenceParam param, final ODataEntry path, final ODataEntry payload)
	{
		if (proceedToValidate(path, payload))
		{
			final Set<String> keyPropertyNames = path.getProperties().keySet();
			keyPropertyNames.stream()
					.filter(name -> !INTEGRATION_KEY_PROPERTY_NAME.equals(name))
					.filter(name -> !keysEqual(name, path, payload))
					.findFirst()
					.ifPresent(mismatch -> throwPathPayloadKeyMismatchException(param, path, payload));
		}
	}

	private boolean proceedToValidate(final ODataEntry path, final ODataEntry payload)
	{
		return path != null && payload != null &&
				!path.getProperties().isEmpty() && !payload.getProperties().isEmpty();
	}

	private boolean keysEqual(final String name, final ODataEntry path, final ODataEntry payload)
	{
		final Object pathValue = path.getProperties().get(name);
		final Object payloadValue = payload.getProperties().get(name);

		if (payloadValue != null && pathValue instanceof ODataEntry)
		{
			final ODataEntry pathEntry = (ODataEntry) pathValue;
			final Set<String> keyPropertyNames = pathEntry.getProperties().keySet();
			return keyPropertyNames.stream()
					.allMatch(keyName -> keysEqual(keyName, pathEntry, (ODataEntry) payloadValue));
		}
		return payloadValue == null || pathValue.equals(payloadValue);
	}

	private void throwPathPayloadKeyMismatchException(final PersistenceParam param, final ODataEntry path, final ODataEntry payload)
	{
		final var pathKey = pathIntegrationKey(param, path);
		final var payloadKey = generateIntegrationKey(param, payload);
		throw new PathPayloadKeyMismatchException(pathKey, payloadKey);
	}

	private String pathIntegrationKey(final PersistenceParam param, final ODataEntry path)
	{
		final String intKey = (String) path.getProperties().get(INTEGRATION_KEY_PROPERTY_NAME);
		return intKey != null ? intKey : generateIntegrationKey(param, path);
	}

	private String generateIntegrationKey(final PersistenceParam param, final ODataEntry entry)
	{
		return getTypeDescriptor(param).map(
				descriptor -> getKeyValueGenerator().generate(descriptor, entry)).orElse("");
	}

	private Optional<TypeDescriptor> getTypeDescriptor(final PersistenceParam param)
	{
		try
		{
			if (param != null && param.getEntitySet() != null && param.getContext() != null)
			{
				final String typeCode = param.getEntitySet().getEntityType().getName();
				final String objectCode = getServiceNameExtractor().extract(param.getContext());
				return Optional.of(getItemTypeDescriptorService().getTypeDescriptor(objectCode, typeCode)
				                                                 .orElseThrow(
						                                                 () -> new IntegrationObjectItemNotFoundException(
								                                                 objectCode, typeCode)));
			}
			return Optional.empty();
		}
		catch (final EdmException e)
		{
			throw new IntegrationKeyCalculationException(e);
		}
	}

	/**
	 * @param oDataEntryIntegrationKeyValueGenerator implementation to use.
	 * @deprecated use {@link #setKeyValueGenerator(IntegrationKeyValueGenerator)} instead.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	public void setoDataEntryIntegrationKeyValueGenerator(final IntegrationKeyValueGenerator<EdmEntitySet, ODataEntry> oDataEntryIntegrationKeyValueGenerator)
	{
		/* The service is not used anymore - nothing to do here. This method is left only to keep backwards compatibility. */
	}

	protected ServiceNameExtractor getServiceNameExtractor()
	{
		return serviceNameExtractor;
	}

	@Required
	public void setServiceNameExtractor(final ServiceNameExtractor serviceNameExtractor)
	{
		this.serviceNameExtractor = serviceNameExtractor;
	}

	protected ItemTypeDescriptorService getItemTypeDescriptorService()
	{
		return itemTypeDescriptorService;
	}

	@Required
	public void setItemTypeDescriptorService(
			final ItemTypeDescriptorService itemTypeDescriptorService)
	{
		this.itemTypeDescriptorService = itemTypeDescriptorService;
	}

	protected IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> getKeyValueGenerator()
	{
		return keyValueGenerator;
	}

	@Required
	public void setKeyValueGenerator(
			final IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry> keyValueGenerator)
	{
		this.keyValueGenerator = keyValueGenerator;
	}
}
