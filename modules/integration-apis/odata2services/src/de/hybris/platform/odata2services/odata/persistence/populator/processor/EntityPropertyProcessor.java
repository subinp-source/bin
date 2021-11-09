/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isAutoCreate;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isPartOf;
import static de.hybris.platform.odata2services.odata.persistence.StorageRequest.storageRequestBuilder;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.ModelEntityService;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.creation.CreateItemStrategy;
import de.hybris.platform.odata2services.odata.persistence.creation.NeverCreateItemStrategy;

import org.apache.olingo.odata2.api.edm.EdmAnnotatable;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

public class EntityPropertyProcessor extends AbstractPropertyProcessor
{
	private ModelEntityService modelEntityService;
	private CreateItemStrategy createItemStrategy;

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute)
	{
		return !attribute.isCollection() && !attribute.isPrimitive() && !attribute.isMap();
	}

	@Override
	protected void processItemInternal(final ItemModel item, final String entryPropertyName, final Object value,
			final StorageRequest request) throws EdmException
	{
		final ODataEntry oDataEntry = (ODataEntry) value;
		final EdmEntitySet relatedEntitySet = request.getEntitySetReferencedByProperty(entryPropertyName);

		final IntegrationItem nestedItem = request.getIntegrationItem().getReferencedItem(entryPropertyName);
		final StorageRequest innerStorageRequest = storageRequestBuilder().from(request)
				.withEntitySet(relatedEntitySet)
				.withODataEntry(oDataEntry)
				.withIntegrationItem(nestedItem)
				.build();
		final ItemModel relatedItem = getModelEntityService()
				.createOrUpdateItem(innerStorageRequest, determineCreateStrategy(request.getEntityType(), entryPropertyName));

		final EdmAnnotatable property = (EdmAnnotatable) request.getEntityType().getProperty(entryPropertyName);

		if (isPartOf(property) && getModelService().isNew(relatedItem))
		{
			relatedItem.setOwner(item);
		}

		final String integrationObjectItemCode = request.getEntityType().getName();
		final String itemPropertyName = getIntegrationObjectService()
				.findItemAttributeName(request.getIntegrationObjectCode(), integrationObjectItemCode, entryPropertyName);

		if (relatedItem instanceof EnumerationValueModel)
		{
			final Object relatedItemValue = getModelService().get(relatedItem.getPk());
			getModelService().setAttributeValue(item, itemPropertyName, relatedItemValue);
		}
		else
		{
			if (isItemPropertySettable(item, entryPropertyName, request))
			{
				getModelService().setAttributeValue(item, itemPropertyName, relatedItem);
			}
		}
	}

	@Override
	protected void processEntityInternal(final ODataEntry oDataEntry, final String propertyName, final Object value,
			final ItemConversionRequest conversionRequest) throws EdmException
	{
		if (value != null)
		{
			final ItemConversionRequest subRequest = conversionRequest.propertyConversionRequest(propertyName, value);
			final ODataEntry entry = getModelEntityService().getODataEntry(subRequest);
			oDataEntry.getProperties().putIfAbsent(propertyName, entry);
		}
	}

	protected CreateItemStrategy determineCreateStrategy(final EdmEntityType entityType, final String propertyName)
			throws EdmException
	{
		final EdmAnnotatable property = (EdmAnnotatable) entityType.getProperty(propertyName);
		return isAutoCreate(property) || isPartOf(property) ? getCreateItemStrategy() : new NeverCreateItemStrategy();
	}

	protected ModelEntityService getModelEntityService()
	{
		return modelEntityService;
	}

	@Required
	public void setModelEntityService(final ModelEntityService modelEntityService)
	{
		this.modelEntityService = modelEntityService;
	}

	/**
	 * @deprecated this method is not going to be used after {@link #processItem(ItemModel, StorageRequest)} is removed.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	protected CreateItemStrategy getCreateItemStrategy()
	{
		return createItemStrategy;
	}

	/**
	 * @deprecated this method is not going to be used after {@link #processItem(ItemModel, StorageRequest)} is removed.
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	public void setCreateItemStrategy(final CreateItemStrategy createItemStrategy)
	{
		this.createItemStrategy = createItemStrategy;
	}
}
