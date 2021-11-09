/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

export interface IProduct {
    catalogId: string;
    catalogVersion: string;
    code: string;
    uid: string;
    description: {};
    name: {
        en: string;
    };
    thumbnail?: {
        catalogId: string;
        catalogVersion: string;
        code: string;
        downloadUrl: string;
        mime: string;
        url: string;
    };
}
