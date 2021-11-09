/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicewebservices.helper;

import de.hybris.platform.assistedservicefacades.user.data.AutoSuggestionCustomerData;
import de.hybris.platform.assistedservicewebservices.dto.CustomerSearchPageWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.CustomerSuggestionWsDto;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;

import javax.annotation.Resource;

import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class CustomerHelper
{

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	public CustomerSearchPageWsDTO getCustomerSearchPageDto(final SearchPageData<CustomerData> customers)
	{
		return dataMapper.map(customers, CustomerSearchPageWsDTO.class);
	}

	public List<CustomerSuggestionWsDto> getCustomerSuggestions(final List<AutoSuggestionCustomerData> customerData)
	{
		return dataMapper.mapAsList(customerData, CustomerSuggestionWsDto.class, FieldSetLevelHelper.DEFAULT_LEVEL);
	}
}
