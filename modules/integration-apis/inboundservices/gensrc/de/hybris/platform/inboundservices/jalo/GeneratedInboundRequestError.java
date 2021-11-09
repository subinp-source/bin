/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.jalo;

import de.hybris.platform.inboundservices.constants.InboundservicesConstants;
import de.hybris.platform.inboundservices.jalo.InboundRequest;
import de.hybris.platform.integrationservices.jalo.MonitoredRequestError;
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
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem InboundRequestError}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedInboundRequestError extends MonitoredRequestError
{
	/** Qualifier of the <code>InboundRequestError.inboundRequest</code> attribute **/
	public static final String INBOUNDREQUEST = "inboundRequest";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INBOUNDREQUEST's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedInboundRequestError> INBOUNDREQUESTHANDLER = new BidirectionalOneToManyHandler<GeneratedInboundRequestError>(
	InboundservicesConstants.TC.INBOUNDREQUESTERROR,
	false,
	"inboundRequest",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(MonitoredRequestError.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(INBOUNDREQUEST, AttributeMode.INITIAL);
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
		INBOUNDREQUESTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundRequestError.inboundRequest</code> attribute.
	 * @return the inboundRequest
	 */
	public InboundRequest getInboundRequest(final SessionContext ctx)
	{
		return (InboundRequest)getProperty( ctx, INBOUNDREQUEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundRequestError.inboundRequest</code> attribute.
	 * @return the inboundRequest
	 */
	public InboundRequest getInboundRequest()
	{
		return getInboundRequest( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundRequestError.inboundRequest</code> attribute. 
	 * @param value the inboundRequest
	 */
	public void setInboundRequest(final SessionContext ctx, final InboundRequest value)
	{
		INBOUNDREQUESTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundRequestError.inboundRequest</code> attribute. 
	 * @param value the inboundRequest
	 */
	public void setInboundRequest(final InboundRequest value)
	{
		setInboundRequest( getSession().getSessionContext(), value );
	}
	
}
