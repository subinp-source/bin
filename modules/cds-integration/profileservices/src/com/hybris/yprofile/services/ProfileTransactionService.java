/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services;

import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

/**
 * ProfileTransactionService Interface. Communication service to send transactions to Profile
 */
public interface ProfileTransactionService {

    /**
     * Sends order to Profile.
     * @param orderModel the order model
     */
    void sendSubmitOrderEvent(final OrderModel orderModel);

    /**
     * Sends consignment to Profile.
     * @param consignmentModel the consignment model
     */
    void sendConsignmentEvent(final ConsignmentModel consignmentModel);

    /**
     * Sends return order to Profile.
     * @param returnRequestModel the return model
     */
    void sendReturnOrderEvent(final ReturnRequestModel returnRequestModel);

    /**
     * Sends user registration event to Profile.
     * @param userModel the user model
     * @param consentReferenceId consent refrence
     * @param sessionId ec session id
     * @param storeName storefront name like 'electronics'
     */
    void sendUserRegistrationEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName);

    /**
     * Sends newUid updated event to Profile.
     * @param event the ChangeUIDEvent
     * @param consentReferenceId the consent reference
     */
    void sendUidChangedEvent(final ChangeUIDEvent event, final String consentReferenceId);

    /**
     * Sends address deleted event to Profile.
     * @param userModel the user model
     * @param consentReferenceId the consent reference
     * @param baseSiteId site id from the event
     */
    void sendAddressDeletedEvent(final UserModel userModel, final String baseSiteId, final String consentReferenceId);

    /**
     * Sends address saved event to Profile.
     * @param userModel the user model
     * @param consentReferenceId the consent reference
     * @param baseSiteId site id from the event
     */
    void sendAddressSavedEvent(final UserModel userModel, String baseSiteId, final String consentReferenceId);

    /**
     * Sends personal details updated event to Profile.
     * @param userModel the user model
     * @param consentReferenceId the consent reference
     * @param baseSiteId site id from the event
     */
    void sendPersonalDetailsChangedEvent(UserModel userModel, String baseSiteId, String consentReferenceId);

    /**
     * Sends user login event to Profile.
     * @param userModel the user model
     * @param consentReferenceId the consent reference
     * @param sessionId ec session id
     * @param storeName storefront name like 'electronics'
     */
    void sendLoginEvent(final UserModel userModel, final String consentReferenceId, final String sessionId, final String storeName);
}
