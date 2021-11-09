/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ILanguage, Payload, Primitive, TypedMap } from '@smart/utils';
import { ISite } from './ISite';

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IExperience
 * @description
 * IExperience - Interface for experience information
 */
export interface IExperience extends Payload {
    catalogDescriptor: IExperienceCatalogDescriptor;
    siteDescriptor: ISite;
    productCatalogVersions: IExperienceCatalogVersion[];
    time: string;

    languageDescriptor?: ILanguage;
    pageId?: string;
    pageContext?: IExperiencePageContext;
    [index: string]: Primitive | Primitive[] | Payload | Payload[];
}

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IExperienceCatalogDescriptor
 * @description
 * IExperienceCatalogDescriptor interface acts as a pointer to catalog and catalogVersion of the experience
 */
export interface IExperienceCatalogDescriptor extends Payload {
    active: boolean;
    catalogId: string;
    catalogVersion: string;
    catalogVersionUuid: string;
    name: TypedMap<string>;
    siteId: string;
}

export interface IExperienceCatalogVersion extends Payload {
    active: boolean;
    catalog: string;
    catalogName: TypedMap<string>;
    catalogVersion: string;
    uuid: string;
}

export interface IExperiencePageContext extends Payload {
    catalogId: string;
    catalogName: TypedMap<string>;
    catalogVersion: string;
    catalogVersionUuid: string;
    siteId: string;
    active: boolean;
}

export interface IDefaultExperienceParams {
    siteId: string;
    catalogId: string;
    catalogVersion: string;
    pageId?: string;
}

export interface IExperienceParams extends IDefaultExperienceParams {
    time?: string;
    productCatalogVersions?: string[];
    language?: string;
}
