/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPage } from './../../entities/pages';

export const pages: IPage[] = [
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'PageTemplate',
        name: 'page1TitleSuffix',
        label: 'page1TitleSuffix',
        typeCode: 'ContentPage',
        uid: 'auid1'
    },
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'PageTemplate',
        name: 'page1TitleSuffix',
        typeCode: 'ContentPage',
        uid: 'auid1'
    },
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'ActionTemplate',
        name: 'welcomePage',
        typeCode: 'ActionPage',
        uid: 'uid2'
    },
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'PageTemplate',
        name: 'Advertise',
        typeCode: 'MyCustomType',
        uid: 'uid3'
    },
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'MyCustomPageTemplate',
        name: 'page2TitleSuffix',
        typeCode: 'HomePage',
        uid: 'uid4'
    },
    {
        creationtime: '2016-04-08T21:16:41+0000',
        modifiedtime: '2016-04-08T21:16:41+0000',
        pk: '8796387968048',
        masterTemplate: 'ZTemplate',
        name: 'page3TitleSuffix',
        typeCode: 'ProductPage',
        uid: 'uid5'
    },
    {
        type: 'contentPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Primary Content Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Primary Content Page'
        },
        typeCode: 'ContentPage',
        uid: 'primaryContentPage',
        label: 'primary-content-page'
    },
    {
        type: 'contentPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Variation Content Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Variation Content Page'
        },
        typeCode: 'ContentPage',
        itemtype: 'ContentPage',
        uid: 'variationContentPage',
        label: 'variation-content-page'
    },
    {
        type: 'categoryPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Primary Category Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Primary Category Page'
        },
        typeCode: 'CategoryPage',
        uid: 'primaryCategoryPage',
        label: 'primary-category-page'
    },
    {
        type: 'categoryPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Variation Category Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Variation Category Page'
        },
        typeCode: 'CategoryPage',
        uid: 'variationCategoryPage',
        label: 'variation-category-page'
    },
    {
        type: 'productPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Primary Product Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Primary Product Page'
        },
        typeCode: 'ProductPage',
        uid: 'primaryProductPage',
        label: 'primary-product-page'
    },
    {
        type: 'productPageData',
        creationtime: '2016-07-07T14:33:37+0000',
        defaultPage: true,
        modifiedtime: '2016-07-12T01:23:41+0000',
        name: 'My Little Variation Product Page',
        onlyOneRestrictionMustApply: true,
        pk: '8796101182512',
        masterTemplate: 'SomePageTemplate',
        title: {
            en: 'Variation Product Page'
        },
        typeCode: 'ProductPage',
        uid: 'variationProductPage',
        label: 'variation-product-page'
    },
    {
        type: 'contentPageData',
        creationtime: '2016-06-28T15:23:37+0000',
        defaultPage: false,
        modifiedtime: '2016-06-28T15:25:51+0000',
        name: 'Homepage',
        pk: '8796101182512',
        masterTemplate: 'AccountPageTemplate',
        title: {
            de: 'Mes lovens pendas',
            en: 'I love pandas'
        },
        typeCode: 'ContentPage',
        uid: 'homepage',
        label: 'i-love-pandas'
    },
    {
        type: 'contentPageData',
        creationtime: '2016-06-28T15:23:37+0000',
        defaultPage: true,
        modifiedtime: '2016-06-28T15:25:51+0000',
        name: 'Some Other Page',
        pk: '8796101182512',
        masterTemplate: 'ProductPageTemplate',
        title: {
            de: 'Mes hatens pendas',
            en: 'I hate pandas'
        },
        typeCode: 'ProductPage',
        uid: 'secondpage',
        label: 'i-hate-pandas'
    },
    {
        type: 'contentPageData',
        creationtime: '2018-06-28T15:23:37+0000',
        defaultPage: true,
        modifiedtime: '2018-06-28T15:25:51+0000',
        name: 'Some Other Page',
        pk: '8796101182513',
        masterTemplate: 'ProductPageTemplate',
        title: {
            en: 'Third page'
        },
        typeCode: 'ProductPage',
        uid: 'thirdpage',
        label: 'third-page'
    }
];
