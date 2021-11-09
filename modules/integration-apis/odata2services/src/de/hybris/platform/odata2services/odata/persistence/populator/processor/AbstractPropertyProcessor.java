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
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor;


import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.inboundservices.persistence.AttributePopulator;
import de.hybris.platform.inboundservices.persistence.populator.AbstractAttributePopulator;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.service.ItemTypeDescriptorService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.AbstractRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Map;
import java.util.Optional;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPropertyProcessor implements PropertyProcessor
{
	private ModelService modelService;
	private IntegrationObjectService integrationObjectService;
	private TypeService typeService;
	private DescriptorFactory descriptorFactory;
	private ItemTypeDescriptorService itemTypeDescriptorService;
	private static final Log LOG = Log.getLogger(AbstractPropertyProcessor.class);

	@Override
	public void processItem(final ItemModel item, final StorageRequest request) throws EdmException
	{
		final Map<String, Object> properties = request.getODataEntry().getProperties();

		for (final Map.Entry<String, Object> entry : properties.entrySet())
		{
			final String propertyName = entry.getKey();
			final Object propertyValue = entry.getValue();

			if (isItemPropertySettable(item, propertyName, request))
			{
				final TypeAttributeDescriptor attributeDescriptor = getTypeAttributeDescriptor(item, request, propertyName);
				final PropertyMetadata propertyMetadata = PropertyMetadata.create(attributeDescriptor, propertyName);
				if (isPropertySupported(propertyMetadata))
				{
					processItemInternal(item, propertyName, propertyValue, request);
				}
			}
		}
	}

	private Optional<TypeAttributeDescriptor> getTypeAttributeDescriptor(final String integrationObjectCode,
	                                                                     final String integrationItemCode,
	                                                                     final String propertyName)
	{
		final Optional<TypeDescriptor> itemTypeDescriptor = getItemTypeDescriptorService().getTypeDescriptorByTypeCode(
				integrationObjectCode, integrationItemCode);
		return itemTypeDescriptor.flatMap(descriptor -> descriptor.getAttribute(propertyName));
	}

	private TypeAttributeDescriptor getTypeAttributeDescriptor(final Object value, final AbstractRequest request,
	                                                           final String propertyName)
	{
		return getTypeAttributeDescriptor(request.getIntegrationObjectCode(), getItemTypeCode(value), propertyName)
				.orElseGet(() -> getTypeAttributeDescriptorForEntityType(request, propertyName));
	}

	private TypeAttributeDescriptor getTypeAttributeDescriptorForEntityType(final AbstractRequest request,
	                                                                        final String propertyName)
	{
		try
		{
			final String entityName = request.getEntityType().getName();
			final String integrationObjectCode = request.getIntegrationObjectCode();
			return getTypeAttributeDescriptor(integrationObjectCode, entityName, propertyName).orElse(null);
		}
		catch (final EdmException ex)
		{
			LOG.error("Cannot get the name of the entity type.", ex);
			return null;
		}
	}

	/**
	 * @deprecated use {@link ItemTypeDescriptorService#getTypeDescriptorByTypeCode(String, String)}
	 * and then retrieve the TypeAttributeDescriptor from the TypeDescriptor attributes.
	 * This method is no longer used.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	protected Optional<TypeAttributeDescriptor> findTypeDescriptorAttributeForItem(final IntegrationObjectItemModel integrationObjectItem, final String integrationItemAttributeName)
	{
		final TypeDescriptor itemTypeDescriptor = descriptorFactory.createItemTypeDescriptor(integrationObjectItem);
		return itemTypeDescriptor.getAttribute(integrationItemAttributeName);
	}

	@Override
	public void processEntity(final ODataEntry oDataEntry, final ItemConversionRequest conversionRequest) throws EdmException
	{
		for (final String propertyName : conversionRequest.getAllPropertyNames())
		{
			if (isPropertySupported(propertyName))
			{
				final TypeAttributeDescriptor attributeDescriptor = getTypeAttributeDescriptor(conversionRequest.getValue(), conversionRequest, propertyName);
				final PropertyMetadata propertyMetadata = PropertyMetadata.create(attributeDescriptor, propertyName);
				if (isPropertySupported(propertyMetadata) && shouldPropertyBeConverted(conversionRequest, propertyName) && attributeDescriptor.isReadable())
				{
					final Object propertyValue = readPropertyValue(attributeDescriptor, conversionRequest);
					processEntityInternal(oDataEntry, propertyName, propertyValue, conversionRequest);
				}
			}
		}
	}

	protected Object readPropertyValue(final TypeAttributeDescriptor descriptor, final ItemConversionRequest conversionRequest)
	{
		return descriptor.isLocalized() ?
				descriptor.accessor().getValue(conversionRequest.getValue(), conversionRequest.getAcceptLocale()) :
				descriptor.accessor().getValue(conversionRequest.getValue());
	}

	/**
	 * @deprecated use {@link #readPropertyValue(TypeAttributeDescriptor, ItemConversionRequest)} instead
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected Object readPropertyValue(final ItemConversionRequest request, final String propertyName) throws EdmException
	{
		final String integrationObjectItemCode = request.getEntityType().getName();
		final String itemPropertyName = getIntegrationObjectService()
				.findItemAttributeName(request.getIntegrationObjectCode(), integrationObjectItemCode, propertyName);

		final Object requestValue = request.getValue();
		final AttributeDescriptorModel attributeDescriptor = getAttributeDescriptor(requestValue, itemPropertyName);

		return attributeDescriptor.getLocalized()
				? getModelService().getAttributeValue(requestValue, attributeDescriptor.getQualifier(), request.getAcceptLocale())
				: getModelService().getAttributeValue(requestValue, attributeDescriptor.getQualifier());
	}

	protected boolean shouldPropertyBeConverted(final ItemConversionRequest request, final String propertyName)
	{
		return request.isPropertyValueShouldBeConverted(propertyName);
	}

	/**
	 * @deprecated switch to extend {@link AbstractAttributePopulator} and
	 * use its {@code isSettable(TypeAttributeDescriptor, ItemModel)} method
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected boolean isItemPropertySettable(final ItemModel item, final String propertyName, final StorageRequest request) throws EdmException
	{
		if (isPropertySupported(propertyName))
		{
			final AttributeDescriptorModel attributeDescriptor = getAttributeDescriptor(item, propertyName, request);
			return getModelService().isNew(item) || attributeDescriptor.getWritable();
		}
		return false;
	}

	/**
	 * @deprecated switch to implement {@link AttributePopulator} and use
	 * {@link TypeAttributeDescriptor}, which eliminates the need to access {@code AttributeDescriptorModel}
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected AttributeDescriptorModel getAttributeDescriptor(final ItemModel item, final String propertyName, final StorageRequest request) throws EdmException
	{
		final String integrationObjectItemCode = request.getEntityType().getName();
		final String itemPropertyName = getIntegrationObjectService()
				.findItemAttributeName(request.getIntegrationObjectCode(), integrationObjectItemCode, propertyName);
		return getAttributeDescriptor(item, itemPropertyName);
	}

	private AttributeDescriptorModel getAttributeDescriptor(final Object value, final String propertyName)
	{
		return getTypeService().getAttributeDescriptor(getItemTypeCode(value), propertyName);
	}

	private boolean isPropertySupported(final String propertyName)
	{
		return !INTEGRATION_KEY_PROPERTY_NAME.equals(propertyName) && !LOCALIZED_ATTRIBUTE_NAME.equals(propertyName);
	}

	private String getItemTypeCode(final Object value)
	{
		return value instanceof HybrisEnumValue ?
				((HybrisEnumValue) value).getType() :
				((ItemModel) value).getItemtype();
	}


	/**
	 * @deprecated use {@link #isPropertySupported(TypeAttributeDescriptor)} instead
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected boolean isPropertySupported(final PropertyMetadata propertyMetadata)
	{
		return isPropertySupported(propertyMetadata.getAttributeDescriptor());
	}

	protected boolean isPropertySupported(final TypeAttributeDescriptor descriptor)
	{
		return descriptor != null && isApplicable(descriptor);
	}

	protected abstract boolean isApplicable(final TypeAttributeDescriptor typeAttributeDescriptor);

	protected ItemTypeDescriptorService getItemTypeDescriptorService()
	{
		return itemTypeDescriptorService;
	}

	@Required
	public void setItemTypeDescriptorService(final ItemTypeDescriptorService itemTypeDescriptorService)
	{
		this.itemTypeDescriptorService = itemTypeDescriptorService;
	}

	/**
	 * @deprecated classes using/overriding this method should move to implement {@link AttributePopulator} instead.
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	protected abstract void processItemInternal(final ItemModel item, final String entryPropertyName, final Object value,
	                                            final StorageRequest request) throws EdmException;

	protected abstract void processEntityInternal(final ODataEntry oDataEntry, final String propertyName, final Object value,
	                                              final ItemConversionRequest request) throws EdmException;

	/**
	 * @deprecated not used anymore. For the purpose of reading item attribute values use {@link TypeAttributeDescriptor#accessor()}
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @deprecated not going to be used once other deprecated methods are removed.
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @deprecated not going to be used once other deprecated methods are removed.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	/**
	 * @deprecated not going to be used once other deprecated methods are removed.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @deprecated not used anymore
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @deprecated temporary used by {@link #findTypeDescriptorAttributeForItem(IntegrationObjectItemModel, String)}.
	 * Once that method is removed, there will be no need for this factory.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	public void setDescriptorFactory(final DescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}

	/**
	 * @deprecated {@link TypeAttributeDescriptor} is going to be used instead of this class everywhere in the code
	 */
	@Deprecated(since = "1905.09-CEP", forRemoval = true)
	public static class PropertyMetadata
	{
		private TypeAttributeDescriptor attributeDescriptor;
		private String propertyName;

		private PropertyMetadata()
		{
			// Can't instantiate using constructor
		}

		static PropertyMetadata create(final TypeAttributeDescriptor attributeDescriptor, final String propertyName)
		{
			return new PropertyMetadata()
					.setAttributeDescriptor(attributeDescriptor)
					.setPropertyName(propertyName);
		}

		TypeAttributeDescriptor getAttributeDescriptor()
		{
			return attributeDescriptor;
		}

		private PropertyMetadata setAttributeDescriptor(final TypeAttributeDescriptor attributeDescriptor)
		{
			this.attributeDescriptor = attributeDescriptor;
			return this;
		}

		String getPropertyName()
		{
			return propertyName;
		}

		private PropertyMetadata setPropertyName(final String propertyName)
		{
			this.propertyName = propertyName;
			return this;
		}
	}
}