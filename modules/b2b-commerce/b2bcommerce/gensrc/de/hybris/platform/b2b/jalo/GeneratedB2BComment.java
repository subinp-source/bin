/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BCommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BComment B2BComment}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BComment extends GenericItem
{
	/** Qualifier of the <code>B2BComment.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>B2BComment.comment</code> attribute **/
	public static final String COMMENT = "comment";
	/** Qualifier of the <code>B2BComment.modifiedDate</code> attribute **/
	public static final String MODIFIEDDATE = "modifiedDate";
	/** Qualifier of the <code>B2BComment.order</code> attribute **/
	public static final String ORDER = "order";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ORDER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BComment> ORDERHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BComment>(
	B2BCommerceConstants.TC.B2BCOMMENT,
	false,
	"order",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(COMMENT, AttributeMode.INITIAL);
		tmp.put(MODIFIEDDATE, AttributeMode.INITIAL);
		tmp.put(ORDER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.comment</code> attribute.
	 * @return the comment
	 */
	public String getComment(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.comment</code> attribute.
	 * @return the comment
	 */
	public String getComment()
	{
		return getComment( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.comment</code> attribute. 
	 * @param value the comment
	 */
	public void setComment(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.comment</code> attribute. 
	 * @param value the comment
	 */
	public void setComment(final String value)
	{
		setComment( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		ORDERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.modifiedDate</code> attribute.
	 * @return the modifiedDate
	 */
	public Date getModifiedDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, MODIFIEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.modifiedDate</code> attribute.
	 * @return the modifiedDate
	 */
	public Date getModifiedDate()
	{
		return getModifiedDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.modifiedDate</code> attribute. 
	 * @param value the modifiedDate
	 */
	public void setModifiedDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, MODIFIEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.modifiedDate</code> attribute. 
	 * @param value the modifiedDate
	 */
	public void setModifiedDate(final Date value)
	{
		setModifiedDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.order</code> attribute.
	 * @return the order
	 */
	public AbstractOrder getOrder(final SessionContext ctx)
	{
		return (AbstractOrder)getProperty( ctx, ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.order</code> attribute.
	 * @return the order
	 */
	public AbstractOrder getOrder()
	{
		return getOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final SessionContext ctx, final AbstractOrder value)
	{
		ORDERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.order</code> attribute. 
	 * @param value the order
	 */
	public void setOrder(final AbstractOrder value)
	{
		setOrder( getSession().getSessionContext(), value );
	}
	
}
