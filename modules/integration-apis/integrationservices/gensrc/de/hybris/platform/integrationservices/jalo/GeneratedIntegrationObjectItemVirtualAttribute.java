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
import de.hybris.platform.integrationservices.jalo.AbstractIntegrationObjectItemAttribute;
import de.hybris.platform.integrationservices.jalo.IntegrationObjectItem;
import de.hybris.platform.integrationservices.jalo.IntegrationObjectVirtualAttributeDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem IntegrationObjectItemVirtualAttribute}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedIntegrationObjectItemVirtualAttribute extends AbstractIntegrationObjectItemAttribute
{
	/** Qualifier of the <code>IntegrationObjectItemVirtualAttribute.retrievalDescriptor</code> attribute **/
	public static final String RETRIEVALDESCRIPTOR = "retrievalDescriptor";
	/** Qualifier of the <code>IntegrationObjectItemVirtualAttribute.integrationObjectItem</code> attribute **/
	public static final String INTEGRATIONOBJECTITEM = "integrationObjectItem";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INTEGRATIONOBJECTITEM's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedIntegrationObjectItemVirtualAttribute> INTEGRATIONOBJECTITEMHANDLER = new BidirectionalOneToManyHandler<GeneratedIntegrationObjectItemVirtualAttribute>(
	IntegrationservicesConstants.TC.INTEGRATIONOBJECTITEMVIRTUALATTRIBUTE,
	false,
	"integrationObjectItem",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractIntegrationObjectItemAttribute.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(RETRIEVALDESCRIPTOR, AttributeMode.INITIAL);
		tmp.put(INTEGRATIONOBJECTITEM, AttributeMode.INITIAL);
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
		INTEGRATIONOBJECTITEMHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectItemVirtualAttribute.integrationObjectItem</code> attribute.
	 * @return the integrationObjectItem
	 */
	public IntegrationObjectItem getIntegrationObjectItem(final SessionContext ctx)
	{
		return (IntegrationObjectItem)getProperty( ctx, INTEGRATIONOBJECTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectItemVirtualAttribute.integrationObjectItem</code> attribute.
	 * @return the integrationObjectItem
	 */
	public IntegrationObjectItem getIntegrationObjectItem()
	{
		return getIntegrationObjectItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectItemVirtualAttribute.integrationObjectItem</code> attribute. 
	 * @param value the integrationObjectItem
	 */
	public void setIntegrationObjectItem(final SessionContext ctx, final IntegrationObjectItem value)
	{
		INTEGRATIONOBJECTITEMHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectItemVirtualAttribute.integrationObjectItem</code> attribute. 
	 * @param value the integrationObjectItem
	 */
	public void setIntegrationObjectItem(final IntegrationObjectItem value)
	{
		setIntegrationObjectItem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectItemVirtualAttribute.retrievalDescriptor</code> attribute.
	 * @return the retrievalDescriptor
	 */
	public IntegrationObjectVirtualAttributeDescriptor getRetrievalDescriptor(final SessionContext ctx)
	{
		return (IntegrationObjectVirtualAttributeDescriptor)getProperty( ctx, RETRIEVALDESCRIPTOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IntegrationObjectItemVirtualAttribute.retrievalDescriptor</code> attribute.
	 * @return the retrievalDescriptor
	 */
	public IntegrationObjectVirtualAttributeDescriptor getRetrievalDescriptor()
	{
		return getRetrievalDescriptor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectItemVirtualAttribute.retrievalDescriptor</code> attribute. 
	 * @param value the retrievalDescriptor
	 */
	public void setRetrievalDescriptor(final SessionContext ctx, final IntegrationObjectVirtualAttributeDescriptor value)
	{
		setProperty(ctx, RETRIEVALDESCRIPTOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IntegrationObjectItemVirtualAttribute.retrievalDescriptor</code> attribute. 
	 * @param value the retrievalDescriptor
	 */
	public void setRetrievalDescriptor(final IntegrationObjectVirtualAttributeDescriptor value)
	{
		setRetrievalDescriptor( getSession().getSessionContext(), value );
	}
	
}
