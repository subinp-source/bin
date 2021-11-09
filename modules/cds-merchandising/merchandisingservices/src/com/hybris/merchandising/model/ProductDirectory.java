/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;


/**
 * POJO to represent ProductDirectory which will be stored at Conversion persistence layer
 */
public class ProductDirectory
{
	private String id;
	private String name;
	private String rollupStrategy;
	private String defaultLanguage;
	private List<String> sites;

	/**
	 * Method which takes {@link MerchProductDirectoryConfigModel} and returns {@link ProductDirectory} which we store in CDS
	 *
	 * @param merchProductDirectoryConfigModel
	 * @return ProductDirectory
	 */
	public static ProductDirectory fromMerchProductDirectoryConfigModel(
			final MerchProductDirectoryConfigModel merchProductDirectoryConfigModel)
	{
		final ProductDirectoryBuilder builder = new ProductDirectoryBuilder(merchProductDirectoryConfigModel.getDisplayName(),
				merchProductDirectoryConfigModel.getRollUpStrategy(),
				merchProductDirectoryConfigModel.getBaseSites().stream().map(BaseSiteModel::getUid).collect(Collectors.toList()),
				merchProductDirectoryConfigModel.getDefaultLanguage().getIsocode());
		Optional.ofNullable(merchProductDirectoryConfigModel.getCdsIdentifier())
				.ifPresent(builder::withId);
		return builder.build();
	}


	/**
	 * Builder class
	 */
	public static class ProductDirectoryBuilder
	{
		private String id;
		private final String productDirectoryName;
		private final String rollupStrategy;
		private final List<String> baseSites;
		private final String defaultLanguage;

		public ProductDirectoryBuilder(final String productDirectoryName, final String rollUpStrategy,
				final List<String> baseSites,
				final String defaultLanguage)
		{
			this.productDirectoryName = productDirectoryName;
			this.rollupStrategy = rollUpStrategy;
			this.baseSites = baseSites;
			this.defaultLanguage = defaultLanguage;
		}

		public ProductDirectoryBuilder withId(final String id)
		{
			this.id = id;
			return this;
		}

		public ProductDirectory build()
		{
			final ProductDirectory productDirectory = new ProductDirectory();
			productDirectory.setId(id);
			productDirectory.setSites(baseSites);
			productDirectory.setName(productDirectoryName);
			productDirectory.setRollupStrategy(rollupStrategy);
			productDirectory.setDefaultLanguage(defaultLanguage);
			return productDirectory;
		}
	}

	/**
	 *
	 * @return unique identifier
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * Set unique identifier
	 *
	 * @param id
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 *
	 * @return returns product directory name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets name
	 *
	 * @param name
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 *
	 * @return rollup strategy for variant roll up
	 */
	public String getRollupStrategy()
	{
		return rollupStrategy;
	}

	/**
	 * Sets roll up strategy
	 *
	 * @param rollUpStrategy
	 */
	public void setRollupStrategy(final String rollUpStrategy)
	{
		this.rollupStrategy = rollUpStrategy;
	}

	/**
	 *
	 * @return List of base sites
	 */
	public List<String> getSites()
	{
		return sites;
	}

	/**
	 * Sets list of base sites
	 *
	 * @param sites
	 */
	public void setSites(final List<String> sites)
	{
		this.sites = sites;
	}

	/**
	 * Returns default Language
	 *
	 * @return defaultLanguage
	 */
	public String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	/**
	 * Sets default Language
	 *
	 * @param defaultLanguage
	 */
	public void setDefaultLanguage(final String defaultLanguage)
	{
		this.defaultLanguage = defaultLanguage;
	}

	@Override
	public String toString()
	{
		return "ProductDirectory{" +
				"id=" + id +
				", name='" + name + '\'' +
				", rollUpStrategy='" + rollupStrategy + '\'' +
				", baseSites=" + sites +
				'}';
	}
}
