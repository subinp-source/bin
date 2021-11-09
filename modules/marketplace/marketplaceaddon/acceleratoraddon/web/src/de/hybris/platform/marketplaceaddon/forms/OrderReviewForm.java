/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceaddon.forms;

import java.util.ArrayList;
import java.util.List;


public class OrderReviewForm
{

	private List<ProductReviewForm> productReviewForms;
	private Double satisfaction;
	private Double delivery;
	private Double communication;
	private String comment;

	public OrderReviewForm()
	{
		productReviewForms = new ArrayList<>();
	}

	public List<ProductReviewForm> getProductReviewForms()
	{
		return productReviewForms;
	}

	public void setProductReviewForms(final List<ProductReviewForm> productReviewForms)
	{
		this.productReviewForms = productReviewForms;
	}

	public Double getSatisfaction()
	{
		return satisfaction;
	}

	public void setSatisfaction(final Double satisfaction)
	{
		this.satisfaction = satisfaction;
	}

	public Double getDelivery()
	{
		return delivery;
	}

	public void setDelivery(final Double delivery)
	{
		this.delivery = delivery;
	}

	public Double getCommunication()
	{
		return communication;
	}

	public void setCommunication(final Double communication)
	{
		this.communication = communication;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(final String comment)
	{
		this.comment = comment;
	}


}
