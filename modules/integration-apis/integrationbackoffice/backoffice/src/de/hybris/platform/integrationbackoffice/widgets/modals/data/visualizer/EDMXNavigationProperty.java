/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAttribute;

public class EDMXNavigationProperty
{
	private String relationship;

	private String fromRole;

	private String toRole;

	private String name;

	private String nullable;

	private String isAutoCreate;

	private String isUnique;

	private String isPartOf;

	public String getRelationship()
	{
		return relationship;
	}

	@XmlAttribute(name = "Relationship")
	public void setRelationship(String relationship)
	{
		this.relationship = relationship;
	}

	public String getFromRole()
	{
		return fromRole;
	}

	@XmlAttribute(name = "FromRole")
	public void setFromRole(String fromRole)
	{
		this.fromRole = fromRole;
	}

	public String getToRole()
	{
		return toRole;
	}

	@XmlAttribute(name = "ToRole")
	public void setToRole(String toRole)
	{
		this.toRole = toRole;
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

	@Override
	public String toString()
	{
		return "NavigationProperty [relationship = " + relationship + ", fromRole = " + fromRole + ", toRole = " + toRole + ", name = " + name + "]";
	}

	public String getNullable()
	{
		return nullable;
	}

	@XmlAttribute(name = "Nullable")
	public void setNullable(String nullable)
	{
		this.nullable = nullable;
	}

	public String getIsAutoCreate()
	{
		return isAutoCreate;
	}

	@XmlAttribute(name = "IsAutoCreate", namespace = "http://schemas.sap.com/commerce")
	public void setIsAutoCreate(String isAutoCreate)
	{
		this.isAutoCreate = isAutoCreate;
	}

	public String getIsUnique()
	{
		return isUnique;
	}

	@XmlAttribute(name = "IsUnique", namespace = "http://schemas.sap.com/commerce")
	public void setIsUnique(String isUnique)
	{
		this.isUnique = isUnique;
	}

	public String getIsPartOf()
	{
		return isPartOf;
	}

	@XmlAttribute(name = "IsPartOf", namespace = "http://schemas.sap.com/commerce")
	public void setIsPartOf(String isPartOf)
	{
		this.isPartOf = isPartOf;
	}
}
