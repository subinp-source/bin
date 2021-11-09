/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@XmlRootElement(name = "Schema")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EDMXSchema
{
	private String namespace;

	private List<EDMXEntityType> edmxEntityTypes;

	private List<EDMXAssociation> edmxAssociations;

	private Map<String, EDMXAssociation> mapOfEDMXAssociation;


	public List<EDMXAssociation> getEdmxAssociations()
	{
		if (edmxAssociations == null)
		{
			edmxAssociations = new LinkedList<>();
		}
		return edmxAssociations;
	}

	@XmlElement(name = "Association", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setEdmxAssociations(List<EDMXAssociation> edmxAssociations)
	{
		this.edmxAssociations = edmxAssociations;


	}

	public String getNamespace()
	{
		return namespace;
	}

	@XmlAttribute(name = "Namespace")
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	@Override
	public String toString()
	{
		return "Schema [EntityList = " + getEdmxEntityTypes() + ", Namespace = " + getNamespace() + "]";
	}

	public List<EDMXEntityType> getEdmxEntityTypes()
	{
		if (edmxEntityTypes == null)
		{
			edmxEntityTypes = new LinkedList<>();
		}
		return edmxEntityTypes;
	}

	@XmlElement(name = "EntityType", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setEdmxEntityTypes(List<EDMXEntityType> edmxEntityTypes)
	{
		this.edmxEntityTypes = edmxEntityTypes;
	}


	public Map<String, EDMXAssociation> getMapOfEDMXAssociation()
	{
		if (mapOfEDMXAssociation == null)
		{
			if (edmxAssociations != null)
			{
				mapOfEDMXAssociation = edmxAssociations.stream()
				                                       .collect(Collectors.toMap(EDMXAssociation::getName, Function.identity()));
			}
			else
			{
				mapOfEDMXAssociation = new HashMap<>();
			}
		}
		return mapOfEDMXAssociation;
	}

}
