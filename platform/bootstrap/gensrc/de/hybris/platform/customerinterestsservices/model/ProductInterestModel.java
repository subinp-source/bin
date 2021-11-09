/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type ProductInterest first defined at extension customerinterestsservices.
 */
@SuppressWarnings("all")
public class ProductInterestModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductInterest";
	
	/**<i>Generated relation code constant for relation <code>Customer2ProductInterest</code> defining source attribute <code>customer</code> in extension <code>customerinterestsservices</code>.</i>*/
	public static final String _CUSTOMER2PRODUCTINTEREST = "Customer2ProductInterest";
	
	/**<i>Generated relation code constant for relation <code>Product2ProductInterest</code> defining source attribute <code>product</code> in extension <code>customerinterestsservices</code>.</i>*/
	public static final String _PRODUCT2PRODUCTINTEREST = "Product2ProductInterest";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.expiryDate</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String EXPIRYDATE = "expiryDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.baseStore</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String BASESTORE = "baseStore";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.notificationType</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String NOTIFICATIONTYPE = "notificationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.emailEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String EMAILENABLED = "emailEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.smsEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String SMSENABLED = "smsEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.notificationChannels</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String NOTIFICATIONCHANNELS = "notificationChannels";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.language</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.customer</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterest.product</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String PRODUCT = "product";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductInterestModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductInterestModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _baseStore initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _language initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _notificationType initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductInterestModel(final BaseSiteModel _baseSite, final BaseStoreModel _baseStore, final LanguageModel _language, final NotificationType _notificationType)
	{
		super();
		setBaseSite(_baseSite);
		setBaseStore(_baseStore);
		setLanguage(_language);
		setNotificationType(_notificationType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _baseStore initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _language initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _notificationType initial attribute declared by type <code>ProductInterest</code> at extension <code>customerinterestsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductInterestModel(final BaseSiteModel _baseSite, final BaseStoreModel _baseStore, final LanguageModel _language, final NotificationType _notificationType, final ItemModel _owner)
	{
		super();
		setBaseSite(_baseSite);
		setBaseStore(_baseStore);
		setLanguage(_language);
		setNotificationType(_notificationType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the baseSite - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.baseStore</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the baseStore
	 */
	@Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
	public BaseStoreModel getBaseStore()
	{
		return getPersistenceContext().getPropertyValue(BASESTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.customer</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.emailEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the emailEnabled
	 * @deprecated since 6.7
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	@Accessor(qualifier = "emailEnabled", type = Accessor.Type.GETTER)
	public Boolean getEmailEnabled()
	{
		return getPersistenceContext().getPropertyValue(EMAILENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.expiryDate</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "expiryDate", type = Accessor.Type.GETTER)
	public Date getExpiryDate()
	{
		return getPersistenceContext().getPropertyValue(EXPIRYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationChannels</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the notificationChannels
	 */
	@Accessor(qualifier = "notificationChannels", type = Accessor.Type.GETTER)
	public Set<NotificationChannel> getNotificationChannels()
	{
		return getPersistenceContext().getPropertyValue(NOTIFICATIONCHANNELS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.notificationType</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the notificationType
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.GETTER)
	public NotificationType getNotificationType()
	{
		return getPersistenceContext().getPropertyValue(NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.product</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterest.smsEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the smsEnabled
	 * @deprecated since 6.7
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	@Accessor(qualifier = "smsEnabled", type = Accessor.Type.GETTER)
	public Boolean getSmsEnabled()
	{
		return getPersistenceContext().getPropertyValue(SMSENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.baseSite</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the baseSite - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.baseStore</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the baseStore
	 */
	@Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
	public void setBaseStore(final BaseStoreModel value)
	{
		getPersistenceContext().setPropertyValue(BASESTORE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.customer</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the customer
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.emailEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the emailEnabled
	 * @deprecated since 6.7
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	@Accessor(qualifier = "emailEnabled", type = Accessor.Type.SETTER)
	public void setEmailEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(EMAILENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.expiryDate</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the expiryDate - Deprecated since 1905, will be replaced by creation time plus expiryDay.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "expiryDate", type = Accessor.Type.SETTER)
	public void setExpiryDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(EXPIRYDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.notificationChannels</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the notificationChannels
	 */
	@Accessor(qualifier = "notificationChannels", type = Accessor.Type.SETTER)
	public void setNotificationChannels(final Set<NotificationChannel> value)
	{
		getPersistenceContext().setPropertyValue(NOTIFICATIONCHANNELS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.notificationType</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the notificationType
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.SETTER)
	public void setNotificationType(final NotificationType value)
	{
		getPersistenceContext().setPropertyValue(NOTIFICATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.product</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterest.smsEnabled</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the smsEnabled
	 * @deprecated since 6.7
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	@Accessor(qualifier = "smsEnabled", type = Accessor.Type.SETTER)
	public void setSmsEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SMSENABLED, value);
	}
	
}
