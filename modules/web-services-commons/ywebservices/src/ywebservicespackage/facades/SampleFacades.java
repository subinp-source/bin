/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ywebservicespackage.facades;

import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

import ywebservicespackage.data.UserData;
import ywebservicespackage.dto.SampleWsDTO;
import ywebservicespackage.dto.TestMapWsDTO;


public interface SampleFacades
{
	SampleWsDTO getSampleWsDTO(final String value);

	UserData getUser(String id);

	List<UserData> getUsers();

	SearchPageData<UserData> getUsers(SearchPageData<?> params);

	TestMapWsDTO getMap();
}
