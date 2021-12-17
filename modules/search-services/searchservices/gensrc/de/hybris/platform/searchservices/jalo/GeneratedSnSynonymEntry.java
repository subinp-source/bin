/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnSynonymDictionary;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnSynonymEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnSynonymEntry extends GenericItem
{
	/** Qualifier of the <code>SnSynonymEntry.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnSynonymEntry.input</code> attribute **/
	public static final String INPUT = "input";
	/** Qualifier of the <code>SnSynonymEntry.synonyms</code> attribute **/
	public static final String SYNONYMS = "synonyms";
	/** Qualifier of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute **/
	public static final String SYNONYMDICTIONARYPOS = "synonymDictionaryPOS";
	/** Qualifier of the <code>SnSynonymEntry.synonymDictionary</code> attribute **/
	public static final String SYNONYMDICTIONARY = "synonymDictionary";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SYNONYMDICTIONARY's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnSynonymEntry> SYNONYMDICTIONARYHANDLER = new BidirectionalOneToManyHandler<GeneratedSnSynonymEntry>(
	SearchservicesConstants.TC.SNSYNONYMENTRY,
	false,
	"synonymDictionary",
	"synonymDictionaryPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(INPUT, AttributeMode.INITIAL);
		tmp.put(SYNONYMS, AttributeMode.INITIAL);
		tmp.put(SYNONYMDICTIONARYPOS, AttributeMode.INITIAL);
		tmp.put(SYNONYMDICTIONARY, AttributeMode.INITIAL);
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
		SYNONYMDICTIONARYHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.input</code> attribute.
	 * @return the input
	 */
	public List<String> getInput(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, INPUT);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.input</code> attribute.
	 * @return the input
	 */
	public List<String> getInput()
	{
		return getInput( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.input</code> attribute. 
	 * @param value the input
	 */
	public void setInput(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, INPUT,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.input</code> attribute. 
	 * @param value the input
	 */
	public void setInput(final List<String> value)
	{
		setInput( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionary</code> attribute.
	 * @return the synonymDictionary
	 */
	public SnSynonymDictionary getSynonymDictionary(final SessionContext ctx)
	{
		return (SnSynonymDictionary)getProperty( ctx, SYNONYMDICTIONARY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionary</code> attribute.
	 * @return the synonymDictionary
	 */
	public SnSynonymDictionary getSynonymDictionary()
	{
		return getSynonymDictionary( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionary</code> attribute. 
	 * @param value the synonymDictionary
	 */
	protected void setSynonymDictionary(final SessionContext ctx, final SnSynonymDictionary value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+SYNONYMDICTIONARY+"' is not changeable", 0 );
		}
		SYNONYMDICTIONARYHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionary</code> attribute. 
	 * @param value the synonymDictionary
	 */
	protected void setSynonymDictionary(final SnSynonymDictionary value)
	{
		setSynonymDictionary( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute.
	 * @return the synonymDictionaryPOS
	 */
	 Integer getSynonymDictionaryPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, SYNONYMDICTIONARYPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute.
	 * @return the synonymDictionaryPOS
	 */
	 Integer getSynonymDictionaryPOS()
	{
		return getSynonymDictionaryPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @return the synonymDictionaryPOS
	 */
	 int getSynonymDictionaryPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getSynonymDictionaryPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @return the synonymDictionaryPOS
	 */
	 int getSynonymDictionaryPOSAsPrimitive()
	{
		return getSynonymDictionaryPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @param value the synonymDictionaryPOS
	 */
	 void setSynonymDictionaryPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, SYNONYMDICTIONARYPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @param value the synonymDictionaryPOS
	 */
	 void setSynonymDictionaryPOS(final Integer value)
	{
		setSynonymDictionaryPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @param value the synonymDictionaryPOS
	 */
	 void setSynonymDictionaryPOS(final SessionContext ctx, final int value)
	{
		setSynonymDictionaryPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonymDictionaryPOS</code> attribute. 
	 * @param value the synonymDictionaryPOS
	 */
	 void setSynonymDictionaryPOS(final int value)
	{
		setSynonymDictionaryPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonyms</code> attribute.
	 * @return the synonyms
	 */
	public List<String> getSynonyms(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, SYNONYMS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnSynonymEntry.synonyms</code> attribute.
	 * @return the synonyms
	 */
	public List<String> getSynonyms()
	{
		return getSynonyms( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonyms</code> attribute. 
	 * @param value the synonyms
	 */
	public void setSynonyms(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, SYNONYMS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnSynonymEntry.synonyms</code> attribute. 
	 * @param value the synonyms
	 */
	public void setSynonyms(final List<String> value)
	{
		setSynonyms( getSession().getSessionContext(), value );
	}
	
}
