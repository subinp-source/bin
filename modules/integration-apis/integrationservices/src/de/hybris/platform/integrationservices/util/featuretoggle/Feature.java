/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util.featuretoggle;

/**
 * Represents a Feature for feature toggling. The intent of this interface
 * is for enumerations to implement it. In a properties file, also define
 * the property and set to true/false.
 *
 * Here's an example of a Feature implementation:
 *
 * <code>
 *     public enum MyFeature implements Feature {
 *         MY_FEATURE("my.feature.enabled")
 *
 *         private final String property;
 *
 *         MyFeature(final String property) {
 *             this.property = property;
 *         }
 *
 *         public String getProperty() {
 *             return property;
 *         }
 *     }
 * </code>
 *
 * In a property file
 *
 * <code>
 *     my.feature.enabled=true
 * </code>
 */
public interface Feature
{
	/**
	 * Gets the property related to the feature enum
	 * @return Feature's property name
	 */
	String getProperty();
}
