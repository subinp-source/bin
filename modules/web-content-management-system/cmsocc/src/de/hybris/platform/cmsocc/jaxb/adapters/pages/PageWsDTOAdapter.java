/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.pages;

import static de.hybris.platform.cmsocc.jaxb.adapters.pages.PageAdapterUtil.PageAdaptedData;

import de.hybris.platform.cmsocc.data.CMSPageWsDTO;

import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This adapter is used to convert {@link CMSPageWsDTO} into {@link PageAdaptedData}
 */
public class PageWsDTOAdapter extends XmlAdapter<PageAdaptedData, CMSPageWsDTO>
{
	@Override
	public PageAdaptedData marshal(final CMSPageWsDTO page)
	{
		return Objects.nonNull(page) ? convert(page) : null;
	}

	protected PageAdaptedData convert(final CMSPageWsDTO page)
	{
		return PageAdapterUtil.convert(page);
	}

	@Override
	public CMSPageWsDTO unmarshal(final PageAdaptedData pageAdaptedData)
	{
		throw new UnsupportedOperationException();
	}
}
