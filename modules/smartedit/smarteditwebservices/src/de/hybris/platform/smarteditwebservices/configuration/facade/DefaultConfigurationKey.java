/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.facade;

/**
 * Constants that define keys and UID's for standard components
 */
public enum DefaultConfigurationKey {

    /**
     * The default tooling language used by Smartedit
     */
    DEFAULT_LANGUAGE("defaultToolingLanguage");

    private final String key;

    /**
     * Constructor that allows for the creation with a key/uid.
     * @param key
     */
    DefaultConfigurationKey(String key) {
        this.key = key;
    }

    /**
     * Gets the key value that this instance was intialized with.
     *
     * @return the key value for the enum
     */
    public String getKey() {
        return key;
    }

    /**
     * The uid is an alias for the key.
     *
     * @return the uid value for the enum
     */
    public String getUid() {
        return getKey();
    }
}
