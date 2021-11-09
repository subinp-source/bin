/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogVersion } from 'fixtures/entities/versions';

export interface IContentCatalog {
    name: { en: string; de?: string };
    versions: ICatalogVersion[];
    catalogId?: string;
}
