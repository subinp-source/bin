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
public class EDMXAssociation
{

	private String name;
	private List<EDMXEnd> edmxEnd;


	public List<EDMXEnd> getEdmxEnd()
	{
		if (edmxEnd == null)
		{
			edmxEnd = new LinkedList<>();
		}
		return edmxEnd;
	}

	@XmlElement(name = "End", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setEdmxEnd(List<EDMXEnd> edmxEnd)
	{
		this.edmxEnd = edmxEnd;
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


}
