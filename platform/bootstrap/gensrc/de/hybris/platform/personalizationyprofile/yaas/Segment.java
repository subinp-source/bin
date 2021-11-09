/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 */

package de.hybris.platform.personalizationyprofile.yaas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;



public class Segment extends LinkedHashMap<String, Object> implements Map<String, Object> {

	public static class SegmentBuilder extends LinkedHashMap<String, Object> {

		public SegmentBuilder withName(final String name) {
			this.put("name", name);
			return this;
		}

		public SegmentBuilder withDescription(final String description) {
			this.put("description", description);
			return this;
		}
		public Segment build() {
			final Segment dto = new Segment();
			dto.putAll(this);
			return dto;
		}
	}

	/** Default serialVersionUID value. */
 	private static final long serialVersionUID = 1L;

    @JsonCreator
	public Segment(@JsonProperty("name") final String name, @JsonProperty("description") final String description)
	{
		this.put("name", name);
		this.put("description", description);
	}

	public Segment()
	{
		// default constructor
	}


	/** <i>Generated property</i> for <code>Segment.name</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setName(final String name)
	{
		this.put("name", name);
	}

	public String getName()
	{
	 	return (String)this.get("name");
	}

	/** <i>Generated property</i> for <code>Segment.description</code> property defined at extension <code>personalizationyprofile</code>. */
	public void setDescription(final String description)
	{
		this.put("description", description);
	}

	public String getDescription()
	{
	 	return (String)this.get("description");
	}

}
