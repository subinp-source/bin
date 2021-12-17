/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.overview;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue;
import java.util.List;


import java.util.Objects;
public  class CharacteristicGroup  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CharacteristicGroup.id</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>CharacteristicGroup.groupDescription</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String groupDescription;

	/** <i>Generated property</i> for <code>CharacteristicGroup.isSelectedTopLevelGroup</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean isSelectedTopLevelGroup;

	/** <i>Generated property</i> for <code>CharacteristicGroup.subGroups</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<CharacteristicGroup> subGroups;

	/** <i>Generated property</i> for <code>CharacteristicGroup.characteristicValues</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<CharacteristicValue> characteristicValues;
	
	public CharacteristicGroup()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setGroupDescription(final String groupDescription)
	{
		this.groupDescription = groupDescription;
	}

	public String getGroupDescription() 
	{
		return groupDescription;
	}
	
	public void setIsSelectedTopLevelGroup(final boolean isSelectedTopLevelGroup)
	{
		this.isSelectedTopLevelGroup = isSelectedTopLevelGroup;
	}

	public boolean isIsSelectedTopLevelGroup() 
	{
		return isSelectedTopLevelGroup;
	}
	
	public void setSubGroups(final List<CharacteristicGroup> subGroups)
	{
		this.subGroups = subGroups;
	}

	public List<CharacteristicGroup> getSubGroups() 
	{
		return subGroups;
	}
	
	public void setCharacteristicValues(final List<CharacteristicValue> characteristicValues)
	{
		this.characteristicValues = characteristicValues;
	}

	public List<CharacteristicValue> getCharacteristicValues() 
	{
		return characteristicValues;
	}
	

}