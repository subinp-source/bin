/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class EDMXEnd
{


	private String multiplicity;
	private String role;
	private String type;

	public String getMultiplicity()
	{
		return multiplicity;
	}

	@XmlAttribute(name = "Multiplicity")
	public void setMultiplicity(String multiplicity)
	{
		this.multiplicity = multiplicity;
	}

	public String getRole()
	{
		return role;
	}

	@XmlAttribute(name = "Role")
	public void setRole(String role)
	{
		this.role = role;
	}

	public String getType()
	{
		return type;
	}

	@XmlAttribute(name = "Type")
	public void setType(String type)
	{
		this.type = type;
	}
}
