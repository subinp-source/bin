/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Key", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EDMXKey
{
	private EDMXPropertyRef edmxPropertyRef;

	public EDMXPropertyRef getEdmxPropertyRef()
	{
		return edmxPropertyRef;
	}

	@XmlElement(name = "PropertyRef", namespace = "http://schemas.microsoft.com/ado/2008/09/edm")
	public void setEdmxPropertyRef(EDMXPropertyRef edmxPropertyRef)
	{
		this.edmxPropertyRef = edmxPropertyRef;
	}

	@Override
	public String toString()
	{
		return "Key [PropertyRef = " + edmxPropertyRef + "]";
	}
}
