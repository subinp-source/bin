/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.populators;

import com.hybris.yprofile.dto.UID;
import de.hybris.platform.commerceservices.event.ChangeUIDEvent;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class UIDPopulator implements Populator<ChangeUIDEvent, UID> {

    @Override
    public void populate(ChangeUIDEvent event, UID uid) throws ConversionException {
        final String originalUid = event.getOldUid();
        final String newUid = event.getNewUid();
        uid.setNewUid(newUid);
        uid.setOriginalUid(originalUid);
    }
}
