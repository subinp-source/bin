/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.schema.attribute;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

/**
 * @deprecated please use {@link PropertyAnnotationGenerator} instead
 * @param <T> : IntegrationObjectItemAttributeModel or IntegrationObjectItemModel
 */
@Deprecated(since = "1905.09-CEP", forRemoval = true)
public interface AnnotationGenerator<T extends ItemModel> extends SchemaElementGenerator<AnnotationAttribute, T>
{
	/**
	 * Determines if this annotation generator is applicable for the given attribute or attributes
	 * @param model the IntegrationObjectItemAttributeModel OR IntegrationObjectItemModel we are verifying on
	 * @return true if applicable, otherwise false
	 */
	boolean isApplicable(final T model);
}
