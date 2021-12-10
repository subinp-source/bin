/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 */
package com.hybris.yprofile.jalo;

import com.hybris.yprofile.constants.ProfileservicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>ProfileservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProfileservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("consentReference", AttributeMode.INITIAL);
		tmp.put("cartIdReference", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("consentReference", AttributeMode.INITIAL);
		tmp.put("profileTagDebug", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.cartIdReference</code> attribute.
	 * @return the cartIdReference
	 */
	public String getCartIdReference(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, ProfileservicesConstants.Attributes.AbstractOrder.CARTIDREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.cartIdReference</code> attribute.
	 * @return the cartIdReference
	 */
	public String getCartIdReference(final AbstractOrder item)
	{
		return getCartIdReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.cartIdReference</code> attribute. 
	 * @param value the cartIdReference
	 */
	public void setCartIdReference(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, ProfileservicesConstants.Attributes.AbstractOrder.CARTIDREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.cartIdReference</code> attribute. 
	 * @param value the cartIdReference
	 */
	public void setCartIdReference(final AbstractOrder item, final String value)
	{
		setCartIdReference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.consentReference</code> attribute.
	 * @return the consentReference
	 */
	public String getConsentReference(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, ProfileservicesConstants.Attributes.AbstractOrder.CONSENTREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.consentReference</code> attribute.
	 * @return the consentReference
	 */
	public String getConsentReference(final AbstractOrder item)
	{
		return getConsentReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.consentReference</code> attribute. 
	 * @param value the consentReference
	 */
	public void setConsentReference(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, ProfileservicesConstants.Attributes.AbstractOrder.CONSENTREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.consentReference</code> attribute. 
	 * @param value the consentReference
	 */
	public void setConsentReference(final AbstractOrder item, final String value)
	{
		setConsentReference( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.consentReference</code> attribute.
	 * @return the consentReference
	 */
	public String getConsentReference(final SessionContext ctx, final User item)
	{
		return (String)item.getProperty( ctx, ProfileservicesConstants.Attributes.User.CONSENTREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.consentReference</code> attribute.
	 * @return the consentReference
	 */
	public String getConsentReference(final User item)
	{
		return getConsentReference( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.consentReference</code> attribute. 
	 * @param value the consentReference
	 */
	public void setConsentReference(final SessionContext ctx, final User item, final String value)
	{
		item.setProperty(ctx, ProfileservicesConstants.Attributes.User.CONSENTREFERENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.consentReference</code> attribute. 
	 * @param value the consentReference
	 */
	public void setConsentReference(final User item, final String value)
	{
		setConsentReference( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return ProfileservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.profileTagDebug</code> attribute.
	 * @return the profileTagDebug
	 */
	public Boolean isProfileTagDebug(final SessionContext ctx, final User item)
	{
		return (Boolean)item.getProperty( ctx, ProfileservicesConstants.Attributes.User.PROFILETAGDEBUG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.profileTagDebug</code> attribute.
	 * @return the profileTagDebug
	 */
	public Boolean isProfileTagDebug(final User item)
	{
		return isProfileTagDebug( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.profileTagDebug</code> attribute. 
	 * @return the profileTagDebug
	 */
	public boolean isProfileTagDebugAsPrimitive(final SessionContext ctx, final User item)
	{
		Boolean value = isProfileTagDebug( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.profileTagDebug</code> attribute. 
	 * @return the profileTagDebug
	 */
	public boolean isProfileTagDebugAsPrimitive(final User item)
	{
		return isProfileTagDebugAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.profileTagDebug</code> attribute. 
	 * @param value the profileTagDebug
	 */
	public void setProfileTagDebug(final SessionContext ctx, final User item, final Boolean value)
	{
		item.setProperty(ctx, ProfileservicesConstants.Attributes.User.PROFILETAGDEBUG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.profileTagDebug</code> attribute. 
	 * @param value the profileTagDebug
	 */
	public void setProfileTagDebug(final User item, final Boolean value)
	{
		setProfileTagDebug( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.profileTagDebug</code> attribute. 
	 * @param value the profileTagDebug
	 */
	public void setProfileTagDebug(final SessionContext ctx, final User item, final boolean value)
	{
		setProfileTagDebug( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.profileTagDebug</code> attribute. 
	 * @param value the profileTagDebug
	 */
	public void setProfileTagDebug(final User item, final boolean value)
	{
		setProfileTagDebug( getSession().getSessionContext(), item, value );
	}
	
}
