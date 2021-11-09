/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogVersion } from 'smarteditcommons';

/**
 * @internal
 *
 * @description
 * Interface for Cloneable Catalog Version response
 *
 * Result of CatalogVersionRestService.getCloneableTargets
 */
export interface ICloneableCatalogVersion {
    versions: ICatalogVersion[];
}
