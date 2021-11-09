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
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndexerOperation;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SnIndex}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnIndex extends GenericItem
{
	/** Qualifier of the <code>SnIndex.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>SnIndex.indexerOperations</code> attribute **/
	public static final String INDEXEROPERATIONS = "indexerOperations";
	/**
	* {@link OneToManyHandler} for handling 1:n INDEXEROPERATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnIndexerOperation> INDEXEROPERATIONSHANDLER = new OneToManyHandler<SnIndexerOperation>(
	SearchservicesConstants.TC.SNINDEXEROPERATION,
	true,
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
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndex.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndex.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndex.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndex.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndex.indexerOperations</code> attribute.
	 * @return the indexerOperations
	 */
	public List<SnIndexerOperation> getIndexerOperations(final SessionContext ctx)
	{
		return (List<SnIndexerOperation>)INDEXEROPERATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnIndex.indexerOperations</code> attribute.
	 * @return the indexerOperations
	 */
	public List<SnIndexerOperation> getIndexerOperations()
	{
		return getIndexerOperations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndex.indexerOperations</code> attribute. 
	 * @param value the indexerOperations
	 */
	public void setIndexerOperations(final SessionContext ctx, final List<SnIndexerOperation> value)
	{
		INDEXEROPERATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnIndex.indexerOperations</code> attribute. 
	 * @param value the indexerOperations
	 */
	public void setIndexerOperations(final List<SnIndexerOperation> value)
	{
		setIndexerOperations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerOperations. 
	 * @param value the item to add to indexerOperations
	 */
	public void addToIndexerOperations(final SessionContext ctx, final SnIndexerOperation value)
	{
		INDEXEROPERATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexerOperations. 
	 * @param value the item to add to indexerOperations
	 */
	public void addToIndexerOperations(final SnIndexerOperation value)
	{
		addToIndexerOperations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerOperations. 
	 * @param value the item to remove from indexerOperations
	 */
	public void removeFromIndexerOperations(final SessionContext ctx, final SnIndexerOperation value)
	{
		INDEXEROPERATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexerOperations. 
	 * @param value the item to remove from indexerOperations
	 */
	public void removeFromIndexerOperations(final SnIndexerOperation value)
	{
		removeFromIndexerOperations( getSession().getSessionContext(), value );
	}
	
}
