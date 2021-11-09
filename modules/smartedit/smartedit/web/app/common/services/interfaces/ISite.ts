/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:ISite
 * @description
 * Interface for site information
 */
export interface ISite extends Payload {
    contentCatalogs: string[];
    name: {
        [index: string]: string;
    };
    previewUrl: string;
    uid: string;
}
