/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.searchservices.admin.SnIndexTypeNotFoundException;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.core.service.SnQualifierType;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link SnContextFactory}.
 */
public class DefaultSnContextFactory implements SnContextFactory
{
	private SnIndexConfigurationService snIndexConfigurationService;
	private SnIndexTypeService snIndexTypeService;
	private SnQualifierTypeFactory snQualifierTypeFactory;

	@Override
	public SnContext createContext(final String indexTypeId)
	{
		Objects.requireNonNull(indexTypeId, "typeId must not be null");

		final DefaultSnContext context = new DefaultSnContext();
		populateContext(context, indexTypeId);

		return context;
	}

	protected void populateContext(final DefaultSnContext context, final String indexTypeId)
	{
		final Optional<SnIndexType> indexTypeOptional = snIndexTypeService.getIndexTypeForId(indexTypeId);
		if (!indexTypeOptional.isPresent())
		{
			throw new SnIndexTypeNotFoundException("Index type not found: indexTypeId = " + indexTypeId);
		}

		final SnIndexType indexType = indexTypeOptional.get();
		context.setIndexType(indexType);

		if (StringUtils.isNotBlank(indexType.getIndexConfigurationId()))
		{
			final Optional<SnIndexConfiguration> indexConfigurationOptional = snIndexConfigurationService
					.getIndexConfigurationForId(indexType.getIndexConfigurationId());
			if (indexConfigurationOptional.isPresent())
			{
				context.setIndexConfiguration(indexConfigurationOptional.get());
			}
		}

		context.setQualifiers(collectQualifiers(context));
	}

	protected Map<String, List<SnQualifier>> collectQualifiers(final DefaultSnContext context)
	{
		final Map<String, List<SnQualifier>> qualifiers = new HashMap<>();
		final List<SnQualifierType> qualifierTypes = snQualifierTypeFactory.getAllQualifierTypes();

		if (CollectionUtils.isNotEmpty(qualifierTypes))
		{
			for (final SnQualifierType qualifierType : qualifierTypes)
			{
				qualifiers.put(qualifierType.getId(), qualifierType.getQualifierProvider().getCurrentQualifiers(context));
			}
		}

		return qualifiers;
	}

	public SnIndexConfigurationService getSnIndexConfigurationService()
	{
		return snIndexConfigurationService;
	}

	@Required
	public void setSnIndexConfigurationService(final SnIndexConfigurationService snIndexConfigurationService)
	{
		this.snIndexConfigurationService = snIndexConfigurationService;
	}

	public SnIndexTypeService getSnIndexTypeService()
	{
		return snIndexTypeService;
	}

	@Required
	public void setSnIndexTypeService(final SnIndexTypeService snIndexTypeService)
	{
		this.snIndexTypeService = snIndexTypeService;
	}

	public SnQualifierTypeFactory getSnQualifierTypeFactory()
	{
		return snQualifierTypeFactory;
	}

	@Required
	public void setSnQualifierTypeFactory(final SnQualifierTypeFactory snQualifierTypeFactory)
	{
		this.snQualifierTypeFactory = snQualifierTypeFactory;
	}
}
