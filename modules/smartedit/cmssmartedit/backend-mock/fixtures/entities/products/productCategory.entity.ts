/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

export interface IProductCategory {
    catalogId: string;
    catalogVersion: string;
    code: string;
    description: {};
    name: {
        en: string;
        de?: string;
    };
    uid: string;
}
