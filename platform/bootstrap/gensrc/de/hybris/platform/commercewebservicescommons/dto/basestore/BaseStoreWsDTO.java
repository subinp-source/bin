/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.basestore;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Base Store
 */
@ApiModel(value="BaseStore", description="Representation of a Base Store")
public  class BaseStoreWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Base store name<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Base store name") 	
	private String name;

	/** Flag defining is external tax is enabled<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.externalTaxEnabled</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="externalTaxEnabled", value="Flag defining is external tax is enabled") 	
	private Boolean externalTaxEnabled;

	/** Payment provider<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.paymentProvider</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="paymentProvider", value="Payment provider") 	
	private String paymentProvider;

	/** Create return process code<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.createReturnProcessCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="createReturnProcessCode", value="Create return process code") 	
	private String createReturnProcessCode;

	/** Maximum radius for searching point of service<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.maxRadiusForPosSearch</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="maxRadiusForPosSearch", value="Maximum radius for searching point of service") 	
	private Double maxRadiusForPosSearch;

	/** Submit order process code<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.submitOrderProcessCode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="submitOrderProcessCode", value="Submit order process code") 	
	private String submitOrderProcessCode;

	/** List of currencies<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.currencies</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="currencies", value="List of currencies") 	
	private List<CurrencyWsDTO> currencies;

	/** Default currency<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.defaultCurrency</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultCurrency", value="Default currency") 	
	private CurrencyWsDTO defaultCurrency;

	/** Point of service being default delivery origin<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.defaultDeliveryOrigin</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultDeliveryOrigin", value="Point of service being default delivery origin") 	
	private PointOfServiceWsDTO defaultDeliveryOrigin;

	/** Default language<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.defaultLanguage</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultLanguage", value="Default language") 	
	private LanguageWsDTO defaultLanguage;

	/** List of delivery countries<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.deliveryCountries</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="deliveryCountries", value="List of delivery countries") 	
	private List<CountryWsDTO> deliveryCountries;

	/** List of delivery modes<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.deliveryModes</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="deliveryModes", value="List of delivery modes") 	
	private DeliveryModeListWsDTO deliveryModes;

	/** List of languages<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.languages</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="languages", value="List of languages") 	
	private List<LanguageWsDTO> languages;

	/** List of points of service<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.pointsOfService</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pointsOfService", value="List of points of service") 	
	private List<PointOfServiceWsDTO> pointsOfService;

	/** Flag specifying whether the express checkout option is enabled<br/><br/><i>Generated property</i> for <code>BaseStoreWsDTO.expressCheckoutEnabled</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="expressCheckoutEnabled", value="Flag specifying whether the express checkout option is enabled") 	
	private Boolean expressCheckoutEnabled;
	
	public BaseStoreWsDTO()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setExternalTaxEnabled(final Boolean externalTaxEnabled)
	{
		this.externalTaxEnabled = externalTaxEnabled;
	}

	public Boolean getExternalTaxEnabled() 
	{
		return externalTaxEnabled;
	}
	
	public void setPaymentProvider(final String paymentProvider)
	{
		this.paymentProvider = paymentProvider;
	}

	public String getPaymentProvider() 
	{
		return paymentProvider;
	}
	
	public void setCreateReturnProcessCode(final String createReturnProcessCode)
	{
		this.createReturnProcessCode = createReturnProcessCode;
	}

	public String getCreateReturnProcessCode() 
	{
		return createReturnProcessCode;
	}
	
	public void setMaxRadiusForPosSearch(final Double maxRadiusForPosSearch)
	{
		this.maxRadiusForPosSearch = maxRadiusForPosSearch;
	}

	public Double getMaxRadiusForPosSearch() 
	{
		return maxRadiusForPosSearch;
	}
	
	public void setSubmitOrderProcessCode(final String submitOrderProcessCode)
	{
		this.submitOrderProcessCode = submitOrderProcessCode;
	}

	public String getSubmitOrderProcessCode() 
	{
		return submitOrderProcessCode;
	}
	
	public void setCurrencies(final List<CurrencyWsDTO> currencies)
	{
		this.currencies = currencies;
	}

	public List<CurrencyWsDTO> getCurrencies() 
	{
		return currencies;
	}
	
	public void setDefaultCurrency(final CurrencyWsDTO defaultCurrency)
	{
		this.defaultCurrency = defaultCurrency;
	}

	public CurrencyWsDTO getDefaultCurrency() 
	{
		return defaultCurrency;
	}
	
	public void setDefaultDeliveryOrigin(final PointOfServiceWsDTO defaultDeliveryOrigin)
	{
		this.defaultDeliveryOrigin = defaultDeliveryOrigin;
	}

	public PointOfServiceWsDTO getDefaultDeliveryOrigin() 
	{
		return defaultDeliveryOrigin;
	}
	
	public void setDefaultLanguage(final LanguageWsDTO defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
	}

	public LanguageWsDTO getDefaultLanguage() 
	{
		return defaultLanguage;
	}
	
	public void setDeliveryCountries(final List<CountryWsDTO> deliveryCountries)
	{
		this.deliveryCountries = deliveryCountries;
	}

	public List<CountryWsDTO> getDeliveryCountries() 
	{
		return deliveryCountries;
	}
	
	public void setDeliveryModes(final DeliveryModeListWsDTO deliveryModes)
	{
		this.deliveryModes = deliveryModes;
	}

	public DeliveryModeListWsDTO getDeliveryModes() 
	{
		return deliveryModes;
	}
	
	public void setLanguages(final List<LanguageWsDTO> languages)
	{
		this.languages = languages;
	}

	public List<LanguageWsDTO> getLanguages() 
	{
		return languages;
	}
	
	public void setPointsOfService(final List<PointOfServiceWsDTO> pointsOfService)
	{
		this.pointsOfService = pointsOfService;
	}

	public List<PointOfServiceWsDTO> getPointsOfService() 
	{
		return pointsOfService;
	}
	
	public void setExpressCheckoutEnabled(final Boolean expressCheckoutEnabled)
	{
		this.expressCheckoutEnabled = expressCheckoutEnabled;
	}

	public Boolean getExpressCheckoutEnabled() 
	{
		return expressCheckoutEnabled;
	}
	

}