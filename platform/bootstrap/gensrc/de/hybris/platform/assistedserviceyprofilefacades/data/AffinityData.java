/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedserviceyprofilefacades.data;

import java.io.Serializable;
import java.math.BigDecimal;


import java.util.Objects;
public  class AffinityData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AffinityData.score</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private BigDecimal score;

	/** <i>Generated property</i> for <code>AffinityData.recentViewCount</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private Integer recentViewCount;

	/** <i>Generated property</i> for <code>AffinityData.recentScore</code> property defined at extension <code>assistedserviceyprofilefacades</code>. */
		
	private BigDecimal recentScore;
	
	public AffinityData()
	{
		// default constructor
	}
	
	public void setScore(final BigDecimal score)
	{
		this.score = score;
	}

	public BigDecimal getScore() 
	{
		return score;
	}
	
	public void setRecentViewCount(final Integer recentViewCount)
	{
		this.recentViewCount = recentViewCount;
	}

	public Integer getRecentViewCount() 
	{
		return recentViewCount;
	}
	
	public void setRecentScore(final BigDecimal recentScore)
	{
		this.recentScore = recentScore;
	}

	public BigDecimal getRecentScore() 
	{
		return recentScore;
	}
	

}