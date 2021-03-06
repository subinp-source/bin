/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureUnitData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.converters.Populator;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;


public class FeaturePopulator implements Populator<Feature, FeatureData>
{
	@Override
	public void populate(final Feature source, final FeatureData target)
	{
		final ClassAttributeAssignmentModel classAttributeAssignment = source.getClassAttributeAssignment();

		// Create the feature
		target.setCode(source.getCode());
		target.setComparable(Boolean.TRUE.equals(classAttributeAssignment.getComparable()));
		target.setDescription(classAttributeAssignment.getDescription());
		target.setName(source.getName());
		target.setRange(Boolean.TRUE.equals(classAttributeAssignment.getRange()));

		final ClassificationAttributeUnitModel unit = classAttributeAssignment.getUnit();
		if (unit != null)
		{
			final FeatureUnitData featureUnitData = new FeatureUnitData();
			featureUnitData.setName(unit.getName());
			featureUnitData.setSymbol(unit.getSymbol());
			featureUnitData.setUnitType(unit.getUnitType());
			target.setFeatureUnit(featureUnitData);
		}

		// Create the feature data items
		final List<FeatureValueData> featureValueDataList = new ArrayList<FeatureValueData>();
		for (final FeatureValue featureValue : source.getValues())
		{
			final FeatureValueData featureValueData = new FeatureValueData();
			final Object value = featureValue.getValue();
			if (value instanceof ClassificationAttributeValueModel)
			{
				featureValueData.setValue(((ClassificationAttributeValueModel) value).getName());
			}
			else if (NumberUtils.isNumber(String.valueOf(value)))
			{
				featureValueData.setValue(String.valueOf(value).replaceAll("\\.0*$", ""));
			}
			else
			{
				featureValueData.setValue(String.valueOf(value));
			}

			featureValueDataList.add(featureValueData);
		}
		target.setFeatureValues(featureValueDataList);
	}
}
