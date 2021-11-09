/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.jalo;

import de.hybris.platform.entitlementservices.constants.EntitlementservicesConstants;
import de.hybris.platform.entitlementservices.jalo.Entitlement;
import de.hybris.platform.entitlementservices.jalo.ProductEntitlement;
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
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>EntitlementservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEntitlementservicesManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n PRODUCTENTITLEMENTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ProductEntitlement> PRODUCT2PRODUCTENTITLEMENTSRELATIONPRODUCTENTITLEMENTSHANDLER = new OneToManyHandler<ProductEntitlement>(
	EntitlementservicesConstants.TC.PRODUCTENTITLEMENT,
	true,
	"subscriptionProduct",
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
	
	public Entitlement createEntitlement(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( EntitlementservicesConstants.TC.ENTITLEMENT );
			return (Entitlement)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating Entitlement : "+e.getMessage(), 0 );
		}
	}
	
	public Entitlement createEntitlement(final Map attributeValues)
	{
		return createEntitlement( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductEntitlement createProductEntitlement(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( EntitlementservicesConstants.TC.PRODUCTENTITLEMENT );
			return (ProductEntitlement)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductEntitlement : "+e.getMessage(), 0 );
		}
	}
	
	public ProductEntitlement createProductEntitlement(final Map attributeValues)
	{
		return createProductEntitlement( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return EntitlementservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productEntitlements</code> attribute.
	 * @return the productEntitlements
	 */
	public Collection<ProductEntitlement> getProductEntitlements(final SessionContext ctx, final Product item)
	{
		return PRODUCT2PRODUCTENTITLEMENTSRELATIONPRODUCTENTITLEMENTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productEntitlements</code> attribute.
	 * @return the productEntitlements
	 */
	public Collection<ProductEntitlement> getProductEntitlements(final Product item)
	{
		return getProductEntitlements( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productEntitlements</code> attribute. 
	 * @param value the productEntitlements
	 */
	public void setProductEntitlements(final SessionContext ctx, final Product item, final Collection<ProductEntitlement> value)
	{
		PRODUCT2PRODUCTENTITLEMENTSRELATIONPRODUCTENTITLEMENTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productEntitlements</code> attribute. 
	 * @param value the productEntitlements
	 */
	public void setProductEntitlements(final Product item, final Collection<ProductEntitlement> value)
	{
		setProductEntitlements( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productEntitlements. 
	 * @param value the item to add to productEntitlements
	 */
	public void addToProductEntitlements(final SessionContext ctx, final Product item, final ProductEntitlement value)
	{
		PRODUCT2PRODUCTENTITLEMENTSRELATIONPRODUCTENTITLEMENTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productEntitlements. 
	 * @param value the item to add to productEntitlements
	 */
	public void addToProductEntitlements(final Product item, final ProductEntitlement value)
	{
		addToProductEntitlements( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productEntitlements. 
	 * @param value the item to remove from productEntitlements
	 */
	public void removeFromProductEntitlements(final SessionContext ctx, final Product item, final ProductEntitlement value)
	{
		PRODUCT2PRODUCTENTITLEMENTSRELATIONPRODUCTENTITLEMENTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productEntitlements. 
	 * @param value the item to remove from productEntitlements
	 */
	public void removeFromProductEntitlements(final Product item, final ProductEntitlement value)
	{
		removeFromProductEntitlements( getSession().getSessionContext(), item, value );
	}
	
}
