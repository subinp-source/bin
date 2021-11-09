/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.security;

public enum SecurityError {

    AUTHENTICATION_ERROR_BAD_CREDENTIALS("authentication.error.badcredentials");

    private final String code;

    SecurityError(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
