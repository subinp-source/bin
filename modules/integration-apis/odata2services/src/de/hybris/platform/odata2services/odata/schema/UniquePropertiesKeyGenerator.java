/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.odata.schema;

import java.util.List;

import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;

/**
 * A key generator that uses any simple entity property having "IsUnique" annotation for the entity key.
 */
public class UniquePropertiesKeyGenerator extends KeyGenerator
{
	private static final String UNIQUE_ANNOTATION_NAME = "s:IsUnique";
	private static final String TRUE_VALUE = "true";

	@Override
	protected boolean isKey(final SimpleProperty property)
	{
		final List<AnnotationAttribute> annotations = property.getAnnotationAttributes();
		return annotations != null && annotations.stream().anyMatch(this::isUnique);
	}

	private boolean isUnique(final AnnotationAttribute attr)
	{
		return UNIQUE_ANNOTATION_NAME.equals(attr.getName()) && TRUE_VALUE.equals(attr.getText());
	}
}
