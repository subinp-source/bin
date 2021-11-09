/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.client;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.hybris.charon.RawResponse;
import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.Header;
import com.hybris.charon.annotations.Http;
import com.hybris.charon.annotations.OAuth;
import com.hybris.merchandising.model.Product;
import com.hybris.merchandising.model.ProductDirectory;

import rx.Observable;

/**
 * MerchCatalogServiceProductDirectoryClient is a JAX-RS interface for the Merchandising Catalog Service which supports
 * synchronising products against a product directory.
 */
@OAuth
@Http
public interface MerchCatalogServiceProductDirectoryClient
{
	/**
	 * handleProductsBatch is a handler for accepting a batch of products and sending them to the
	 * Merchandising Catalog service.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param version the version to send the products as.
	 * @param products a list of products to send.
	 * @return String - ID of the operation.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products/versions/{versionId}")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:10000}")
	String handleProductsBatch(@PathParam("productDirectoryId") final String productDirectoryId,
			@PathParam("versionId") final Long version, final List<Product> products);

	/**
	 * handleProductsBatch is a handler for accepting a batch of products and sending them to the
	 * Merchandising Catalog service.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param products a list of products to send.
	 * @return String - ID of the operation.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:10000}")
	Void handleProductsBatch(@PathParam("productDirectoryId") final String productDirectoryId,
			final List<Product> products);

	/**
	 * handleProductsBatchAsync is a handler for accepting a batch of products and sending them to the
	 * Merchandising Catalog service.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param products a list of products to send.
	 * @return String - ID of the operation.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Observable<Void> handleProductsBatchAsync(@PathParam("productDirectoryId") final String productDirectoryId, 
			final List<Product> products);

	/**
	 * handleProductsBatchAsync is a handler for accepting a batch of products and sending them to the
	 * Merchandising Catalog service.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param version the version to send the products as.
	 * @param products a list of products to send.
	 * @return String - ID of the operation.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products/versions/{versionId}")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Observable<String> handleProductsBatchAsync(@PathParam("productDirectoryId") final String productDirectoryId,
			@PathParam("versionId") final Long version, final List<Product> products);

	/**
	 * publishProducts is an API for marking a set of products as being published to live in a
	 * synchronous manner.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param version the version to publish.
	 * @return Void.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products/versions/{versionId}/publish")
	@Header(name="Content-Length", val="0")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:15000}")
	Void publishProducts(@PathParam("productDirectoryId") final String productDirectoryId,
			@PathParam("versionId") final Long version);

	/**
	 * publishProductsAsync is an API for marking a set of products as being published to live in an
	 * asynchronous manner.
	 * @param productDirectoryId - the product directory ID we wish to associate the products with.
	 * @param version the version to publish.
	 * @return an Observable containing Void.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/products/versions/{versionId}/publish")
	@Header(name="Content-Length", val="0")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:15000}")
	Observable<Void> publishProductsAsync(@PathParam("productDirectoryId") final String productDirectoryId,
			@PathParam("versionId") final Long version);

	/**
	 * handleCategoriesAsync is an asynchronous method for sending categories to CDS.
	 * @param productDirectoryId - the product directory ID we wish to associate the categories with.
	 * @param categories the {@link CategoryHierarchyWrapper} object to publish.
	 * @return an Observable containing the response.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/categories")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Observable<String> handleCategoriesAsync(@PathParam("productDirectoryId") final String productDirectoryId, 
			final CategoryHierarchyWrapper categories);

	/**
	 * handleCategories is a method for sending categories to CDS.
	 * @param productDirectoryId - the product directory ID we wish to associate the categories with.
	 * @param categories the {@link CategoryHierarchyWrapper} object to publish.
	 * @return String - ID of the operation.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories/{productDirectoryId}/categories")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	String handleCategories(@PathParam("productDirectoryId") final String productDirectoryId, 
			final CategoryHierarchyWrapper categories);

	/**
	 * createProductDirectoryAsync is a method for creating a product directory and synchronising with CDS.
	 * @param productDirectory the {@link ProductDirectory} we wish to create.
	 * @return Observable representing the {@link RawResponse} of the operation.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Observable<RawResponse> createProductDirectoryAsync(final ProductDirectory productDirectory);

	/**
	 * createProductDirectory is a method for creating a product directory and synchronising with CDS.
	 * @param productDirectory the {@link ProductDirectory} we wish to create.
	 * @return a {@link RawResponse} representing the response of the operation.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/${tenant}/productdirectories")
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	RawResponse createProductDirectory(final ProductDirectory productDirectory);

	/**
	 * updateProductDirectoryAsync is a method for updating an existing product directory and synchronising with CDS.
	 * @param productDirectoryId the ID of the {@link ProductDirectory} to update.
	 * @param productDirectory the {@link ProductDirectory} to update.
	 * @return an Observable containing Void.
	 */
	@PUT
	@Path("/${tenant}/productdirectories/{productDirectoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Observable<Void> updateProductDirectoryAsync(@PathParam("productDirectoryId") final String productDirectoryId,
			final ProductDirectory productDirectory);

	/**
	 * updateProductDirectory is a method for updating an existing product directory and synchronising with CDS.
	 * @param productDirectoryId the ID of the {@link ProductDirectory} to update.
	 * @param productDirectory the {@link ProductDirectory} to update.
	 * @return Void.
	 */
	@PUT
	@Path("/${tenant}/productdirectories/{productDirectoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:30000}")
	Void updateProductDirectory(@PathParam("productDirectoryId") final String productDirectoryId,
			final ProductDirectory productDirectory);

	/**
	 * deleteProductDirectoryAsync is a method for deleting an existing product directory and synchronising with CDS.
	 * @param productDirectoryId the ID of the {@link ProductDirectory} to delete.
	 * @return an Observable containing Void.
	 */
	@DELETE
	@Path("/${tenant}/productdirectories/{productDirectoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:5000}")
	Observable<Void> deleteProductDirectoryAsync(@PathParam("productDirectoryId") final String productDirectoryId);

	/**
	 * deleteProductDirectory is a method for deleting an existing product directory and synchronising with CDS.
	 * @param productDirectoryId the ID of the {@link ProductDirectory} to delete.
	 * @return Void.
	 */
	@DELETE
	@Path("/${tenant}/productdirectories/{productDirectoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Control(retries = "${retries:0}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:5000}")
	Void deleteProductDirectory(@PathParam("productDirectoryId") final String productDirectoryId);
}
