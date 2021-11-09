/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.pages;

import de.hybris.platform.cmsocc.data.CMSPageListWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.pages.PageAdapterUtil.PageAdaptedData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;


/**
 * This adapter is used to convert {@link de.hybris.platform.cmsocc.data.CMSPageListWsDTO} into
 * {@link PageListWsDTOAdapter.ListAdaptedPages}
 */
public class PageListWsDTOAdapter extends XmlAdapter<PageListWsDTOAdapter.ListAdaptedPages, CMSPageListWsDTO>
{
	@XmlRootElement(name = "pages")
	public static class ListAdaptedPages
	{
		@XmlElement(name = "page")
		public List<PageAdaptedData> page = new ArrayList<>();

		private PaginationWsDTO pagination;

		private List<SortWsDTO> sorts;

		public PaginationWsDTO getPagination()
		{
			return pagination;
		}

		public void setPagination(final PaginationWsDTO pagination)
		{
			this.pagination = pagination;
		}

		public List<SortWsDTO> getSorts()
		{
			return sorts;
		}

		public void setSorts(final List<SortWsDTO> sorts)
		{
			this.sorts = sorts;
		}

		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(false);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}
	}

	@Override
	public ListAdaptedPages marshal(final CMSPageListWsDTO pageList)
	{
		if (Objects.isNull(pageList) || Objects.isNull(pageList.getPage()))
		{
			return null;
		}

		final ListAdaptedPages listAdaptedPages = new ListAdaptedPages();
		final List<PageAdaptedData> convertedPages = pageList.getPage().stream() //
				.map(pageDTO -> PageAdapterUtil.convert(pageDTO, false)) //
				.collect(Collectors.toList());
		listAdaptedPages.page.addAll(convertedPages);
		return listAdaptedPages;
	}

	@Override
	public final CMSPageListWsDTO unmarshal(final ListAdaptedPages listAdaptedPages) throws Exception
	{
		throw new UnsupportedOperationException();
	}

}
