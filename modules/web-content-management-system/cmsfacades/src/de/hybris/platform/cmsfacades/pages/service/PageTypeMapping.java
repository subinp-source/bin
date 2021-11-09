/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service;

import de.hybris.platform.cmsfacades.data.AbstractPageData;


/**
 * Represents meta-information about a <code>ComposedTypeModel</code> and the type code of the target data model to be
 * converted to. The type data is the type code representation of an object that extends a
 * <code>AbstractPageData</code>. <br>
 * <br>
 * For example, a ContentPageModel will be converted to a ContentPageData, therefore the typecode will be 'ContentPage'
 * and the typedata will be 'ContentPageData.class'.
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public interface PageTypeMapping
{
	/**
	 * Get the typecode identifying the <code>ComposedTypeModel</code>.
	 *
	 * @return the typecode
	 */
	String getTypecode();

	/**
	 * Set the typecode identifying the <code>ComposedTypeModel</code>.
	 *
	 * @param typecode
	 *           - the typecode
	 */
	void setTypecode(String typecode);

	/**
	 * Get the class of the data model which the <code>ComposedTypeModel</code> will be converted to.
	 *
	 * @return the typecode of the data model
	 */
	Class<? extends AbstractPageData> getTypedata();

	/**
	 * Set the class of the data model which the <code>ComposedTypeModel</code> will be converted to.
	 *
	 * @param typedata
	 *           - the typecode of the data model
	 */
	void setTypedata(Class<? extends AbstractPageData> typedata);

}
