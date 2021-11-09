/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cartfileupload;

import java.io.InputStream;

/**
 * This interface supports asynchronous processing of file upload to create a SavedCart.
 */
public interface SavedCartFileUploadFacade
{
	/**
	 * Creates SavedCart from the file uploaded in asynchronous fashion. This will create a @
	 * {@link de.hybris.platform.acceleratorservices.cartfileupload.events.SavedCartFileUploadEvent} event which is
	 * captured by event listener @
	 * {@link de.hybris.platform.acceleratorservices.cartfileupload.events.SavedCartFileUploadEventListener} to trigger a
	 * business process.
	 * 
	 * @param fileStream
	 *           Input stream of the file
	 * @param fileName
	 *           File Name
	 * @param fileFormat
	 *           File Format
	 */
	void createCartFromFileUpload(InputStream fileStream, String fileName, String fileFormat);
}
