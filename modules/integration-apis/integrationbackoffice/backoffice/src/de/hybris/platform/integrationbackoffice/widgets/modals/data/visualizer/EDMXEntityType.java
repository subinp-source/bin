/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import java.util.LinkedList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class EDMXEntityType
{
	private EDMXKey edmxKey;

	private String name;

	private List<EDMXProperty> properties;

	private List<EDMXNavigationProperty> navigationProperties;

	public EDMXKey getEdmxKey()
	{
		return edmxKey;
	}

	@XmlElement(name = "Key", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setEdmxKey(EDMXKey edmxKey)
	{
		this.edmxKey = edmxKey;
	}

	public String getName()
	{
		return name;
	}

	@XmlAttribute(name = "Name")
	public void setName(String name)
	{
		this.name = name;
	}

	public List<EDMXProperty> getProperties()
	{
		if (properties == null)
		{
			properties = new LinkedList<>();
		}
		return properties;
	}

	@XmlElement(name = "Property", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setProperties(List<EDMXProperty> properties)
	{
		this.properties = properties;
	}

	@Override
	public String toString()
	{
		return "EntityType [ Key = " + edmxKey + ", Name = " + name + ", Properties= " + properties + ", NavigationProperty = " + navigationProperties + "]";
	}

	public List<EDMXNavigationProperty> getNavigationProperties()
	{
		if (navigationProperties == null)
		{
			navigationProperties = new LinkedList<>();
		}
		return navigationProperties;
	}

	@XmlElement(name = "NavigationProperty", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setNavigationProperties(List<EDMXNavigationProperty> navigationProperties)
	{
		this.navigationProperties = navigationProperties;
	}
}
