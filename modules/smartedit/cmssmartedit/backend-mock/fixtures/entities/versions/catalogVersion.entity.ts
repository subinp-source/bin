/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IPageDisplayCondition } from 'fixtures/entities/pages';

export interface ICatalogVersion {
    active: boolean;
    version: string;
    pageDisplayConditions?: IPageDisplayCondition[];
    uuid?: string;
    thumbnailUrl?: string;
    homepage?: {
        current: {
            uid: string;
            name: string;
            catalogVersionUuid: string;
        };
        old: {
            uid: string;
            name: string;
            catalogVersionUuid: string;
        };
    };
}
