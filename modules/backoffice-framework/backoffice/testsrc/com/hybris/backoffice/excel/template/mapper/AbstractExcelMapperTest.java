/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.mapper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;


class AbstractExcelMapperTest
{

	AttributeDescriptorModel mockAttributeDescriptorUnique(final boolean unique)
	{
		final AttributeDescriptorModel uniqueAttributeDescriptor = mock(AttributeDescriptorModel.class);
		given(uniqueAttributeDescriptor.getUnique()).willReturn(unique);
		return uniqueAttributeDescriptor;
	}

	AttributeDescriptorModel mockAttributeDescriptorLocalized(final boolean localized)
	{
		final AttributeDescriptorModel uniqueAttributeDescriptor = mock(AttributeDescriptorModel.class);
		given(uniqueAttributeDescriptor.getLocalized()).willReturn(localized);
		return uniqueAttributeDescriptor;
	}

	ExcelFilter<AttributeDescriptorModel> getUniqueFilter()
	{
		return AttributeDescriptorModel::getUnique;
	}

}
