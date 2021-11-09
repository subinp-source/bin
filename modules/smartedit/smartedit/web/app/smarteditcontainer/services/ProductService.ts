/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IProduct,
    IRestService,
    LanguageService,
    Page,
    Pageable,
    PRODUCT_LIST_RESOURCE_API,
    PRODUCT_RESOURCE_API,
    RestServiceFactory,
    SeDowngradeService
} from 'smarteditcommons';

/**
 * Interface used by ProductService for product search
 */
export interface IProductSearch {
    /**
     * id of the catalog
     */
    catalogId: string;
    /**
     * version of the catalog
     */
    catalogVersion: string;
}

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:ProductService
 * @description
 * The ProductService provides is used to access products from the product catalog
 */
@SeDowngradeService()
export class ProductService {
    private readonly productService: IRestService<IProduct>;
    private readonly productListService: IRestService<Page<IProduct>>;

    constructor(
        private restServiceFactory: RestServiceFactory,
        private languageService: LanguageService
    ) {
        this.productService = this.restServiceFactory.get(PRODUCT_RESOURCE_API);
        this.productListService = this.restServiceFactory.get(PRODUCT_LIST_RESOURCE_API);
    }
    /**
     * Returns a Product that matches the given siteUID and productUID
     */

    public getProductById(siteUID: string, productUID: string): Promise<IProduct> {
        return this.productService.get({
            siteUID,
            productUID
        });
    }
    /**
     * Returns a list of Products from the catalog that match the given mask
     */
    public async findProducts(
        productSearch: IProductSearch,
        pageable: Pageable
    ): Promise<Page<IProduct>> {
        this._validateProductCatalogInfo(productSearch);
        const langIsoCode = await this.languageService.getResolveLocale();
        const list = await this.productListService.get({
            catalogId: productSearch.catalogId,
            catalogVersion: productSearch.catalogVersion,
            text: pageable.mask,
            pageSize: pageable.pageSize,
            currentPage: pageable.currentPage,
            langIsoCode
        });

        return list;
    }

    private _validateProductCatalogInfo(productSearch: IProductSearch) {
        if (!productSearch.catalogId) {
            throw new Error('[productService] - catalog ID missing.');
        }
        if (!productSearch.catalogVersion) {
            throw new Error('[productService] - catalog version missing.');
        }
    }
}
