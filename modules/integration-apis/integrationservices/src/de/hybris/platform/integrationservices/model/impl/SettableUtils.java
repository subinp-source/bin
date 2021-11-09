/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.apache.commons.lang.BooleanUtils;

class SettableUtils {

    private SettableUtils() {
        throw new IllegalStateException("Utility class");
    }

    static boolean isInitial(final AttributeDescriptorModel descriptor) {
        return BooleanUtils.isTrue(descriptor.getInitial());
    }

    static boolean isWritable(final AttributeDescriptorModel descriptor) {
        return BooleanUtils.isTrue(descriptor.getWritable());
    }

    static boolean isReadOnly(final AttributeDescriptorModel descriptor) {
        return !isInitial(descriptor) && !isWritable(descriptor);
    }
}
