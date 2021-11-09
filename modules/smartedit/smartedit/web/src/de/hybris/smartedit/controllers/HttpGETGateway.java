/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import org.springframework.messaging.handler.annotation.Header;


/**
 * Gateway to relay the GET operation to the secured webservice responsible of executing the operation. By default,
 * {@code smarteditwebservices} is the targeted extension.
 */
public interface HttpGETGateway
{

	public String loadAll(String payload, @Header("Authorization") String token);

}
