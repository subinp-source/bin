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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndex;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnIndexerOperation}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnIndexerOperation extends GenericItem
{
	/** Qualifier of the <code>SnIndexerOperation.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnIndexerOperation.index</code> attribute **/
	public static final String INDEX = "index";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INDEX's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSnIndexerOperation> INDEXHANDLER = new BidirectionalOneToManyHandler<GeneratedSnIndexerOperation>(
	SearchservicesConstants.TC.SNINDEXEROPERATION,
	false,
	"index",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(INDEX, AttributeMode.INITIAL);
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
		INDEXHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerOperation.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerOperation.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.index</code> attribute.
	 * @return the index
	 */
	public SnIndex getIndex(final SessionContext ctx)
	{
		return (SnIndex)getProperty( ctx, INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndexerOperation.index</code> attribute.
	 * @return the index
	 */
	public SnIndex getIndex()
	{
		return getIndex( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerOperation.index</code> attribute. 
	 * @param value the index
	 */
	protected void setIndex(final SessionContext ctx, final SnIndex value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+INDEX+"' is not changeable", 0 );
		}
		INDEXHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndexerOperation.index</code> attribute. 
	 * @param value the index
	 */
	protected void setIndex(final SnIndex value)
	{
		setIndex( getSession().getSessionContext(), value );
	}
	
}
