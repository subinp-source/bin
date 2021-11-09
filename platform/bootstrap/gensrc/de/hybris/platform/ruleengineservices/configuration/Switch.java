/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.ruleengineservices.configuration;
 
public enum Switch   
{
	/** <i>Generated enum value</i> for <code>Switch.CONSUMPTION("ruleengineservices.consumption.enabled")</code> value defined at extension <code>ruleengineservices</code>. */
	CONSUMPTION("ruleengineservices.consumption.enabled")  , 
	/** <i>Generated enum value</i> for <code>Switch.GENERATOR_WEBSITEGROUP("promotionengineservices.generator.websitegroup.enabled")</code> value defined at extension <code>promotionengineservices</code>. */
	GENERATOR_WEBSITEGROUP("promotionengineservices.generator.websitegroup.enabled") ; 

    private final String value;

    Switch(final String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static Switch fromValue(final String value)
    {
        for (final Switch ev : values())
        {
            if (ev.getValue().equals(value))
            {
                return ev;
            }
        }

        throw new IllegalArgumentException("Unknown value \"" + value + "\"");
    }

}
