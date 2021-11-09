/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.util.impl;

import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticValueWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticWsDTO;
import de.hybris.platform.sap.productconfig.occ.GroupWsDTO;
import de.hybris.platform.sap.productconfig.occ.util.ImageHandler;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;


public class ImageHandlerImpl implements ImageHandler
{

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Override
	public void convertImages(final ConfigurationData configData, final ConfigurationWsDTO configurationWs)
	{
		configData.getGroups().forEach(group -> convertImagesInGroup(group, configurationWs.getGroups()));
	}

	protected void convertImagesInGroup(final UiGroupData group, final List<GroupWsDTO> groups)
	{
		final Optional<GroupWsDTO> groupWsOptional = groups.stream()
				.filter(currentWsGroup -> group.getId().equals(currentWsGroup.getId())).findFirst();
		if (groupWsOptional.isEmpty())
		{
			throw new IllegalStateException("We expect that WS data contains a matching group for " + group.getId());
		}
		final GroupWsDTO groupWs = groupWsOptional.get();

		convertImagesInGroup(group, groupWs);
	}

	protected void convertImagesInGroup(final UiGroupData group, final GroupWsDTO groupWs)
	{
		final List<CsticData> cstics = group.getCstics();
		if (cstics != null)
		{
			cstics.forEach(cstic -> convertImagesInCstic(cstic, groupWs.getAttributes()));
		}
		final List<UiGroupData> subGroups = group.getSubGroups();
		if (subGroups != null)
		{
			subGroups.forEach(subGroup -> convertImagesInGroup(subGroup, groupWs.getSubGroups()));
		}
	}

	protected void convertImagesInCstic(final CsticData cstic, final List<CsticWsDTO> cstics)
	{
		final Optional<CsticWsDTO> csticWsOptional = cstics.stream()
				.filter(currentCsticWs -> cstic.getName().equals(currentCsticWs.getName())).findFirst();
		if (csticWsOptional.isEmpty())
		{
			throw new IllegalStateException("We expect that WS data contains a matching characteristic for " + cstic.getName());
		}
		final CsticWsDTO csticWs = csticWsOptional.get();
		convertImagesInCstic(cstic, csticWs);
	}

	protected void convertImagesInCstic(final CsticData cstic, final CsticWsDTO csticWs)
	{
		csticWs.setImages(getDataMapper().mapAsList(cstic.getMedia(), ImageWsDTO.class, null));
		final List<CsticValueData> values = cstic.getDomainvalues();
		if (values != null)
		{
			values.forEach(currentvalue -> convertImagesInValue(currentvalue, csticWs.getDomainValues()));
		}
	}

	protected void convertImagesInValue(final CsticValueData csticValue, final CsticValueWsDTO csticValueWs)
	{
		csticValueWs.setImages(getDataMapper().mapAsList(csticValue.getMedia(), ImageWsDTO.class, null));
	}

	protected void convertImagesInValue(final CsticValueData csticValue, final List<CsticValueWsDTO> csticValueListWs)
	{
		final Optional<CsticValueWsDTO> csticValueWsOptional = csticValueListWs.stream()
				.filter(currentCsticValueWs -> csticValue.getKey().equals(currentCsticValueWs.getKey())).findFirst();
		if (csticValueWsOptional.isEmpty())
		{
			throw new IllegalStateException("We expect that WS data contains a matching value for " + csticValue.getKey());
		}
		final CsticValueWsDTO csticValueWs = csticValueWsOptional.get();
		convertImagesInValue(csticValue, csticValueWs);

	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

}
