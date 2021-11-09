/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

public class CreateVirtualAttributeModalData
{

	private String alias;
	private String vadmCode;
	private String scriptLocation;
	private String type;
	private boolean isDescriptorPersisted;

	public CreateVirtualAttributeModalData(final String alias, final String vadmCode, final String scriptLocation,
	                                       final String type, final boolean isDescriptorPersisted)
	{
		this.alias = alias;
		this.vadmCode = vadmCode;
		this.scriptLocation = scriptLocation;
		this.type = type;
		this.isDescriptorPersisted = isDescriptorPersisted;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getVadmCode()
	{
		return vadmCode;
	}

	public String getScriptLocation()
	{
		return scriptLocation;
	}

	public String getType()
	{
		return type;
	}

	public boolean getIsDescriptorPersisted()
	{
		return isDescriptorPersisted;
	}

}
