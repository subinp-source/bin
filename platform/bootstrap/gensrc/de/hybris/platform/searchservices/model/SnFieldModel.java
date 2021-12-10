/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type SnField first defined at extension searchservices.
 */
@SuppressWarnings("all")
public class SnFieldModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SnField";
	
	/**<i>Generated relation code constant for relation <code>SnIndexType2Field</code> defining source attribute <code>indexType</code> in extension <code>searchservices</code>.</i>*/
	public static final String _SNINDEXTYPE2FIELD = "SnIndexType2Field";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.id</code> attribute defined at extension <code>searchservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.name</code> attribute defined at extension <code>searchservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.fieldType</code> attribute defined at extension <code>searchservices</code>. */
	public static final String FIELDTYPE = "fieldType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.valueProvider</code> attribute defined at extension <code>searchservices</code>. */
	public static final String VALUEPROVIDER = "valueProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.valueProviderParameters</code> attribute defined at extension <code>searchservices</code>. */
	public static final String VALUEPROVIDERPARAMETERS = "valueProviderParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.retrievable</code> attribute defined at extension <code>searchservices</code>. */
	public static final String RETRIEVABLE = "retrievable";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.searchable</code> attribute defined at extension <code>searchservices</code>. */
	public static final String SEARCHABLE = "searchable";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.localized</code> attribute defined at extension <code>searchservices</code>. */
	public static final String LOCALIZED = "localized";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.qualifierTypeId</code> attribute defined at extension <code>searchservices</code>. */
	public static final String QUALIFIERTYPEID = "qualifierTypeId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.multiValued</code> attribute defined at extension <code>searchservices</code>. */
	public static final String MULTIVALUED = "multiValued";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.useForSpellchecking</code> attribute defined at extension <code>searchservices</code>. */
	public static final String USEFORSPELLCHECKING = "useForSpellchecking";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.useForSuggesting</code> attribute defined at extension <code>searchservices</code>. */
	public static final String USEFORSUGGESTING = "useForSuggesting";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.weight</code> attribute defined at extension <code>searchservices</code>. */
	public static final String WEIGHT = "weight";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.indexTypePOS</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXTYPEPOS = "indexTypePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SnField.indexType</code> attribute defined at extension <code>searchservices</code>. */
	public static final String INDEXTYPE = "indexType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SnFieldModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SnFieldModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _fieldType initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 * @param _id initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 * @param _indexType initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnFieldModel(final SnFieldType _fieldType, final String _id, final SnIndexTypeModel _indexType)
	{
		super();
		setFieldType(_fieldType);
		setId(_id);
		setIndexType(_indexType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _fieldType initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 * @param _id initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 * @param _indexType initial attribute declared by type <code>SnField</code> at extension <code>searchservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SnFieldModel(final SnFieldType _fieldType, final String _id, final SnIndexTypeModel _indexType, final ItemModel _owner)
	{
		super();
		setFieldType(_fieldType);
		setId(_id);
		setIndexType(_indexType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.fieldType</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the fieldType
	 */
	@Accessor(qualifier = "fieldType", type = Accessor.Type.GETTER)
	public SnFieldType getFieldType()
	{
		return getPersistenceContext().getPropertyValue(FIELDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.id</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexType</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.GETTER)
	public SnIndexTypeModel getIndexType()
	{
		return getPersistenceContext().getPropertyValue(INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.localized</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
	public Boolean getLocalized()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.multiValued</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the multiValued
	 */
	@Accessor(qualifier = "multiValued", type = Accessor.Type.GETTER)
	public Boolean getMultiValued()
	{
		return getPersistenceContext().getPropertyValue(MULTIVALUED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute defined at extension <code>searchservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.qualifierTypeId</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the qualifierTypeId
	 */
	@Accessor(qualifier = "qualifierTypeId", type = Accessor.Type.GETTER)
	public String getQualifierTypeId()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIERTYPEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.retrievable</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the retrievable
	 */
	@Accessor(qualifier = "retrievable", type = Accessor.Type.GETTER)
	public Boolean getRetrievable()
	{
		return getPersistenceContext().getPropertyValue(RETRIEVABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.searchable</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the searchable
	 */
	@Accessor(qualifier = "searchable", type = Accessor.Type.GETTER)
	public Boolean getSearchable()
	{
		return getPersistenceContext().getPropertyValue(SEARCHABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSpellchecking</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the useForSpellchecking
	 */
	@Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.GETTER)
	public Boolean getUseForSpellchecking()
	{
		return getPersistenceContext().getPropertyValue(USEFORSPELLCHECKING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSuggesting</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the useForSuggesting
	 */
	@Accessor(qualifier = "useForSuggesting", type = Accessor.Type.GETTER)
	public Boolean getUseForSuggesting()
	{
		return getPersistenceContext().getPropertyValue(USEFORSUGGESTING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProvider</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the valueProvider
	 */
	@Accessor(qualifier = "valueProvider", type = Accessor.Type.GETTER)
	public String getValueProvider()
	{
		return getPersistenceContext().getPropertyValue(VALUEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the valueProviderParameters
	 */
	@Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getValueProviderParameters()
	{
		return getPersistenceContext().getPropertyValue(VALUEPROVIDERPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.weight</code> attribute defined at extension <code>searchservices</code>. 
	 * @return the weight
	 */
	@Accessor(qualifier = "weight", type = Accessor.Type.GETTER)
	public Float getWeight()
	{
		return getPersistenceContext().getPropertyValue(WEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.fieldType</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the fieldType
	 */
	@Accessor(qualifier = "fieldType", type = Accessor.Type.SETTER)
	public void setFieldType(final SnFieldType value)
	{
		getPersistenceContext().setPropertyValue(FIELDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.id</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SnField.indexType</code> attribute defined at extension <code>searchservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexType
	 */
	@Accessor(qualifier = "indexType", type = Accessor.Type.SETTER)
	public void setIndexType(final SnIndexTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.localized</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
	public void setLocalized(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOCALIZED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.multiValued</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the multiValued
	 */
	@Accessor(qualifier = "multiValued", type = Accessor.Type.SETTER)
	public void setMultiValued(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MULTIVALUED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.name</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.qualifierTypeId</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the qualifierTypeId
	 */
	@Accessor(qualifier = "qualifierTypeId", type = Accessor.Type.SETTER)
	public void setQualifierTypeId(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIERTYPEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.retrievable</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the retrievable
	 */
	@Accessor(qualifier = "retrievable", type = Accessor.Type.SETTER)
	public void setRetrievable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(RETRIEVABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.searchable</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the searchable
	 */
	@Accessor(qualifier = "searchable", type = Accessor.Type.SETTER)
	public void setSearchable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.useForSpellchecking</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the useForSpellchecking
	 */
	@Accessor(qualifier = "useForSpellchecking", type = Accessor.Type.SETTER)
	public void setUseForSpellchecking(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USEFORSPELLCHECKING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.useForSuggesting</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the useForSuggesting
	 */
	@Accessor(qualifier = "useForSuggesting", type = Accessor.Type.SETTER)
	public void setUseForSuggesting(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USEFORSUGGESTING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.valueProvider</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the valueProvider
	 */
	@Accessor(qualifier = "valueProvider", type = Accessor.Type.SETTER)
	public void setValueProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUEPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.valueProviderParameters</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the valueProviderParameters
	 */
	@Accessor(qualifier = "valueProviderParameters", type = Accessor.Type.SETTER)
	public void setValueProviderParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(VALUEPROVIDERPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SnField.weight</code> attribute defined at extension <code>searchservices</code>. 
	 *  
	 * @param value the weight
	 */
	@Accessor(qualifier = "weight", type = Accessor.Type.SETTER)
	public void setWeight(final Float value)
	{
		getPersistenceContext().setPropertyValue(WEIGHT, value);
	}
	
}
