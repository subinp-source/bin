/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;


import java.util.Objects;
/**
 * Representation of a Review
 */
@ApiModel(value="Review", description="Representation of a Review")
public  class ReviewWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Identifier of review<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.id</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="id", value="Identifier of review") 	
	private String id;

	/** Review headline<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.headline</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="headline", value="Review headline") 	
	private String headline;

	/** Review comment<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.comment</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="comment", value="Review comment") 	
	private String comment;

	/** Review rating value<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.rating</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="rating", value="Review rating value") 	
	private Double rating;

	/** Date of the review<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.date</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="date", value="Date of the review") 	
	private Date date;

	/** Alias name for the review<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.alias</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="alias", value="Alias name for the review") 	
	private String alias;

	/** Person related to the review<br/><br/><i>Generated property</i> for <code>ReviewWsDTO.principal</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="principal", value="Person related to the review") 	
	private UserWsDTO principal;
	
	public ReviewWsDTO()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setHeadline(final String headline)
	{
		this.headline = headline;
	}

	public String getHeadline() 
	{
		return headline;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	public String getComment() 
	{
		return comment;
	}
	
	public void setRating(final Double rating)
	{
		this.rating = rating;
	}

	public Double getRating() 
	{
		return rating;
	}
	
	public void setDate(final Date date)
	{
		this.date = date;
	}

	public Date getDate() 
	{
		return date;
	}
	
	public void setAlias(final String alias)
	{
		this.alias = alias;
	}

	public String getAlias() 
	{
		return alias;
	}
	
	public void setPrincipal(final UserWsDTO principal)
	{
		this.principal = principal;
	}

	public UserWsDTO getPrincipal() 
	{
		return principal;
	}
	

}