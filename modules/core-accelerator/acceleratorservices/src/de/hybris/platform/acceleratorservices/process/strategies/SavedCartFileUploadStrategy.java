/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.process.strategies;


import de.hybris.platform.acceleratorservices.cartfileupload.data.SavedCartFileUploadReportData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;

import java.io.IOException;

public interface SavedCartFileUploadStrategy
{
    SavedCartFileUploadReportData createSavedCartFromFile(MediaModel mediaModel, CartModel cartModel) throws IOException;
}
