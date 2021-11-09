/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class EDMXProperty
{
	private String name;

	private String type;

	private String nullable;

	private String isLanguageDependent;

	private String isUnique;

	private String alias;

	public String getName()
	{
		return name;
	}

	@XmlAttribute(name = "Name")
	public void setName(String name)
	{
		this.name = name;
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

	public String getNullable()
	{
		return nullable;
	}

	@XmlAttribute(name = "Nullable")
	public void setNullable(String nullable)
	{
		this.nullable = nullable;
	}

	public String getIsLanguageDependent()
	{
		return isLanguageDependent;
	}

	@XmlAttribute(name = "IsLanguageDependent", namespace = "http://schemas.sap.com/commerce")
	public void setIsLanguageDependent(String isLanguageDependent)
	{
		this.isLanguageDependent = isLanguageDependent;
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

	public String getAlias()
	{
		return alias;
	}

	@XmlAttribute(name = "Alias", namespace = "http://schemas.sap.com/commerce")
	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	@Override
	public String toString()
	{
		return "Property [Name=" + name + ", Type = " + type + ", Nullable = " + nullable + ", Name = " + name + " , isLanguageDependent= " + isLanguageDependent + ", isUnique=" + isUnique + ", alias=" + alias + "]";
	}
}
