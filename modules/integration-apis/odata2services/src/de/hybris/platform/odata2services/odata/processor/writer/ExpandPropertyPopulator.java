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
package de.hybris.platform.odata2services.odata.processor.writer;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;

import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.security.TypePermissionService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.apache.olingo.odata2.api.uri.NavigationPropertySegment;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.slf4j.Logger;

/**
 * Populates the $expand system property
 */
public class ExpandPropertyPopulator implements ResponseWriterPropertyPopulator
{
	private static final Logger LOG = Log.getLogger(ExpandPropertyPopulator.class);
	private TypePermissionService typePermissionService;

	@Override
	public boolean isApplicable(final ItemLookupRequest itemLookupRequest)
	{
		return itemLookupRequest.getExpand() != null && !itemLookupRequest.getExpand().isEmpty();
	}

	@Override
	public EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder populate(
			final EntityProviderWriteProperties properties, final ItemLookupRequest itemLookupRequest,
			final ItemLookupResult result)
	{
		final EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder builder = EntityProviderWriteProperties.fromProperties(
				properties);
		try
		{
			// We do not support the $select system property which is why null is passed as the argument
			final ExpandSelectTreeNode expandSelectTree = UriParser.createExpandSelectTree(null, itemLookupRequest.getExpand());
			final Map<String, ODataCallback> callbacks = populateCallbacks(expandSelectTree, itemLookupRequest);
			builder.expandSelectTree(expandSelectTree).callbacks(callbacks);
		}
		catch (final EdmException e)
		{
			LOG.error("Cannot set $expand properties due to exception.", e);
			throw new InternalProcessingException("Problem while trying to set $expand system property.");
		}
		return builder;
	}

	private Map<String, ODataCallback> populateCallbacks(final ExpandSelectTreeNode expandSelectTree,
	                                                     final ItemLookupRequest itemLookupRequest)
	{
		validateExpand(itemLookupRequest);
		final Map<String, ODataCallback> callbacks = new HashMap<>();
		expandSelectTree.getLinks().forEach((propName, expandTreeNode) -> callbacks.put(propName, new CallbackWriter()));
		return callbacks;
	}

	private void validateExpand(final ItemLookupRequest itemLookupRequest)
	{
		if(typePermissionService != null) {
			itemLookupRequest.getExpand().forEach(
					navPropertyPath -> validateReadExpandPath(navPropertyPath, itemLookupRequest.getTypeDescriptor()));
		}
	}

	private void validateReadExpandPath(final List<NavigationPropertySegment> navPropertyPath,
	                                    TypeDescriptor currentItemType)
	{
		for (final NavigationPropertySegment navPropertySegment : navPropertyPath)
		{
			final String attrName = getAttributeName(navPropertySegment);
			if(!attrName.equals(LOCALIZED_ATTRIBUTE_NAME))
			{
				final TypeDescriptor finalCurrentItemType = currentItemType;
				final TypeAttributeDescriptor attribute = currentItemType.getAttribute(attrName)
				                                                         .orElseThrow(
						                                                         () -> new TypeAttributeDescriptorNotFoundException(finalCurrentItemType, attrName));
				currentItemType = attribute.getAttributeType();
				typePermissionService.checkReadPermission(currentItemType);
			}
		}
	}

	private String getAttributeName(final NavigationPropertySegment navigationPropertySegment)
	{
		try
		{
			return navigationPropertySegment.getNavigationProperty().getName();
		}
		catch (final EdmException e)
		{
			LOG.error("Cannot set $expand properties due to exception.", e);
			throw new InternalProcessingException("Problem while trying to set $expand system property.");
		}
	}

	public void setTypePermissionService(final TypePermissionService typePermissionService)
	{
		this.typePermissionService = typePermissionService;
	}
}
