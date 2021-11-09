/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface ISyncJob {
    catalogId: string;
    name: string;
    sourceCatalogVersion: string;
    targetCatalogVersion: string;
    creationDate: string;
    startDate: string;
    endDate: string;
    syncStatus: string;
    syncResult: string;
    code: string;
    lastModifiedDate: string;
}
