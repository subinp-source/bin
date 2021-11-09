/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import static de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModelUtils.isUnique;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * @deprecated Use {@link UniquePropertyAnnotationGenerator} instead
 */
@Deprecated(since = "1905.09-CEP", forRemoval = true)
public class UniqueAttributeGenerator implements AnnotationGenerator<IntegrationObjectItemAttributeModel>
{
	static final String IS_UNIQUE = "s:IsUnique";
	
	@Override
	public boolean isApplicable(final IntegrationObjectItemAttributeModel itemAttributeModel)
	{
		return itemAttributeModel != null && isUnique(itemAttributeModel);
	}

	@Override
	public AnnotationAttribute generate(final IntegrationObjectItemAttributeModel itemAttributeModel)
	{
		return new AnnotationAttribute()
				.setName(IS_UNIQUE)
				.setText("true");
	}
}
