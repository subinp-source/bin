/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, Param } from '@nestjs/common';

import {
    apparelOnlineProducts,
    apparelOnlineProducts2,
    apparelStagedProducts,
    products,
    productCategories
} from 'fixtures/constants/products';
import { IProduct, IProductCategory } from 'fixtures/entities/products';

@Controller()
export class ProductsController {
    @Get('cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:versionId/categories*')
    getProductCategoriesByFreeTextSearch(@Param('versionId') versionId: string) {
        const filteredCategories: IProductCategory[] = productCategories.filter(
            (cateogry: IProductCategory) => cateogry.catalogVersion === versionId
        );
        return {
            pagination: {
                count: 10,
                page: 1,
                totalCount: 10,
                totalPages: 1
            },
            productCategories: filteredCategories
        };
    }

    @Get('cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:versionId/products*')
    getProductsByFreeTextSearch(
        @Param('catalogId') catalogId: string,
        @Param('versionId') versionId: string
    ) {
        if (catalogId === 'apparelProductCatalog_2' && versionId === 'Online') {
            return {
                pagination: {
                    count: 2,
                    page: 1,
                    totalCount: 2,
                    totalPages: 1
                },
                products: apparelOnlineProducts2
            };
        }
        const resultProducts: IProduct[] =
            versionId === 'Online' ? apparelOnlineProducts : apparelStagedProducts;
        return {
            pagination: {
                count: 10,
                page: 1,
                totalCount: 10,
                totalPages: 1
            },
            products: resultProducts
        };
    }

    @Get('cmssmarteditwebservices/v1/sites/:baseSiteId/categories/:code')
    getCategoryByCode(@Param('code') code: string) {
        const resultCategory: IProductCategory | undefined = productCategories.find(
            (category: IProductCategory) => category.uid === code
        );
        return resultCategory ? resultCategory : productCategories[0];
    }

    @Get('cmssmarteditwebservices/v1/sites/:baseSiteId/products/:code')
    getProductByCode(@Param('code') code: string) {
        const resultProduct: IProduct | undefined = products[code];
        return resultProduct ? resultProduct : products;
    }
}
