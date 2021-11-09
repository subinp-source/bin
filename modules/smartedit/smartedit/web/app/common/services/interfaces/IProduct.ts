/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IProduct
 * @description
 * Interface for product information
 */
export interface IProduct {
    catalogId: string;
    catalogVersion: string;
    code: string;
    description: {
        [index: string]: string;
    };
    name: {
        [index: string]: string;
    };
    thumbnailMediaCode: string;
}
