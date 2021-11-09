/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface SynchronizationJob {
    creationDate: string;
    syncStatus: string;
    endDate: string;
    lastModifiedDate: string;
    syncResult: string;
    startDate: string;
    sourceCatalogVersion: string;
    targetCatalogVersion: string;
    code?: string;
}
