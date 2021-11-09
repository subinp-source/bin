/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;

import java.io.DataInputStream;
import java.util.List;


/**
 * Handles creation and sending of emails.
 */
public interface EmailService
{
	/**
	 * 
	 * Returns a {@link EmailAddressModel}. Instance is found for the given email address and display name criteria. If
	 * there is no instance for this condition the new {@link EmailAddressModel} instance is returned based on the given
	 * criteria.
	 * 
	 * @param emailAddress
	 *           Email id
	 * @param displayName
	 *           Display name
	 * @return EmailAddress object
	 */
	EmailAddressModel getOrCreateEmailAddressForEmail(String emailAddress, String displayName);

	/**
	 * Creates an email attachment from an input stream.
	 * 
	 * @param masterDataStream
	 *           Input data stream
	 * @param filename
	 *           File name
	 * @param mimeType
	 *           Mime type
	 * @return EmailAttachment object
	 */
	EmailAttachmentModel createEmailAttachment(DataInputStream masterDataStream, String filename, String mimeType);

	/**
	 * Creates and EmailMessage object.
	 * 
	 * @param toAddresses
	 *           List of To email addresses
	 * @param ccAddresses
	 *           List of CC email addresses
	 * @param bccAddresses
	 *           List of BCC email addresses
	 * @param fromAddress
	 *           From email address
	 * @param replyToAddress
	 *           Reply To email address
	 * @param subject
	 *           Subject of the email
	 * @param body
	 *           Contents of the email
	 * @param attachments
	 *           List of email attachments
	 * @return EmailMessage object
	 */
	EmailMessageModel createEmailMessage(List<EmailAddressModel> toAddresses, List<EmailAddressModel> ccAddresses, // NOSONAR
			List<EmailAddressModel> bccAddresses, EmailAddressModel fromAddress, String replyToAddress, String subject, String body,
			List<EmailAttachmentModel> attachments);

	/**
	 * Sends an email
	 * 
	 * @param message
	 *           EmailMessage
	 * @return true if successful
	 */
	boolean send(EmailMessageModel message);
}
