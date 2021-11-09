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
package de.hybris.platform.odata2services.odata.processor.reader;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;

import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException;
import de.hybris.platform.integrationservices.model.impl.DelegatingAttributeValueAccessor;
import de.hybris.platform.integrationservices.model.impl.NullAttributeValueGetter;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.security.TypePermissionService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.PersistenceService;
import de.hybris.platform.odata2services.odata.processor.writer.ResponseWriter;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.uri.NavigationSegment;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractEntityReader implements EntityReader
{
	private static final Logger LOG = Log.getLogger(AbstractEntityReader.class);

	private PersistenceService persistenceService;
	private ResponseWriter responseWriter;
	private TypePermissionService typePermissionService;

	protected boolean handleAssociationMultiplicityRetrievalError(final EdmException e)
	{
		LOG.warn("An exception occurred while getting the multiplicity of an association", e);
		return false;
	}

	protected void validateReadPermission(final ItemLookupRequest itemLookupRequest)
	{
		if (typePermissionService != null)
		{
			TypeDescriptor currentTypeDescriptor = itemLookupRequest.getTypeDescriptor();
			for(final NavigationSegment navSeg : itemLookupRequest.getNavigationSegments())
			{
				final String navSegmentName = getNavigationPropertyName(navSeg);
				if (!LOCALIZED_ATTRIBUTE_NAME.equals(navSegmentName))
				{
					final TypeDescriptor typeDescriptor = getTypeDescriptorForNavSeg(currentTypeDescriptor, navSegmentName);
					typePermissionService.checkReadPermission(typeDescriptor);
					currentTypeDescriptor = typeDescriptor;
				}
			}
		}
	}

	private TypeDescriptor getTypeDescriptorForNavSeg(final TypeDescriptor currentTypeDescriptor, final String navSegName)
	{
		return currentTypeDescriptor
				.getAttribute(navSegName)
				.map(TypeAttributeDescriptor::getAttributeType)
				.orElseThrow(() -> new TypeAttributeDescriptorNotFoundException(currentTypeDescriptor, navSegName));
	}

	private String getNavigationPropertyName(final NavigationSegment navSegment)
	{
		try
		{
			return navSegment.getNavigationProperty().getName();
		}
		catch (final EdmException e)
		{
			throw new InternalProcessingException(e);
		}
	}

	protected PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	@Required
	public void setPersistenceService(final PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	protected ResponseWriter getResponseWriter()
	{
		return responseWriter;
	}

	@Required
	public void setResponseWriter(final ResponseWriter responseWriter)
	{
		this.responseWriter = responseWriter;
	}

	public void setTypePermissionService(final TypePermissionService typePermissionService)
	{
		this.typePermissionService = typePermissionService;
	}
}
