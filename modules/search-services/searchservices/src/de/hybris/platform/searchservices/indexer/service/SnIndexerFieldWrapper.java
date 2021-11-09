/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.service.SnQualifier;

import java.util.List;
import java.util.Map;


/**
 * Wrapper for fields used during the indexing process.
 */
public interface SnIndexerFieldWrapper
{
	/**
	 * Returns the field id.
	 *
	 * @return the field id
	 */
	String getFieldId();

	/**
	 * Returns the field.
	 *
	 * @return the field
	 */
	SnField getField();

	/**
	 * Returns the value provider id.
	 *
	 * @return the value provider
	 */
	String getValueProviderId();

	/**
	 * Returns the value provider parameters.
	 *
	 * @return the value provider parameters
	 */
	Map<String, String> getValueProviderParameters();

	/**
	 * Returns whether the field is localized.
	 *
	 * @return <code>true</code> if the field is localized, <code>false</code> otherwise.
	 */
	boolean isLocalized();

	/**
	 * Returns whether the field is qualified.
	 *
	 * @return <code>true</code> if the field is qualified, <code>false</code> otherwise.
	 */
	boolean isQualified();

	/**
	 * Returns whether the field is multi-valued.
	 *
	 * @return <code>true</code> if the field is multi-valued, <code>false</code> otherwise.
	 */
	boolean isMultiValued();

	/**
	 * Returns the qualifiers.
	 *
	 * @return the qualifiers.
	 */
	List<SnQualifier> getQualifiers();
}
