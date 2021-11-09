/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.ws;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public interface ExtendedInvocationBuilder extends Invocation.Builder
{
	Response patch(Entity<?> entity);
	
	<T> T patch(Entity<?> entity, Class<T> aClass);

	<T> T patch(Entity<?> entity, GenericType<T> genericType);
}
