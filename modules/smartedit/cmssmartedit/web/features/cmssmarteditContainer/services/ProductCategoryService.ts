/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import {
    IRestService,
    LanguageService,
    Page,
    Pageable,
    RestServiceFactory,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';

interface IProductCatalogInfo {
    catalogId: string;
    catalogVersion: string;
    siteUID: string;
}

export type IProductCategorySearchPayload = IProductCatalogInfo & Pageable;

export interface IProductCategory {
    catalogId: string;
    catalogVersion: string;
    code: string;
    description: TypedMap<string>;
    id: string;
    name: TypedMap<string>;
    technicalUniqueId: string;
    uid: string;
}
export const PRODUCT_CATEGORY_RESOURCE_BASE_URI =
    '/cmssmarteditwebservices/v1/sites/:siteUID/categories';
export const PRODUCT_CATEGORY_RESOURCE_URI = `${PRODUCT_CATEGORY_RESOURCE_BASE_URI}/:categoryUID`;
export const PRODUCT_CATEGORY_SEARCH_RESOURCE_URI =
    '/cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:catalogVersion/categories';

@SeDowngradeService()
@Injectable()
export class ProductCategoryService {
    private productCategoryService: IRestService<IProductCategory>;
    private productCategorySearchService: IRestService<IProductCategory>;

    constructor(restServiceFactory: RestServiceFactory, private languageService: LanguageService) {
        this.productCategoryService = restServiceFactory.get<IProductCategory>(
            PRODUCT_CATEGORY_RESOURCE_URI
        );
        this.productCategorySearchService = restServiceFactory.get<IProductCategory>(
            PRODUCT_CATEGORY_SEARCH_RESOURCE_URI
        );
    }

    public getCategoryById(siteUID: string, categoryUID: string): Promise<IProductCategory> {
        return this.productCategoryService.get({
            siteUID,
            categoryUID
        });
    }

    public getCategories(payload: IProductCategorySearchPayload): Promise<Page<IProductCategory>> {
        this._validateProductCatalogInfo(payload);
        return this.languageService.getResolveLocale().then((langIsoCode) => {
            return this.productCategorySearchService.page({
                catalogId: payload.catalogId,
                catalogVersion: payload.catalogVersion,
                text: payload.mask,
                pageSize: payload.pageSize,
                currentPage: payload.currentPage,
                langIsoCode
            });
        }) as any;
    }

    private _validateProductCatalogInfo(productCatalogInfo: IProductCatalogInfo): void {
        if (!productCatalogInfo.siteUID) {
            throw Error('[productService] - site UID missing.');
        }
        if (!productCatalogInfo.catalogId) {
            throw Error('[productService] - catalog ID missing.');
        }
        if (!productCatalogInfo.catalogVersion) {
            throw Error('[productService] - catalog version  missing.');
        }
    }
}
