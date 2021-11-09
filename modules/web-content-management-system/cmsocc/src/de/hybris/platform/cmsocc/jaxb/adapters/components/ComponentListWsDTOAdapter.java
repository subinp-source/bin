/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.components;

import static java.util.stream.Collectors.toList;

import de.hybris.platform.cmsocc.data.ComponentListWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.SortWsDTO;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;


/**
 * This adapter is used to convert {@link de.hybris.platform.cmsocc.data.ComponentListWsDTO} into
 * {@link ComponentListWsDTOAdapter.ListAdaptedComponents}
 */
public class ComponentListWsDTOAdapter extends XmlAdapter<ComponentListWsDTOAdapter.ListAdaptedComponents, ComponentListWsDTO>
{
	@XmlRootElement(name="components")
	public static class ListAdaptedComponents
	{
		@XmlElement(name = "component")
		public List<ComponentAdaptedData> component = new ArrayList<>();

		private PaginationWsDTO pagination;

		private List<SortWsDTO> sorts;

		/**
		 * @return the pagination
		 */
		public PaginationWsDTO getPagination()
		{
			return pagination;
		}

		/**
		 * @param pagination
		 *           the pagination to set
		 */
		public void setPagination(final PaginationWsDTO pagination)
		{
			this.pagination = pagination;
		}

		/**
		 * @return the sorts
		 */
		public List<SortWsDTO> getSorts()
		{
			return sorts;
		}

		/**
		 * @param sorts
		 *           the sorts to set
		 */
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
	public ListAdaptedComponents marshal(final ComponentListWsDTO componentList)
	{
		if (componentList == null || componentList.getComponent() == null)
		{
			return null;
		}
		final ListAdaptedComponents listAdaptedComponent = new ListAdaptedComponents();

		final List<ComponentAdaptedData> convertedComponents = componentList.getComponent() //
				.stream() //
				.map(ComponentAdapterUtil::convert) //
				.collect(toList());
		listAdaptedComponent.component.addAll(convertedComponents);

		return listAdaptedComponent;
	}

	@Override
	public ComponentListWsDTO unmarshal(final ListAdaptedComponents listAdapedComponents) throws Exception
	{
		throw new UnsupportedOperationException();
	}
}
