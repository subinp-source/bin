/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.integration.file.DefaultDirectoryScanner;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;


/**
 * Custom directory scanner for marketplace
 */
public class MarketplaceVendorDirectoryScanner extends DefaultDirectoryScanner
{
	private String fileNamePatten;

	public MarketplaceVendorDirectoryScanner()
	{
		final List<FileListFilter<File>> defaultFilters = new ArrayList<FileListFilter<File>>(2);
		defaultFilters.add(new IgnoreHiddenFileListFilter());
		defaultFilters.add(new AcceptOnceFileListFilter<File>());
		super.setFilter(new CompositeFileListFilter<File>(defaultFilters));
	}

	public String getFileNamePatten()
	{
		return fileNamePatten;
	}

	public void setFileNamePatten(final String fileNamePatten)
	{
		this.fileNamePatten = fileNamePatten;
	}

	@Override
	protected File[] listEligibleFiles(final File root)
	{
		final Stream<File> files = Arrays.stream(root.listFiles()).filter(File::isDirectory).flatMap(d -> Arrays.stream(d.listFiles()))
				.filter(File::isFile)
				.filter(f -> f.getName().matches(fileNamePatten));
		return files.toArray(File[]::new);
	}
}

