/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.ws;

import java.util.Locale;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class DefaultExtendedInvocationBuilder implements ExtendedInvocationBuilder
{
	private static final String PATCH = "PATCH";

	private final Invocation.Builder builder;

	private DefaultExtendedInvocationBuilder(final Invocation.Builder builder)
	{
		this.builder = builder;
	}

	public static DefaultExtendedInvocationBuilder from(final Invocation.Builder builder)
	{
		return new DefaultExtendedInvocationBuilder(builder);
	}

	@Override
	public Invocation build(final String s)
	{
		return builder.build(s);
	}

	@Override
	public Invocation build(final String s, final Entity<?> entity)
	{
		return builder.build(s, entity);
	}

	@Override
	public Invocation buildGet()
	{
		return builder.buildGet();
	}

	@Override
	public Invocation buildDelete()
	{
		return builder.buildDelete();
	}

	@Override
	public Invocation buildPost(final Entity<?> entity)
	{
		return builder.buildPost(entity);
	}

	@Override
	public Invocation buildPut(final Entity<?> entity)
	{
		return builder.buildPut(entity);
	}

	@Override
	public AsyncInvoker async()
	{
		return builder.async();
	}

	@Override
	public Invocation.Builder accept(final String... strings)
	{
		return builder.accept(strings);
	}

	@Override
	public Invocation.Builder accept(final MediaType... mediaTypes)
	{
		return builder.accept(mediaTypes);
	}

	@Override
	public Invocation.Builder acceptLanguage(final Locale... locales)
	{
		return builder.acceptLanguage(locales);
	}

	@Override
	public Invocation.Builder acceptLanguage(final String... strings)
	{
		return builder.acceptLanguage(strings);
	}

	@Override
	public Invocation.Builder acceptEncoding(final String... strings)
	{
		return builder.acceptEncoding(strings);
	}

	@Override
	public Invocation.Builder cookie(final Cookie cookie)
	{
		return builder.cookie(cookie);
	}

	@Override
	public Invocation.Builder cookie(final String s, final String s1)
	{
		return builder.cookie(s, s1);
	}

	@Override
	public Invocation.Builder cacheControl(final CacheControl cacheControl)
	{
		return builder.cacheControl(cacheControl);
	}

	@Override
	public Invocation.Builder header(final String s, final Object o)
	{
		return builder.header(s, o);
	}

	@Override
	public Invocation.Builder headers(final MultivaluedMap<String, Object> multivaluedMap)
	{
		return builder.headers(multivaluedMap);
	}

	@Override
	public Invocation.Builder property(final String s, final Object o)
	{
		return builder.property(s, o);
	}

	@Override
	public Response get()
	{
		return builder.get();
	}

	@Override
	public <T> T get(final Class<T> aClass)
	{
		return builder.get(aClass);
	}

	@Override
	public <T> T get(final GenericType<T> genericType)
	{
		return builder.get(genericType);
	}

	@Override
	public Response put(final Entity<?> entity)
	{
		return builder.put(entity);
	}

	@Override
	public <T> T put(final Entity<?> entity, final Class<T> aClass)
	{
		return builder.put(entity, aClass);
	}

	@Override
	public <T> T put(final Entity<?> entity, final GenericType<T> genericType)
	{
		return builder.put(entity, genericType);
	}

	@Override
	public Response post(final Entity<?> entity)
	{
		return builder.post(entity);
	}

	@Override
	public <T> T post(final Entity<?> entity, final Class<T> aClass)
	{
		return builder.post(entity, aClass);
	}

	@Override
	public <T> T post(final Entity<?> entity, final GenericType<T> genericType)
	{
		return builder.post(entity, genericType);
	}

	@Override
	public Response patch(final Entity<?> entity)
	{
		return builder.method(PATCH, entity);
	}

	@Override
	public <T> T patch(final Entity<?> entity, final Class<T> aClass)
	{
		return builder.method(PATCH, entity, aClass);
	}

	@Override
	public <T> T patch(final Entity<?> entity, final GenericType<T> genericType)
	{
		return builder.method(PATCH, entity, genericType);
	}

	@Override
	public Response delete()
	{
		return builder.delete();
	}

	@Override
	public <T> T delete(final Class<T> aClass)
	{
		return builder.delete(aClass);
	}

	@Override
	public <T> T delete(final GenericType<T> genericType)
	{
		return builder.delete(genericType);
	}

	@Override
	public Response head()
	{
		return builder.head();
	}

	@Override
	public Response options()
	{
		return builder.options();
	}

	@Override
	public <T> T options(final Class<T> aClass)
	{
		return builder.options(aClass);
	}

	@Override
	public <T> T options(final GenericType<T> genericType)
	{
		return builder.options(genericType);
	}

	@Override
	public Response trace()
	{
		return builder.trace();
	}

	@Override
	public <T> T trace(final Class<T> aClass)
	{
		return builder.trace(aClass);
	}

	@Override
	public <T> T trace(final GenericType<T> genericType)
	{
		return builder.trace(genericType);
	}

	@Override
	public Response method(final String s)
	{
		return builder.method(s);
	}

	@Override
	public <T> T method(final String s, final Class<T> aClass)
	{
		return builder.method(s, aClass);
	}

	@Override
	public <T> T method(final String s, final GenericType<T> genericType)
	{
		return builder.method(s, genericType);
	}

	@Override
	public Response method(final String s, final Entity<?> entity)
	{
		return builder.method(s, entity);
	}

	@Override
	public <T> T method(final String s, final Entity<?> entity, final Class<T> aClass)
	{
		return builder.method(s, entity, aClass);
	}

	@Override
	public <T> T method(final String s, final Entity<?> entity, final GenericType<T> genericType)
	{
		return builder.method(s, entity, genericType);
	}
}
