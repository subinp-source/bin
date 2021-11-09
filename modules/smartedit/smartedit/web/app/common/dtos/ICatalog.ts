/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IHomepageVersions } from './IHomepage';
import { Payload, TypedMap } from '@smart/utils';
import { ISite } from 'smarteditcommons/services';

/** from Backend */

export interface IPageDisplayConditionsOption extends Payload {
    id: string;
    label: string;
}
export interface IPageDisplayConditions extends Payload {
    options: IPageDisplayConditionsOption[];
    typecode: string;
}
export interface IBaseCatalogVersion extends Payload {
    active: boolean;
    pageDisplayConditions: IPageDisplayConditions[];
    uuid: string;
    version: string;
    thumbnailUrl?: string;
}

export interface ICatalogVersion extends IBaseCatalogVersion {
    name?: { [index: string]: string };
    catalogId?: string;
    siteId?: string;
    catalogName?: TypedMap<string>;
    siteDescriptor?: ISite;
    homepage?: IHomepageVersions;
}

export interface IBaseCatalog {
    catalogId: string;
    versions: IBaseCatalogVersion[];
    name?: TypedMap<string>;
}

export interface ICatalog {
    catalogId: string;
    versions: ICatalogVersion[];
    name?: TypedMap<string>;
}
export interface IBaseCatalogs {
    catalogs: IBaseCatalog[];
}

export interface ICatalogs {
    catalogs: ICatalog[];
}
