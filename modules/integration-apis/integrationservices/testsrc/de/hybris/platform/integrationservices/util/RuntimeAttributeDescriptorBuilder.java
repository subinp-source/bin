/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.util;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.ExternalResource;


/**
 * Utility class to build Runtime {@link AttributeDescriptorModel} for testing purposes.
 */
public class RuntimeAttributeDescriptorBuilder extends ExternalResource
{
	private static final String LOCALIZED_TYPE_PREFIX = "localized:";
	private final Set<Key> createdAttributeDescriptors = new HashSet<>();
	private String attributeTypeCode;
	private String enclosingTypeCode;
	private String qualifier;
	private boolean unique;
	private boolean optional;
	private boolean localized;
	private boolean partOf;
	private boolean generate;

	/**
	 * Creates a new instance of {@link RuntimeAttributeDescriptorBuilder}
	 *
	 * @return instance of the builder,
	 */
	public static RuntimeAttributeDescriptorBuilder attributeDescriptor()
	{
		return new RuntimeAttributeDescriptorBuilder();
	}

	public RuntimeAttributeDescriptorBuilder withQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withEnclosingType(final String code)
	{
		enclosingTypeCode = code;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withAttributeType(final String code)
	{
		if (code.startsWith(LOCALIZED_TYPE_PREFIX))
		{
			attributeTypeCode = code.substring(LOCALIZED_TYPE_PREFIX.length());
			return withLocalized(true);
		}
		attributeTypeCode = code;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withUnique(final boolean unique)
	{
		this.unique = unique;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withOptional(final boolean optional)
	{
		this.optional = optional;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withLocalized(final boolean localized)
	{
		this.localized = localized;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withPartOf(final boolean partOf)
	{
		this.partOf = partOf;
		return this;
	}

	public RuntimeAttributeDescriptorBuilder withGenerate(final boolean generate)
	{
		this.generate = generate;
		return this;
	}

	@Override
	protected void before() throws ImpExException
	{
		setup();
	}

	@Override
	protected void after()
	{
		cleanup();
	}

	/**
	 * Setups the defined {@link AttributeDescriptorModel} via Impex.
	 */
	public void setup() throws ImpExException
	{
		attributeTypeCode = localized
				? LOCALIZED_TYPE_PREFIX + attributeTypeCode
				: attributeTypeCode;
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE AttributeDescriptor; qualifier[unique = true]; attributeType(code)     ; enclosingType(code)[unique = true]; partOf       ; unique       ; optional       ; generate       ;  localized",
				"                                 ;" + qualifier + "        ;" + attributeTypeCode + ";" + enclosingTypeCode + "          ;" + partOf + ";" + unique + ";" + optional + ";" + generate + ";" + localized);
		createdAttributeDescriptors.add(new Key(qualifier, enclosingTypeCode));
	}

	/**
	 * Handles clean up for the created {@link AttributeDescriptorModel}.
	 */
	public void cleanup()
	{
		createdAttributeDescriptors.forEach(this::deleteAttributeDescriptor);
		createdAttributeDescriptors.clear();
	}

	private void deleteAttributeDescriptor(final Key key)
	{
		IntegrationTestUtil.remove(AttributeDescriptorModel.class, key::matches);
	}

	private static class Key
	{
		private final String qualifier;
		private final String enclosingTypeCode;

		private Key(final String qualifier, final String enclosingTypeCode)
		{
			this.qualifier = qualifier;
			this.enclosingTypeCode = enclosingTypeCode;
		}

		boolean matches(final AttributeDescriptorModel m)
		{
			final ComposedTypeModel enclosingType = m.getEnclosingType();
			return enclosingType.getCode().equals(enclosingTypeCode) && m.getQualifier().equals(qualifier);
		}
	}
}