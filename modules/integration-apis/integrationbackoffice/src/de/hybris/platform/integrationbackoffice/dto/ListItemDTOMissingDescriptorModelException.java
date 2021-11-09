/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.dto;

public class ListItemDTOMissingDescriptorModelException extends RuntimeException
{
	public ListItemDTOMissingDescriptorModelException(String message){
		super(String.format("Missing descriptor model for attribute %s", message));
	}

}
