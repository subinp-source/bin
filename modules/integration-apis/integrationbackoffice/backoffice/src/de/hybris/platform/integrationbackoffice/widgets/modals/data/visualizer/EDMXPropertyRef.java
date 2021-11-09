/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PropertyRef", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
@XmlAccessorType(XmlAccessType.PROPERTY)

public class EDMXPropertyRef
{
	private String name;

	public String getName()
	{
		return name;
	}

	@XmlAttribute(name = "Name")
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "PropertyRef [Name = " + name + "]";
	}
}

