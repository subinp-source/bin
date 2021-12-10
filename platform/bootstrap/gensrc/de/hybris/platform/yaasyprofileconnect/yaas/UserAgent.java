/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 11-Dec-2021, 12:32:59 AM
* ----------------------------------------------------------------
*
* [y] hybris Platform
*
* Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*/

package de.hybris.platform.yaasyprofileconnect.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;



public class UserAgent extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class UserAgentBuilder extends LinkedHashMap<String, Object> {

		public UserAgentBuilder withDeviceType(final String deviceType) {
			this.put("deviceType", deviceType);
			return this;
		}

		public UserAgentBuilder withOperatingSystemNoVersion(final String operatingSystemNoVersion) {
			this.put("operatingSystemNoVersion", operatingSystemNoVersion);
			return this;
		}

		public UserAgentBuilder withBrowserNoVersion(final String browserNoVersion) {
			this.put("browserNoVersion", browserNoVersion);
			return this;
		}
		public UserAgent build() {
			final UserAgent dto = new UserAgent();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public UserAgent(@JsonProperty("deviceType") final String deviceType, @JsonProperty("operatingSystemNoVersion") final String operatingSystemNoVersion, @JsonProperty("browserNoVersion") final String browserNoVersion)
	{
		this.put("deviceType", deviceType);
		this.put("operatingSystemNoVersion", operatingSystemNoVersion);
		this.put("browserNoVersion", browserNoVersion);
	}

	public UserAgent()
	{
		// default constructor
	}

	/** <i>Generated property</i> for <code>UserAgent.deviceType</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setDeviceType(final String deviceType)
	{
		this.put("deviceType", deviceType);
	}

	public String getDeviceType()
	{
	 	return (String)this.get("deviceType");
	}

	/** <i>Generated property</i> for <code>UserAgent.operatingSystemNoVersion</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setOperatingSystemNoVersion(final String operatingSystemNoVersion)
	{
		this.put("operatingSystemNoVersion", operatingSystemNoVersion);
	}

	public String getOperatingSystemNoVersion()
	{
	 	return (String)this.get("operatingSystemNoVersion");
	}

	/** <i>Generated property</i> for <code>UserAgent.browserNoVersion</code> property defined at extension <code>yaasyprofileconnect</code>. */
	public void setBrowserNoVersion(final String browserNoVersion)
	{
		this.put("browserNoVersion", browserNoVersion);
	}

	public String getBrowserNoVersion()
	{
	 	return (String)this.get("browserNoVersion");
	}

}
