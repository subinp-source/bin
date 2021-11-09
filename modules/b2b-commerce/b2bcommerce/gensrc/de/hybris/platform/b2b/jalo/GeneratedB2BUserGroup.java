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
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BUserGroup B2BUserGroup}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BUserGroup extends UserGroup
{
	/** Qualifier of the <code>B2BUserGroup.Unit</code> attribute **/
	public static final String UNIT = "Unit";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n UNIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedB2BUserGroup> UNITHANDLER = new BidirectionalOneToManyHandler<GeneratedB2BUserGroup>(
	B2BCommerceConstants.TC.B2BUSERGROUP,
	false,
	"Unit",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(UNIT, AttributeMode.INITIAL);
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
		UNITHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUserGroup.Unit</code> attribute.
	 * @return the Unit - The Parent B2BUnit
	 */
	public B2BUnit getUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUserGroup.Unit</code> attribute.
	 * @return the Unit - The Parent B2BUnit
	 */
	public B2BUnit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUserGroup.Unit</code> attribute. 
	 * @param value the Unit - The Parent B2BUnit
	 */
	public void setUnit(final SessionContext ctx, final B2BUnit value)
	{
		UNITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUserGroup.Unit</code> attribute. 
	 * @param value the Unit - The Parent B2BUnit
	 */
	public void setUnit(final B2BUnit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
}
