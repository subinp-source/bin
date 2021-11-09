/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.model.ClassificationAttributeValueService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link ClassificationAttributeValueService}
 */
public class DefaultClassificationAttributeValueService implements ClassificationAttributeValueService
{
	private static final Logger LOG = Log.getLogger(DefaultClassificationAttributeValueService.class);

	private final ModelService modelService;
	private final ClassificationSystemService classificationSystemService;

	/**
	 * Instantiates the DefaultClassificationAttributeValueService object
	 *
	 * @param classificationSystemService {@link ClassificationSystemService} to be used internally
	 * @param modelService {@link ModelService} to be used internally
	 */
	public DefaultClassificationAttributeValueService(final ClassificationSystemService classificationSystemService, final ModelService modelService)
	{
		this.classificationSystemService = classificationSystemService;
		this.modelService = modelService;
	}

	@Override
	public Optional<ClassificationAttributeValueModel> find(final ClassificationSystemVersionModel systemVersion,
	                                                        final String valueCode)
	{
		if (systemVersion != null && StringUtils.isNotBlank(valueCode))
		{
			try
			{
				final var attributeValue = getClassificationSystemService().getAttributeValueForCode(systemVersion, valueCode);
				return Optional.of(attributeValue);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Failed finding the ClassificationAttributeValue with system version {} and code {}", systemVersion, valueCode);
			}
		}
		return Optional.empty();
	}

	@Override
	public ClassificationAttributeValueModel create(final ClassificationSystemVersionModel systemVersion, final String valueCode)
	{
		Preconditions.checkArgument(systemVersion != null, "SystemVersion must be provided to create ClassificationAttributeValue");
		Preconditions.checkArgument(StringUtils.isNotBlank(valueCode), "Code must be provided to create ClassificationAttributeValue");

		final ClassificationAttributeValueModel classificationAttributeValueModel = new ClassificationAttributeValueModel();
		classificationAttributeValueModel.setSystemVersion(systemVersion);
		classificationAttributeValueModel.setCode(valueCode);
		modelService.save(classificationAttributeValueModel);
		return classificationAttributeValueModel;
	}

	protected ClassificationSystemService getClassificationSystemService()
	{
		return classificationSystemService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
