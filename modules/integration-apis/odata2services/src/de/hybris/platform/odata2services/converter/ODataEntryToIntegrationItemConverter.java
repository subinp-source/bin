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

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * Converts an {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry} to an instance of
 * {@link de.hybris.platform.integrationservices.item.IntegrationItem}
 */
public interface ODataEntryToIntegrationItemConverter
{
	/**
	 * Performs the conversion when there is no {@link TypeDescriptor} available. In this case the type descriptor will be derived
	 * from the other parameters.
	 *
	 * @param context   context of the request containing the entry
	 * @param entitySet metadata about the entry received
	 * @param entry     the entry received to be converted to the {@code IntegrationItem}
	 * @return an integration item converted from the entry
	 * @throws EdmException if there is a problem with the metadata provided by the {@code entitySet}
	 */
	IntegrationItem convert(ODataContext context, EdmEntitySet entitySet, final ODataEntry entry) throws EdmException;

	/**
	 * Performs the conversion when {@link TypeDescriptor} for the integration item to create is available.
	 *
	 * @param context   context of the request containing the entry
	 * @param typeDesc  type descriptor for the integration item to create
	 * @param entitySet metadata about the entry received
	 * @param entry     the entry received to be converted to the {@code IntegrationItem}
	 * @return an integration item converted from the entry
	 * @throws EdmException if there is a problem with the metadata provided by the {@code entitySet}
	 * @deprecated use {@link #convert(ODataContext, TypeDescriptor, ODataEntry)}
	 */
	@Deprecated(since = "19.05.2004-CEP", forRemoval = true)
	IntegrationItem convert(ODataContext context, TypeDescriptor typeDesc,
	                        EdmEntitySet entitySet, ODataEntry entry) throws EdmException;

	/**
	 * Performs the conversion when {@link TypeDescriptor} for the integration item to create is available.
	 *
	 * @param context   context of the request containing the entry
	 * @param typeDesc  type descriptor for the integration item to create
	 * @param entry     the entry received to be converted to the {@code IntegrationItem}
	 * @return an integration item converted from the entry
	 */
	IntegrationItem convert(ODataContext context, TypeDescriptor typeDesc, ODataEntry entry);

	/**
	 * Performs conversion of an OData entry nested in the payload of an outer entry.
	 *
	 * @param context    context of the request containing the entry
	 * @param typeDesc   type descriptor for the integration item to create, that matches the entry
	 *                   being converted
	 * @param entry      the entry received to be converted to the {@code IntegrationItem}
	 * @param parentItem integration item that corresponds to the OData entry containing the entry
	 *                   being converted.
	 * @return an integration item converted from the entry
	 */
	IntegrationItem convert(ODataContext context,
	                        TypeDescriptor typeDesc,
	                        ODataEntry entry,
	                        IntegrationItem parentItem);
}
