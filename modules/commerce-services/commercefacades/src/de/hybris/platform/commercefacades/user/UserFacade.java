/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.user;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.TitleData;

import java.util.List;


/**
 * User facade interface. Deals with methods related to user operations - registering, logging in and other.
 */
public interface UserFacade
{

	/**
	 * Provide all localized titles.
	 *
	 * @return List of {@link TitleData} objects
	 */
	List<TitleData> getTitles();

	/**
	 * Test if the address book is empty.
	 *
	 * @return true if the customer has no addresses
	 * @deprecated since 6.5, instead check if {@link #getAddressBook()} is empty directly
	 */
	@Deprecated(since = "6.5", forRemoval = true)
	boolean isAddressBookEmpty();

	/**
	 * Get the list of delivery addresses.
	 *
	 * @return the delivery addresses
	 */
	List<AddressData> getAddressBook();

	/**
	 * Adds the address for the current user
	 *
	 * @param addressData
	 *           the address to add
	 */
	void addAddress(AddressData addressData);

	/**
	 * Removes the address for the current user
	 *
	 * @param addressData
	 *           the address to remove
	 */
	void removeAddress(AddressData addressData);

	/**
	 * Updates the address for the current user
	 *
	 * @param addressData
	 *           the address to update
	 */
	void editAddress(AddressData addressData);

	/**
	 * Returns the default address
	 *
	 * @return the address
	 */
	AddressData getDefaultAddress();

	/**
	 * Sets the default address
	 *
	 * @param addressData
	 *           the address to make default
	 */
	void setDefaultAddress(AddressData addressData);

	/**
	 * Returns the address with matching code for the current user
	 *
	 * @param code
	 *           the address code
	 * @return the address
	 */
	AddressData getAddressForCode(String code);

	/**
	 * Test if the address id matches with the default address id
	 *
	 * @param addressId
	 *           the address Id
	 * @return true if address id is the default address id
	 */
	boolean isDefaultAddress(String addressId);

	/**
	 * Returns the current user's Credit Card Payment Infos
	 *
	 * @param saved
	 *           <code>true</code> to retrieve only saved credit card payment infos
	 * @return list of Credit Card Payment Info Data
	 */
	List<CCPaymentInfoData> getCCPaymentInfos(boolean saved);

	/**
	 * Returns the current user's credit card payment info given it's code
	 *
	 * @param code
	 *           the code
	 * @return the Credit Card Payment Info Data
	 */
	CCPaymentInfoData getCCPaymentInfoForCode(String code);


	/**
	 * Updates current users' payment info
	 *
	 * @param paymentInfo
	 *           - new payment info data
	 */
	void updateCCPaymentInfo(CCPaymentInfoData paymentInfo);


	/**
	 * Removes credit card payment info by id
	 *
	 * @param id
	 *           the id
	 */
	void removeCCPaymentInfo(String id);

	/**
	 * Unlink the credit card info from the customer by CC id
	 *
	 * @param id
	 *           the id
	 * @deprecated since 6.7. Use {@link UserFacade#removeCCPaymentInfo(String)} instead
	 */
	@Deprecated(since = "6.7", forRemoval = true)
	void unlinkCCPaymentInfo(String id);


	/**
	 * Sets the default Payment Info
	 *
	 * @param paymentInfo
	 *           the paymentInfo to make default
	 */
	void setDefaultPaymentInfo(CCPaymentInfoData paymentInfo);

	/**
	 * Sets users preferred language to the current session language
	 */
	void syncSessionLanguage();

	/**
	 * Sets users preferred currency to the current session currency
	 */
	void syncSessionCurrency();

	/**
	 * Test if the current user is anonymous
	 *
	 * @return <code>true</code> if the current user is anonymous
	 */
	boolean isAnonymousUser();

	/**
	 * Verifies whether the user exists. The given <code>id</code> is used to find the user in matching strategies that
	 * use one of the unique identifiers to recognize the user.
	 *
	 * @param id
	 *           - ID used to find the user
	 * @return <code>true</code> if the user exists
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	boolean isUserExisting(String id);

	/**
	 * Returns the UID of the found user. The user is searched by a matching strategy that uses one of the unique
	 * identifiers to recognize the user.
	 *
	 * @param id
	 *           one of the unique identifiers used to identify a user
	 * @return UID of the found user
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if user doesn't exist
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	String getUserUID(String id);

	/**
	 * Sets current user explicitly. The given <code>id</code> is used to find the user in matching strategies that use
	 * one of the unique identifiers to recognize the user.
	 *
	 * @param id
	 *           - id used to find the user
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if user doesn't exist
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	void setCurrentUser(final String id);
}
