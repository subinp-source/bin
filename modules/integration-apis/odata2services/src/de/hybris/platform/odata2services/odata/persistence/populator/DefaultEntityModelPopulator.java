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
package de.hybris.platform.odata2services.odata.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;
import de.hybris.platform.odata2services.odata.persistence.populator.processor.PropertyProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

/**
 * Default implementation for {@link EntityModelPopulator}
 */
public class DefaultEntityModelPopulator implements EntityModelPopulator
{
	private List<PropertyProcessor> propertyProcessors;
	private Set<Class<? extends PropertyProcessor>> excludedItemPropertyProcessors = new HashSet<>();

	@Override
	public void populateItem(final ItemModel item, final StorageRequest storageRequest) throws EdmException
	{
		Preconditions.checkArgument(item != null, "ItemModel cannot be null");
		Preconditions.checkArgument(storageRequest != null, "StorageRequest cannot be null");

		for (final PropertyProcessor propertyProcessor : getPropertyProcessors())
		{
			if (! excludedItemPropertyProcessors.contains(propertyProcessor.getClass()))
			{
				propertyProcessor.processItem(item, storageRequest);
			}
		}
	}

	@Override
	public void populateEntity(final ODataEntry oDataEntry, final ItemConversionRequest conversionRequest) throws EdmException
	{
		Preconditions.checkArgument(oDataEntry != null, "ItemModel cannot be null");
		Preconditions.checkArgument(conversionRequest != null, "ItemConversionRequest cannot be null");

		for (final PropertyProcessor propertyProcessor : propertyProcessors)
		{
			propertyProcessor.processEntity(oDataEntry, conversionRequest);
		}
	}

	protected List<PropertyProcessor> getPropertyProcessors()
	{
		return propertyProcessors;
	}

	@Required
	public void setPropertyProcessors(final List<PropertyProcessor> propertyProcessors)
	{
		this.propertyProcessors = propertyProcessors;
	}

	/**
	 * This method allows custom {@code PropertyProcessor}s be gradually reworked during the deprecation period into the new
	 * interface implementation. Any custom property processor that has been changed, should be registered as excluded in the
	 * configuration for {@code oDataEntityModelPopulator} spring bean.
	 * @param processors classes of processors to be excluded from processing after they've been refactored.
	 *
	 * @deprecated This is a temporary method used while deprecated {@link PropertyProcessor}s are being migrated to
	 * {@link de.hybris.platform.inboundservices.persistence.AttributePopulator}s. After the migration is complete, the method will be removed.
	 * There is no alternative available.
	 */
	@Deprecated(since = "1905.07-CEP", forRemoval = true)
	public void setExcludedItemPropertyProcessors(final Set<Class<? extends PropertyProcessor>> processors)
	{
		excludedItemPropertyProcessors = processors;
	}
}
