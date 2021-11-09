/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.jalo;

import de.hybris.platform.b2b.punchout.constants.B2bpunchoutConstants;
import de.hybris.platform.b2b.punchout.jalo.B2BCustomerPunchOutCredentialMapping;
import de.hybris.platform.b2b.punchout.jalo.PunchOutCredential;
import de.hybris.platform.b2b.punchout.jalo.StoredPunchOutSession;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>B2bpunchoutManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2bpunchoutManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("punchOutOrder", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("unspcs", AttributeMode.INITIAL);
		tmp.put("unitOfMeasure", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public B2BCustomerPunchOutCredentialMapping createB2BCustomerPunchOutCredentialMapping(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2bpunchoutConstants.TC.B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING );
			return (B2BCustomerPunchOutCredentialMapping)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BCustomerPunchOutCredentialMapping : "+e.getMessage(), 0 );
		}
	}
	
	public B2BCustomerPunchOutCredentialMapping createB2BCustomerPunchOutCredentialMapping(final Map attributeValues)
	{
		return createB2BCustomerPunchOutCredentialMapping( getSession().getSessionContext(), attributeValues );
	}
	
	public PunchOutCredential createPunchOutCredential(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2bpunchoutConstants.TC.PUNCHOUTCREDENTIAL );
			return (PunchOutCredential)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PunchOutCredential : "+e.getMessage(), 0 );
		}
	}
	
	public PunchOutCredential createPunchOutCredential(final Map attributeValues)
	{
		return createPunchOutCredential( getSession().getSessionContext(), attributeValues );
	}
	
	public StoredPunchOutSession createStoredPunchOutSession(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( B2bpunchoutConstants.TC.STOREDPUNCHOUTSESSION );
			return (StoredPunchOutSession)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating StoredPunchOutSession : "+e.getMessage(), 0 );
		}
	}
	
	public StoredPunchOutSession createStoredPunchOutSession(final Map attributeValues)
	{
		return createStoredPunchOutSession( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return B2bpunchoutConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.punchOutOrder</code> attribute.
	 * @return the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public Boolean isPunchOutOrder(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, B2bpunchoutConstants.Attributes.AbstractOrder.PUNCHOUTORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.punchOutOrder</code> attribute.
	 * @return the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public Boolean isPunchOutOrder(final AbstractOrder item)
	{
		return isPunchOutOrder( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @return the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public boolean isPunchOutOrderAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isPunchOutOrder( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @return the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public boolean isPunchOutOrderAsPrimitive(final AbstractOrder item)
	{
		return isPunchOutOrderAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @param value the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public void setPunchOutOrder(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, B2bpunchoutConstants.Attributes.AbstractOrder.PUNCHOUTORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @param value the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public void setPunchOutOrder(final AbstractOrder item, final Boolean value)
	{
		setPunchOutOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @param value the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public void setPunchOutOrder(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setPunchOutOrder( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.punchOutOrder</code> attribute. 
	 * @param value the punchOutOrder - Signifies whether the order originates from a punch out process.
	 */
	public void setPunchOutOrder(final AbstractOrder item, final boolean value)
	{
		setPunchOutOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.unitOfMeasure</code> attribute.
	 * @return the unitOfMeasure - UnitOfMeasure describes how the product is packaged or shipped. It must conform to
	 * 						UN/CEFACT Unit of Measure Common Codes. For a list of UN/CEFACT codes, see
	 * 						www.unetrades.net.
	 */
	public String getUnitOfMeasure(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, B2bpunchoutConstants.Attributes.Product.UNITOFMEASURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.unitOfMeasure</code> attribute.
	 * @return the unitOfMeasure - UnitOfMeasure describes how the product is packaged or shipped. It must conform to
	 * 						UN/CEFACT Unit of Measure Common Codes. For a list of UN/CEFACT codes, see
	 * 						www.unetrades.net.
	 */
	public String getUnitOfMeasure(final Product item)
	{
		return getUnitOfMeasure( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.unitOfMeasure</code> attribute. 
	 * @param value the unitOfMeasure - UnitOfMeasure describes how the product is packaged or shipped. It must conform to
	 * 						UN/CEFACT Unit of Measure Common Codes. For a list of UN/CEFACT codes, see
	 * 						www.unetrades.net.
	 */
	public void setUnitOfMeasure(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, B2bpunchoutConstants.Attributes.Product.UNITOFMEASURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.unitOfMeasure</code> attribute. 
	 * @param value the unitOfMeasure - UnitOfMeasure describes how the product is packaged or shipped. It must conform to
	 * 						UN/CEFACT Unit of Measure Common Codes. For a list of UN/CEFACT codes, see
	 * 						www.unetrades.net.
	 */
	public void setUnitOfMeasure(final Product item, final String value)
	{
		setUnitOfMeasure( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.unspcs</code> attribute.
	 * @return the unspcs - Classification specifies the commodity grouping of the line item to the buyer. All the
	 * 						supplier's products and services must be mapped and standardized to the UNSPSC
	 * 						schema. For PunchOut index catalogs, the Classification determines the location of
	 * 						the PunchOut item within catalogs displayed to users. For a list of UNSPSC codes,
	 * 						see www.unspsc.org.
	 */
	public String getUnspcs(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, B2bpunchoutConstants.Attributes.Product.UNSPCS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.unspcs</code> attribute.
	 * @return the unspcs - Classification specifies the commodity grouping of the line item to the buyer. All the
	 * 						supplier's products and services must be mapped and standardized to the UNSPSC
	 * 						schema. For PunchOut index catalogs, the Classification determines the location of
	 * 						the PunchOut item within catalogs displayed to users. For a list of UNSPSC codes,
	 * 						see www.unspsc.org.
	 */
	public String getUnspcs(final Product item)
	{
		return getUnspcs( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.unspcs</code> attribute. 
	 * @param value the unspcs - Classification specifies the commodity grouping of the line item to the buyer. All the
	 * 						supplier's products and services must be mapped and standardized to the UNSPSC
	 * 						schema. For PunchOut index catalogs, the Classification determines the location of
	 * 						the PunchOut item within catalogs displayed to users. For a list of UNSPSC codes,
	 * 						see www.unspsc.org.
	 */
	public void setUnspcs(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, B2bpunchoutConstants.Attributes.Product.UNSPCS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.unspcs</code> attribute. 
	 * @param value the unspcs - Classification specifies the commodity grouping of the line item to the buyer. All the
	 * 						supplier's products and services must be mapped and standardized to the UNSPSC
	 * 						schema. For PunchOut index catalogs, the Classification determines the location of
	 * 						the PunchOut item within catalogs displayed to users. For a list of UNSPSC codes,
	 * 						see www.unspsc.org.
	 */
	public void setUnspcs(final Product item, final String value)
	{
		setUnspcs( getSession().getSessionContext(), item, value );
	}
	
}
