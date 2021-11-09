/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('yInfiniteScrollingApp', [
        'coretemplates',
        'templateCacheDecoratorModule',
        'legacySmarteditCommonsModule'
    ])
    .controller('defaultController', function(restServiceFactory) {
        this.pageSize = 10;

        this.loadItems = function(mask, pageSize, currentPage) {
            return restServiceFactory
                .get('/loadItems')
                .get({
                    pageSize: pageSize,
                    currentPage: currentPage,
                    mask: mask
                })
                .then(function(response) {
                    return response;
                });
        };
    });
