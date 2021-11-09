/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.jalo;

import de.hybris.platform.integrationservices.constants.IntegrationservicesConstants;
import de.hybris.platform.integrationservices.jalo.IntegrationObjectItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem IntegrationObject}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedIntegrationObject extends GenericItem
{
	/** Qualifier of the <code>IntegrationObject.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>IntegrationObject.items</code> attribute **/
	public static final String ITEMS = "items";
	/**
	* {@link OneToManyHandler} for handling 1:n ITEMS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<IntegrationObjectItem> ITEMSHANDLER = new OneToManyHandler<IntegrationObjectItem>(
	IntegrationservicesConstants.TC.INTEGRATIONOBJECTITEM,
	true,
	"integrationObject",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObject.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObject.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObject.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObject.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObject.items</code> attribute.
	 * @return the items
	 */
	public Set<IntegrationObjectItem> getItems(final SessionContext ctx)
	{
		return (Set<IntegrationObjectItem>)ITEMSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObject.items</code> attribute.
	 * @return the items
	 */
	public Set<IntegrationObjectItem> getItems()
	{
		return getItems( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObject.items</code> attribute. 
	 * @param value the items
	 */
	public void setItems(final SessionContext ctx, final Set<IntegrationObjectItem> value)
	{
		ITEMSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObject.items</code> attribute. 
	 * @param value the items
	 */
	public void setItems(final Set<IntegrationObjectItem> value)
	{
		setItems( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to items. 
	 * @param value the item to add to items
	 */
	public void addToItems(final SessionContext ctx, final IntegrationObjectItem value)
	{
		ITEMSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to items. 
	 * @param value the item to add to items
	 */
	public void addToItems(final IntegrationObjectItem value)
	{
		addToItems( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from items. 
	 * @param value the item to remove from items
	 */
	public void removeFromItems(final SessionContext ctx, final IntegrationObjectItem value)
	{
		ITEMSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from items. 
	 * @param value the item to remove from items
	 */
	public void removeFromItems(final IntegrationObjectItem value)
	{
		removeFromItems( getSession().getSessionContext(), value );
	}
	
}
