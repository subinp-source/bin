/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.fraud.data;

import java.io.Serializable;


import java.util.Objects;
public  class FraudSymptomScoringsData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FraudSymptomScoringsData.explanation</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String explanation;

	/** <i>Generated property</i> for <code>FraudSymptomScoringsData.name</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>FraudSymptomScoringsData.score</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private Double score;
	
	public FraudSymptomScoringsData()
	{
		// default constructor
	}
	
	public void setExplanation(final String explanation)
	{
		this.explanation = explanation;
	}

	public String getExplanation() 
	{
		return explanation;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setScore(final Double score)
	{
		this.score = score;
	}

	public Double getScore() 
	{
		return score;
	}
	

}