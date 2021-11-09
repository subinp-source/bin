/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.customer;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


/**
 * Defines an API to perform various customer related operations
 */
public interface CustomerFacade
{
	/**
	 * Register a user with given parameters
	 *
	 * @param registerData
	 * 		the user data the user will be registered with
	 * @throws IllegalArgumentException
	 * 		if required data is missing
	 * @throws UnknownIdentifierException
	 * 		if the title code is invalid
	 * @throws DuplicateUidException
	 * 		if the login is not unique
	 */
	void register(RegisterData registerData) throws DuplicateUidException, UnknownIdentifierException, IllegalArgumentException;

	/**
	 * Generate dummy customer data with random customerId to return if user already exists in database.
	 *
	 * * @param registerData
	 *          data provided by user during registration
	 * @return
	 */
	CustomerData nextDummyCustomerData(final RegisterData registerData);

	/**
	 * Sends a forgotten password request for the customer specified.
	 * The given <code>id</code> is one of the unique identifiers that is used to recognize the customer in matching strategies.
	 *
	 * @param id
	 * 		the id of the customer to send the forgotten password mail for.
	 * @throws UnknownIdentifierException
	 * 		if the customer cannot be found for the id specified
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	void forgottenPassword(String id);

	/**
	 * Update the password for the user by decrypting and validating the token.
	 *
	 * @param token
	 * 		the token to identify the the customer to reset the password for.
	 * @param newPassword
	 * 		the new password to set
	 * @throws IllegalArgumentException
	 * 		If the new password is empty or the token is invalid or expired
	 * @throws TokenInvalidatedException
	 * 		if the token was already used or there is a newer token
	 */
	void updatePassword(String token, String newPassword) throws TokenInvalidatedException;

	// Methods that operate on the current session user

	/**
	 * Returns the current session user.
	 *
	 * @return current session user data
	 * @throws ConversionException
	 * 		the conversion exception when exception occurred when converting user
	 */
	CustomerData getCurrentCustomer() throws ConversionException;

	/**
	 * Returns the uid of current session user.
	 *
	 * @return current session user uid
	 */
	String getCurrentCustomerUid();

	/**
	 * Change the current customer's UID. The current password is required for 2 reasons, firstly to validate that the
	 * current visitor is actually the customer, secondly the password hash may be salted with the UID and therefore if
	 * the UID is changed then the password needs to be re-hashed.
	 *
	 * @param newUid
	 * 		the new UID for the current customer
	 * @param currentPassword
	 * 		current user password to validate user
	 * @throws PasswordMismatchException
	 * 		thrown if the password is invalid
	 * @throws DuplicateUidException
	 * 		thrown if the newUid is already in use
	 */
	void changeUid(String newUid, String currentPassword) throws DuplicateUidException, PasswordMismatchException;

	/**
	 * Changes current user password. If current session user is anonymous nothing happens.
	 *
	 * @param oldPassword
	 * 		old password to confirm
	 * @param newPassword
	 * 		new password to set
	 * @throws de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException
	 * 		if the given old password does not match the one stored in the system
	 */
	void changePassword(String oldPassword, String newPassword) throws PasswordMismatchException;

	/**
	 * Updates current customer's profile with given parameters
	 *
	 * @param customerData
	 * 		the updated customer data
	 * @throws DuplicateUidException
	 * 		if the login is not unique
	 */
	void updateProfile(CustomerData customerData) throws DuplicateUidException;

	/**
	 * Updates current customer's profile with given parameters
	 *
	 * @param customerData
	 * 		the updated customer data
	 * @throws DuplicateUidException
	 * 		if the login is not unique
	 */
	void updateFullProfile(CustomerData customerData) throws DuplicateUidException;

	/**
	 * updates the session currency and language to the user settings, assigns the cart to the current user and
	 * calculates the cart
	 */
	void loginSuccess();

	/**
	 * publishes LoginSuccessEvent to the event system
	 */
	default void publishLoginSuccessEvent(){
		//provided default implementation for backward compatibility
		//override this.
		return;
	}

	/**
	 * Create a regular customer from a guest customer who has just completed the guest checkout.
	 *
	 * @param pwd
	 * 		the new password entered by the user
	 * @param orderCode
	 * 		the order code
	 * @throws DuplicateUidException
	 * 		if the login is not unique
	 */
	void changeGuestToCustomer(String pwd, String orderCode) throws DuplicateUidException;

	/**
	 * Generates a random guid
	 *
	 * @return a unique random guid
	 */
	String generateGUID();

	/**
	 * Creates a new guest customer for anonymousCheckout and sets the email and name.
	 *
	 * @param email
	 * 		the email address of the anonymous customer
	 * @param name
	 * 		the name of the anonymous customer
	 * @throws DuplicateUidException
	 */
	void createGuestUserForAnonymousCheckout(String email, String name) throws DuplicateUidException;

	/**
	 * Guest customer which is created for anonymous checkout will be the cart user. The session user will remain
	 * anonymous.
	 *
	 * @param guestCustomerData
	 * 		customer data to update the cart with
	 */
	void updateCartWithGuestForAnonymousCheckout(CustomerData guestCustomerData);

	/**
	 * This method will be used by rememberMeServices when there is encoding attributes for language and currency.
	 *
	 * @param languageEncoding
	 * 		enable/disable language encoding
	 * @param currencyEncoding
	 * 		enable/disable currency encoding
	 */
	void rememberMeLoginSuccessWithUrlEncoding(boolean languageEncoding, boolean currencyEncoding);

	/**
	 * Gets the user for UID.
	 *
	 * @param userId
	 * 		the user id
	 * @return the user for UID
	 */
	CustomerData getUserForUID(final String userId);

	/**
	 * Close Account for current session user.
	 *
	 * @return the customer with updated {@link UserModel#DEACTIVATIONDATE} attribute
	 */
	CustomerData closeAccount();

	/**
	 * Sets the new password for the user.
	 * The given <code>userId</code> is one of the unique identifiers that is used to recognize the user in matching strategies.
	 *
	 * @param userId
	 * 		id used to identify the user.
	 * @param newPassword
	 * 		new password for the user
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 * 		if user not found
	 * @throws de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException
	 * 		if encoding not found
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	void setPassword(final String userId, final String newPassword);
}
