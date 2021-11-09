/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.sitemap.generator;

import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;

import java.io.File;
import java.io.IOException;
import java.util.List;


public interface SiteMapGenerator<T>
{

	File render(final CMSSiteModel site, final CurrencyModel currencyModel, final LanguageModel languageModel,
			final RendererTemplateModel rendererTemplateModel, final List<T> models, final String filePrefix, final Integer index)
			throws IOException;

	List<T> getData(final CMSSiteModel site);

	SiteMapPageEnum getSiteMapPageEnum();

}
