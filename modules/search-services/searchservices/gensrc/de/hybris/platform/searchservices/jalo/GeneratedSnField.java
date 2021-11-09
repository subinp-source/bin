/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndexType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnField}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnField extends GenericItem
{
	/** Qualifier of the <code>SnField.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnField.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SnField.fieldType</code> attribute **/
	public static final String FIELDTYPE = "fieldType";
	/** Qualifier of the <code>SnField.valueProvider</code> attribute **/
	public static final String VALUEPROVIDER = "valueProvider";
	/** Qualifier of the <code>SnField.valueProviderParameters</code> attribute **/
	public static final String VALUEPROVIDERPARAMETERS = "valueProviderParameters";
	/** Qualifier of the <code>SnField.retrievable</code> attribute **/
	public static final String RETRIEVABLE = "retrievable";
	/** Qualifier of the <code>SnField.searchable</code> attribute **/
	public static final String SEARCHABLE = "searchable";
	/** Qualifier of the <code>SnField.localized</code> attribute **/
	public static final String LOCALIZED = "localized";
	/** Qualifier of the <code>SnField.qualifierTypeId</code> attribute **/
	public static final String QUALIFIERTYPEID = "qualifierTypeId";
	/** Qualifier of the <code>SnField.multiValued</code> attribute **/
	public static final String MULTIVALUED = "multiValued";
	/** Qualifier of the <code>SnField.useForSpellchecking</code> attribute **/
	public static final String USEFORSPELLCHECKING = "useForSpellchecking";
	/** Qualifier of the <code>SnField.useForSuggesting</code> attribute **/
	public static final String USEFORSUGGESTING = "useForSuggesting";
	/** Qualifier of the <code>SnField.weight</code> attribute **/
	public static final String WEIGHT = "weight";
	/** Qualifier of the <code>SnField.indexTypePOS</code> attribute **/
	public static final String INDEXTYPEPOS = "indexTypePOS";
	/** Qualifier of the <code>SnField.indexType</code> attribute **/
	public static final String INDEXTYPE = "indexType";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INDEXTYPE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnField> INDEXTYPEHANDLER = new BidirectionalOneToManyHandler<GeneratedSnField>(
	SearchservicesConstants.TC.SNFIELD,
	false,
	"indexType",
	"indexTypePOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(FIELDTYPE, AttributeMode.INITIAL);
		tmp.put(VALUEPROVIDER, AttributeMode.INITIAL);
		tmp.put(VALUEPROVIDERPARAMETERS, AttributeMode.INITIAL);
		tmp.put(RETRIEVABLE, AttributeMode.INITIAL);
		tmp.put(SEARCHABLE, AttributeMode.INITIAL);
		tmp.put(LOCALIZED, AttributeMode.INITIAL);
		tmp.put(QUALIFIERTYPEID, AttributeMode.INITIAL);
		tmp.put(MULTIVALUED, AttributeMode.INITIAL);
		tmp.put(USEFORSPELLCHECKING, AttributeMode.INITIAL);
		tmp.put(USEFORSUGGESTING, AttributeMode.INITIAL);
		tmp.put(WEIGHT, AttributeMode.INITIAL);
		tmp.put(INDEXTYPEPOS, AttributeMode.INITIAL);
		tmp.put(INDEXTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		INDEXTYPEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.fieldType</code> attribute.
	 * @return the fieldType
	 */
	public EnumerationValue getFieldType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, FIELDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.fieldType</code> attribute.
	 * @return the fieldType
	 */
	public EnumerationValue getFieldType()
	{
		return getFieldType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.fieldType</code> attribute. 
	 * @param value the fieldType
	 */
	public void setFieldType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, FIELDTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.fieldType</code> attribute. 
	 * @param value the fieldType
	 */
	public void setFieldType(final EnumerationValue value)
	{
		setFieldType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexType</code> attribute.
	 * @return the indexType
	 */
	public SnIndexType getIndexType(final SessionContext ctx)
	{
		return (SnIndexType)getProperty( ctx, INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexType</code> attribute.
	 * @return the indexType
	 */
	public SnIndexType getIndexType()
	{
		return getIndexType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexType</code> attribute. 
	 * @param value the indexType
	 */
	protected void setIndexType(final SessionContext ctx, final SnIndexType value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+INDEXTYPE+"' is not changeable", 0 );
		}
		INDEXTYPEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexType</code> attribute. 
	 * @param value the indexType
	 */
	protected void setIndexType(final SnIndexType value)
	{
		setIndexType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexTypePOS</code> attribute.
	 * @return the indexTypePOS
	 */
	 Integer getIndexTypePOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, INDEXTYPEPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexTypePOS</code> attribute.
	 * @return the indexTypePOS
	 */
	 Integer getIndexTypePOS()
	{
		return getIndexTypePOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @return the indexTypePOS
	 */
	 int getIndexTypePOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getIndexTypePOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @return the indexTypePOS
	 */
	 int getIndexTypePOSAsPrimitive()
	{
		return getIndexTypePOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, INDEXTYPEPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final Integer value)
	{
		setIndexTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final SessionContext ctx, final int value)
	{
		setIndexTypePOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final int value)
	{
		setIndexTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.localized</code> attribute.
	 * @return the localized
	 */
	public Boolean isLocalized(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, LOCALIZED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.localized</code> attribute.
	 * @return the localized
	 */
	public Boolean isLocalized()
	{
		return isLocalized( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.localized</code> attribute. 
	 * @return the localized
	 */
	public boolean isLocalizedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isLocalized( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.localized</code> attribute. 
	 * @return the localized
	 */
	public boolean isLocalizedAsPrimitive()
	{
		return isLocalizedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.localized</code> attribute. 
	 * @param value the localized
	 */
	public void setLocalized(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, LOCALIZED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.localized</code> attribute. 
	 * @param value the localized
	 */
	public void setLocalized(final Boolean value)
	{
		setLocalized( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.localized</code> attribute. 
	 * @param value the localized
	 */
	public void setLocalized(final SessionContext ctx, final boolean value)
	{
		setLocalized( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.localized</code> attribute. 
	 * @param value the localized
	 */
	public void setLocalized(final boolean value)
	{
		setLocalized( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.multiValued</code> attribute.
	 * @return the multiValued
	 */
	public Boolean isMultiValued(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, MULTIVALUED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.multiValued</code> attribute.
	 * @return the multiValued
	 */
	public Boolean isMultiValued()
	{
		return isMultiValued( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.multiValued</code> attribute. 
	 * @return the multiValued
	 */
	public boolean isMultiValuedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isMultiValued( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.multiValued</code> attribute. 
	 * @return the multiValued
	 */
	public boolean isMultiValuedAsPrimitive()
	{
		return isMultiValuedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.multiValued</code> attribute. 
	 * @param value the multiValued
	 */
	public void setMultiValued(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, MULTIVALUED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.multiValued</code> attribute. 
	 * @param value the multiValued
	 */
	public void setMultiValued(final Boolean value)
	{
		setMultiValued( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.multiValued</code> attribute. 
	 * @param value the multiValued
	 */
	public void setMultiValued(final SessionContext ctx, final boolean value)
	{
		setMultiValued( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.multiValued</code> attribute. 
	 * @param value the multiValued
	 */
	public void setMultiValued(final boolean value)
	{
		setMultiValued( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnField.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSnField.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.qualifierTypeId</code> attribute.
	 * @return the qualifierTypeId
	 */
	public String getQualifierTypeId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, QUALIFIERTYPEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.qualifierTypeId</code> attribute.
	 * @return the qualifierTypeId
	 */
	public String getQualifierTypeId()
	{
		return getQualifierTypeId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.qualifierTypeId</code> attribute. 
	 * @param value the qualifierTypeId
	 */
	public void setQualifierTypeId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, QUALIFIERTYPEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.qualifierTypeId</code> attribute. 
	 * @param value the qualifierTypeId
	 */
	public void setQualifierTypeId(final String value)
	{
		setQualifierTypeId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.retrievable</code> attribute.
	 * @return the retrievable
	 */
	public Boolean isRetrievable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, RETRIEVABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.retrievable</code> attribute.
	 * @return the retrievable
	 */
	public Boolean isRetrievable()
	{
		return isRetrievable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.retrievable</code> attribute. 
	 * @return the retrievable
	 */
	public boolean isRetrievableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isRetrievable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.retrievable</code> attribute. 
	 * @return the retrievable
	 */
	public boolean isRetrievableAsPrimitive()
	{
		return isRetrievableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.retrievable</code> attribute. 
	 * @param value the retrievable
	 */
	public void setRetrievable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, RETRIEVABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.retrievable</code> attribute. 
	 * @param value the retrievable
	 */
	public void setRetrievable(final Boolean value)
	{
		setRetrievable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.retrievable</code> attribute. 
	 * @param value the retrievable
	 */
	public void setRetrievable(final SessionContext ctx, final boolean value)
	{
		setRetrievable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.retrievable</code> attribute. 
	 * @param value the retrievable
	 */
	public void setRetrievable(final boolean value)
	{
		setRetrievable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.searchable</code> attribute.
	 * @return the searchable
	 */
	public Boolean isSearchable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SEARCHABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.searchable</code> attribute.
	 * @return the searchable
	 */
	public Boolean isSearchable()
	{
		return isSearchable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.searchable</code> attribute. 
	 * @return the searchable
	 */
	public boolean isSearchableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSearchable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.searchable</code> attribute. 
	 * @return the searchable
	 */
	public boolean isSearchableAsPrimitive()
	{
		return isSearchableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.searchable</code> attribute. 
	 * @param value the searchable
	 */
	public void setSearchable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SEARCHABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.searchable</code> attribute. 
	 * @param value the searchable
	 */
	public void setSearchable(final Boolean value)
	{
		setSearchable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.searchable</code> attribute. 
	 * @param value the searchable
	 */
	public void setSearchable(final SessionContext ctx, final boolean value)
	{
		setSearchable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.searchable</code> attribute. 
	 * @param value the searchable
	 */
	public void setSearchable(final boolean value)
	{
		setSearchable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSpellchecking</code> attribute.
	 * @return the useForSpellchecking
	 */
	public Boolean isUseForSpellchecking(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, USEFORSPELLCHECKING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSpellchecking</code> attribute.
	 * @return the useForSpellchecking
	 */
	public Boolean isUseForSpellchecking()
	{
		return isUseForSpellchecking( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @return the useForSpellchecking
	 */
	public boolean isUseForSpellcheckingAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isUseForSpellchecking( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @return the useForSpellchecking
	 */
	public boolean isUseForSpellcheckingAsPrimitive()
	{
		return isUseForSpellcheckingAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @param value the useForSpellchecking
	 */
	public void setUseForSpellchecking(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, USEFORSPELLCHECKING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @param value the useForSpellchecking
	 */
	public void setUseForSpellchecking(final Boolean value)
	{
		setUseForSpellchecking( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @param value the useForSpellchecking
	 */
	public void setUseForSpellchecking(final SessionContext ctx, final boolean value)
	{
		setUseForSpellchecking( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSpellchecking</code> attribute. 
	 * @param value the useForSpellchecking
	 */
	public void setUseForSpellchecking(final boolean value)
	{
		setUseForSpellchecking( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSuggesting</code> attribute.
	 * @return the useForSuggesting
	 */
	public Boolean isUseForSuggesting(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, USEFORSUGGESTING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSuggesting</code> attribute.
	 * @return the useForSuggesting
	 */
	public Boolean isUseForSuggesting()
	{
		return isUseForSuggesting( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @return the useForSuggesting
	 */
	public boolean isUseForSuggestingAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isUseForSuggesting( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @return the useForSuggesting
	 */
	public boolean isUseForSuggestingAsPrimitive()
	{
		return isUseForSuggestingAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @param value the useForSuggesting
	 */
	public void setUseForSuggesting(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, USEFORSUGGESTING,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @param value the useForSuggesting
	 */
	public void setUseForSuggesting(final Boolean value)
	{
		setUseForSuggesting( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @param value the useForSuggesting
	 */
	public void setUseForSuggesting(final SessionContext ctx, final boolean value)
	{
		setUseForSuggesting( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.useForSuggesting</code> attribute. 
	 * @param value the useForSuggesting
	 */
	public void setUseForSuggesting(final boolean value)
	{
		setUseForSuggesting( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProvider</code> attribute.
	 * @return the valueProvider
	 */
	public String getValueProvider(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VALUEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProvider</code> attribute.
	 * @return the valueProvider
	 */
	public String getValueProvider()
	{
		return getValueProvider( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.valueProvider</code> attribute. 
	 * @param value the valueProvider
	 */
	public void setValueProvider(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VALUEPROVIDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.valueProvider</code> attribute. 
	 * @param value the valueProvider
	 */
	public void setValueProvider(final String value)
	{
		setValueProvider( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProviderParameters</code> attribute.
	 * @return the valueProviderParameters
	 */
	public Map<String,String> getAllValueProviderParameters(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, VALUEPROVIDERPARAMETERS);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.valueProviderParameters</code> attribute.
	 * @return the valueProviderParameters
	 */
	public Map<String,String> getAllValueProviderParameters()
	{
		return getAllValueProviderParameters( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.valueProviderParameters</code> attribute. 
	 * @param value the valueProviderParameters
	 */
	public void setAllValueProviderParameters(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, VALUEPROVIDERPARAMETERS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.valueProviderParameters</code> attribute. 
	 * @param value the valueProviderParameters
	 */
	public void setAllValueProviderParameters(final Map<String,String> value)
	{
		setAllValueProviderParameters( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.weight</code> attribute.
	 * @return the weight
	 */
	public Float getWeight(final SessionContext ctx)
	{
		return (Float)getProperty( ctx, WEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.weight</code> attribute.
	 * @return the weight
	 */
	public Float getWeight()
	{
		return getWeight( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.weight</code> attribute. 
	 * @return the weight
	 */
	public float getWeightAsPrimitive(final SessionContext ctx)
	{
		Float value = getWeight( ctx );
		return value != null ? value.floatValue() : 0.0f;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnField.weight</code> attribute. 
	 * @return the weight
	 */
	public float getWeightAsPrimitive()
	{
		return getWeightAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final Float value)
	{
		setProperty(ctx, WEIGHT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final Float value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final float value)
	{
		setWeight( ctx,Float.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnField.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final float value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
}
