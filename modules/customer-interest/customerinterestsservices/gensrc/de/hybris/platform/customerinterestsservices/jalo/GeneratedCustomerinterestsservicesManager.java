/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.jalo;

import de.hybris.platform.customerinterestsservices.constants.CustomerinterestsservicesConstants;
import de.hybris.platform.customerinterestsservices.jalo.ProductInterest;
import de.hybris.platform.customerinterestsservices.jalo.ProductInterestsProcess;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>CustomerinterestsservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCustomerinterestsservicesManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n PRODUCTINTERESTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ProductInterest> CUSTOMER2PRODUCTINTERESTPRODUCTINTERESTSHANDLER = new OneToManyHandler<ProductInterest>(
	CustomerinterestsservicesConstants.TC.PRODUCTINTEREST,
	false,
	"customer",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n PRODUCTINTERESTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ProductInterest> PRODUCT2PRODUCTINTERESTPRODUCTINTERESTSHANDLER = new OneToManyHandler<ProductInterest>(
	CustomerinterestsservicesConstants.TC.PRODUCTINTEREST,
	false,
	"product",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
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
	
	public ProductInterest createProductInterest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomerinterestsservicesConstants.TC.PRODUCTINTEREST );
			return (ProductInterest)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductInterest : "+e.getMessage(), 0 );
		}
	}
	
	public ProductInterest createProductInterest(final Map attributeValues)
	{
		return createProductInterest( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductInterestsProcess createProductInterestsProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( CustomerinterestsservicesConstants.TC.PRODUCTINTERESTSPROCESS );
			return (ProductInterestsProcess)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductInterestsProcess : "+e.getMessage(), 0 );
		}
	}
	
	public ProductInterestsProcess createProductInterestsProcess(final Map attributeValues)
	{
		return createProductInterestsProcess( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return CustomerinterestsservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.productInterests</code> attribute.
	 * @return the productInterests
	 */
	public Collection<ProductInterest> getProductInterests(final SessionContext ctx, final Customer item)
	{
		return CUSTOMER2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.productInterests</code> attribute.
	 * @return the productInterests
	 */
	public Collection<ProductInterest> getProductInterests(final Customer item)
	{
		return getProductInterests( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.productInterests</code> attribute. 
	 * @param value the productInterests
	 */
	public void setProductInterests(final SessionContext ctx, final Customer item, final Collection<ProductInterest> value)
	{
		CUSTOMER2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.productInterests</code> attribute. 
	 * @param value the productInterests
	 */
	public void setProductInterests(final Customer item, final Collection<ProductInterest> value)
	{
		setProductInterests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productInterests. 
	 * @param value the item to add to productInterests
	 */
	public void addToProductInterests(final SessionContext ctx, final Customer item, final ProductInterest value)
	{
		CUSTOMER2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productInterests. 
	 * @param value the item to add to productInterests
	 */
	public void addToProductInterests(final Customer item, final ProductInterest value)
	{
		addToProductInterests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productInterests. 
	 * @param value the item to remove from productInterests
	 */
	public void removeFromProductInterests(final SessionContext ctx, final Customer item, final ProductInterest value)
	{
		CUSTOMER2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productInterests. 
	 * @param value the item to remove from productInterests
	 */
	public void removeFromProductInterests(final Customer item, final ProductInterest value)
	{
		removeFromProductInterests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productInterests</code> attribute.
	 * @return the productInterests
	 */
	public Collection<ProductInterest> getProductInterests(final SessionContext ctx, final Product item)
	{
		return PRODUCT2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productInterests</code> attribute.
	 * @return the productInterests
	 */
	public Collection<ProductInterest> getProductInterests(final Product item)
	{
		return getProductInterests( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productInterests</code> attribute. 
	 * @param value the productInterests
	 */
	public void setProductInterests(final SessionContext ctx, final Product item, final Collection<ProductInterest> value)
	{
		PRODUCT2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productInterests</code> attribute. 
	 * @param value the productInterests
	 */
	public void setProductInterests(final Product item, final Collection<ProductInterest> value)
	{
		setProductInterests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productInterests. 
	 * @param value the item to add to productInterests
	 */
	public void addToProductInterests(final SessionContext ctx, final Product item, final ProductInterest value)
	{
		PRODUCT2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productInterests. 
	 * @param value the item to add to productInterests
	 */
	public void addToProductInterests(final Product item, final ProductInterest value)
	{
		addToProductInterests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productInterests. 
	 * @param value the item to remove from productInterests
	 */
	public void removeFromProductInterests(final SessionContext ctx, final Product item, final ProductInterest value)
	{
		PRODUCT2PRODUCTINTERESTPRODUCTINTERESTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productInterests. 
	 * @param value the item to remove from productInterests
	 */
	public void removeFromProductInterests(final Product item, final ProductInterest value)
	{
		removeFromProductInterests( getSession().getSessionContext(), item, value );
	}
	
}
